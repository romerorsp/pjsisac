package br.com.sisac.view;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import br.com.sisac.dao.DAOPessoa;
import br.com.sisac.dao.DAOReserva;
import br.com.sisac.model.Mail;
import br.com.sisac.model.Pessoa;
import br.com.sisac.model.Relatorio;
import br.com.sisac.model.Reserva;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;

@Named
@Scope(value="session")
@SuppressWarnings("deprecation")
public class ManagedBeanReserva extends Relatorio{

    private @Autowired DAOReserva daoReserva;
    private @Autowired DAOPessoa daoPessoa;
    private List<Reserva> reservas;
    private List<Pessoa> pessoas;
    private Reserva reserva;

    @PostConstruct
    public void init() {
        reservas = daoReserva.listar();
        pessoas = daoPessoa.listar(false);
    }

    public DAOReserva getDaoReserva() {
        return daoReserva;
    }

    public void setDaoReserva(DAOReserva daoReserva) {
        this.daoReserva = daoReserva;
    }

    public void atualizarPessoa() {
        for (Pessoa p : pessoas) {
            if (reserva.getPessoa().getId() == p.getId()) {
                reserva.setPessoa(p);
            }
        }
    }

    public boolean validar() {
        boolean valido = true;
        if ("".equals(reserva.getDataReserva()) || "".equals(reserva.getDataUso())) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Os campos com (*) devem ser preenchidos obrigatoriamente!", ""));
            valido = false;
        }
        return valido;
    }

    public List<SelectItem> getPessoas() {
        List<SelectItem> itens = new ArrayList<SelectItem>(pessoas.size());

        for (Pessoa p : pessoas) {
            itens.add(new SelectItem(p.getId(), p.getNome()));
        }

        return itens;
    }

    public String submitAcao(HttpServletRequest request) {
        int acao = ManagedBeanUtil.getAcaoParam();
        long id = 0;
        if (acao != ManagedBeanUtil.ACAO_IR_PARA_PESQUISAR && acao != ManagedBeanUtil.ACAO_PESQUISAR
                && acao != ManagedBeanUtil.ACAO_IR_PARA_LISTAR) {
            id = ManagedBeanUtil.getIdParam();
        }
        Reserva r = new Reserva();

        switch (acao) {
            case ManagedBeanUtil.ACAO_IR_PARA_LISTAR:
                reservas = daoReserva.listar();
                setReserva(new Reserva());
                return "lista-reserva.xhtml";
            case ManagedBeanUtil.ACAO_EXCLUIR:
                r.setId(id);
                r = daoReserva.consultarPorId(r);
                daoReserva.excluir(r);
                setReserva(new Reserva());
                reservas = daoReserva.listar();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Exclusão realizada com sucesso!", ""));
                return "lista-reserva.xhtml";
            case ManagedBeanUtil.ACAO_IR_PARA_ALTERAR:
                r.setId(id);
                setReserva(daoReserva.consultarPorId(r));
                pessoas = daoPessoa.listar(false);
                return "cadastro-reserva.xhtml";
            case ManagedBeanUtil.ACAO_IR_PARA_PESQUISAR:
                reserva = new Reserva();
                setReserva(reserva);
                return "pesquisa-reserva.xhtml";
            case ManagedBeanUtil.ACAO_PESQUISAR:
                reservas = daoReserva.consultarPorFiltro(reserva);
                return "lista-reserva.xhtml";
            case ManagedBeanUtil.ACAO_IR_PARA_INCLUIR:
                setReserva(new Reserva());
                pessoas = daoPessoa.listar(false);
                return "cadastro-reserva.xhtml";
            case ManagedBeanUtil.ACAO_ATUALIZAR:
                if (!validar()) {
                    return "cadastro-reserva.xhtml";
                }
                Pessoa p = daoPessoa.consultarPorId(reserva.getPessoa());
                reserva.setPessoa(p);
                String mensagem = (reserva.getId() != 0L)
                        ? "Alteração realizada com sucesso!"
                        : "Cadastrado com sucesso!";
                Mail.enviaEmail(p.getEmail(), p.getNome(), "Reserva Sisac", "Reserva Confirmada" +r.getTipo(), request);
                daoReserva.atualizar(reserva);
                reservas = daoReserva.listar();
                setReserva(new Reserva());
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                        mensagem, ""));
                return "lista-reserva.xhtml";
            default:
                return "lista-reserva.xhtml";
        }
    }

    public DAOPessoa getDaoPessoa() {
        return daoPessoa;
    }

    public void setDaoPessoa(DAOPessoa daoPessoa) {
        this.daoPessoa = daoPessoa;
    }

    public List<Reserva> getReservas() {
        return reservas;
    }

    public void setReservas(List<Reserva> reservas) {
        this.reservas = reservas;
    }

    public Reserva getReserva() {
        return reserva;
    }

    public void setReserva(Reserva reserva) {
        this.reserva = reserva;
    }

    public void setPessoas(List<Pessoa> pessoas) {
        this.pessoas = pessoas;
    }
    public void emitir()throws Exception{
        Map<String, Object> parametros = new HashMap<String, Object>();
//    if(this.loteMovimentarDAO.selecionarOcupacaoReport(this.objRazao.getRazao_social(),area,fazenda.getCodigo(),this.data_de, this.data_ate).isEmpty()){
        FacesMessage msg = new FacesMessage("Nenhuma Ocupação Encontrada de Acordo com o Filtro!");
        FacesContext.getCurrentInstance().addMessage(null, msg);
////    }else{
//   List<LoteMovimentar> lista = this.loteMovimentarDAO.selecionarOcupacaoReport(this.objRazao.getRazao_social(),area,fazenda.getCodigo(),this.data_de, this.data_ate);
//    parametros.put("list", lista);
         
    
    String caminho;
    String dirImage;
//    parametros.put("data_de", this.data_de);

    
    
    FacesContext facesContext = FacesContext.getCurrentInstance(); 
    facesContext.responseComplete(); 
    ServletContext scontext = (ServletContext)facesContext.getExternalContext().getContext();
    caminho = scontext.getRealPath("reports/relatorioAgendamento.jasper");
    System.out.println(caminho);
    dirImage = scontext.getRealPath("imagensEmpresa");
    dirImage += "//"; 
    dirImage += "logoMini.png";

    
    parametros.put("dir_image", dirImage);
    String subDir;
    subDir = scontext.getRealPath("reportManejo");
    parametros.put("REPORT_LOCALE", new Locale("pt","BR")) ;
    parametros.put("SUBREPORT_DIR", subDir);
    
//    Connection conn = null;
//        try {
//            Class.forName("com.mysql.jdbc.Driver");
////            conn = DriverManager.getConnection("jdbc:mysql://mysql.redbov.com.br:3306/sist62br4?autoReconnect=true","sist62br4","3758EGvx6n");
//              conn = DriverManager.getConnection("jdbc:mysql://mysql.redbov.com.br:3306/redbovbr?autoReconnect=true","redbovbr","u2wJEsxb44");
////              conn = DriverManager.getConnection("jdbc:mysql://mysql.redbov.com.br:3306/redbovbr4?autoReconnect=true","redbovbr4","wRVVYaVFv6");
////            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_redbov","root","admin");
//        } catch (ClassNotFoundException ex) {
//            Logger.getLogger(Relatorio.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    parametros.put("conn", conn);
//    parametros.put("conn", Utilidades.gerarConexao());
    
    @SuppressWarnings("unused") JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(this.reservas); 
    
    
    
   JasperPrint jasperPrint = criaRelatorio(caminho, parametros);
    
   try{             
                    ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
                    JRPdfExporter exporter = new JRPdfExporter(); 
                    exporter.setParameter(JRExporterParameter.JASPER_PRINT,jasperPrint); 
                    exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos); 
                    exporter.exportReport(); 
                    byte[] bytes = baos.toByteArray(); 
                    if (bytes != null && bytes.length > 0) { 
                            HttpServletResponse response = (HttpServletResponse)facesContext.getExternalContext().getResponse(); 
                            response.setContentType("application/pdf"); 
                            
                            response.setHeader("Content-disposition","inline; filename=\"relatorioOcupacao"+new SimpleDateFormat("ddMMYYY").format(new Date())+".pdf\""); 
                            
                            response.setContentLength(bytes.length); 
                            ServletOutputStream outputStream = response.getOutputStream(); 
                            outputStream.write(bytes, 0, bytes.length); 
                            outputStream.flush(); 
                            outputStream.close(); 
                    } 
                
   }catch(Exception e){
       e.printStackTrace();
   }
    }
}
