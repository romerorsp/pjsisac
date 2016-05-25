
package br.com.sisac.model;


import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;

/**
 *
 * @author Bruno Villanova
 * @since 2013-12-09
 * 
 */
@ManagedBean
@SessionScoped
@SuppressWarnings("deprecation")
public class ReportOcupacao extends Relatorio implements java.io.Serializable {


	private static final long serialVersionUID = -1268565359736041406L;
	private String razao = null;    
	@SuppressWarnings("unused")
	private int area = 0;    
	private Date data_de = null;    
	private Date data_ate = null;    

	@SuppressWarnings("unused")
	private HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);    

	public ReportOcupacao() {}

	public String getRazao() {
		return razao;
	}

	public void setRazao(String razao) {
		this.razao = razao;
	}

	public Date getData_de() {
		return data_de;
	}

	public void setData_de(Date data_de) {
		this.data_de = data_de;
	}

	public Date getData_ate() {
		return data_ate;
	}

	public void setData_ate(Date data_ate) {
		this.data_ate = data_ate;
	}

	public void emitir()throws Exception{
		Map<String, Object> parametros = new HashMap<String, Object>();
		//        if(this.loteMovimentarDAO.selecionarOcupacaoReport(this.objRazao.getRazao_social(),area,fazenda.getCodigo(),this.data_de, this.data_ate).isEmpty()){
		FacesMessage msg = new FacesMessage("Nenhuma Ocupação Encontrada de Acordo com o Filtro!");
		FacesContext.getCurrentInstance().addMessage(null, msg);
		////        }else{
		//       List<LoteMovimentar> lista = this.loteMovimentarDAO.selecionarOcupacaoReport(this.objRazao.getRazao_social(),area,fazenda.getCodigo(),this.data_de, this.data_ate);
		//        parametros.put("list", lista);


		String caminho;
		String dirImage;
		//        parametros.put("data_de", this.data_de);



		FacesContext facesContext = FacesContext.getCurrentInstance(); 
		facesContext.responseComplete(); 
		ServletContext scontext = (ServletContext)facesContext.getExternalContext().getContext();
		caminho = scontext.getRealPath("reportManejo/relatorioOcupacao.jasper");

		dirImage = scontext.getRealPath("imagensEmpresa");
		dirImage += "//"; 
		dirImage += "logoMini.png";

		parametros.put("data", this.data_de);
		parametros.put("data_de", this.data_de);
		parametros.put("data_ate", this.data_ate);
		parametros.put("dir_image", dirImage);
		String subDir;
		subDir = scontext.getRealPath("reportManejo");
		parametros.put("REPORT_LOCALE", new Locale("pt","BR")) ;
		parametros.put("SUBREPORT_DIR", subDir);

		//        Connection conn = null;
		//            try {
		//                Class.forName("com.mysql.jdbc.Driver");
		////                conn = DriverManager.getConnection("jdbc:mysql://mysql.redbov.com.br:3306/sist62br4?autoReconnect=true","sist62br4","3758EGvx6n");
		//                  conn = DriverManager.getConnection("jdbc:mysql://mysql.redbov.com.br:3306/redbovbr?autoReconnect=true","redbovbr","u2wJEsxb44");
		////                  conn = DriverManager.getConnection("jdbc:mysql://mysql.redbov.com.br:3306/redbovbr4?autoReconnect=true","redbovbr4","wRVVYaVFv6");
		////                conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_redbov","root","admin");
		//            } catch (ClassNotFoundException ex) {
		//                Logger.getLogger(Relatorio.class.getName()).log(Level.SEVERE, null, ex);
		//            }
		//        parametros.put("conn", conn);
		//        parametros.put("conn", Utilidades.gerarConexao());

		JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(new ArrayList<>()); 



		JasperPrint jasperPrint = criaRelatorioDS(caminho, parametros,ds);

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