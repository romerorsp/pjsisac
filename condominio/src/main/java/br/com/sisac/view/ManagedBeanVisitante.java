package br.com.sisac.view;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.imageio.stream.FileImageOutputStream;

import org.primefaces.event.CaptureEvent;

import br.com.sisac.dao.DAOPessoa;
import br.com.sisac.model.Pessoa;

public class ManagedBeanVisitante {

    private DAOPessoa daoPessoa;
    private List<Pessoa> pessoas;
    private Pessoa pessoa;

    @PostConstruct
    public void init() {
        pessoas = daoPessoa.listar(true);
    }

    public DAOPessoa getDaoPessoa() {
        return daoPessoa;
    }

    public void setDaoPessoa(DAOPessoa daoPessoa) {
        this.daoPessoa = daoPessoa;
    }

    public List<Pessoa> getPessoas() {
        return pessoas;
    }

    public void setPessoas(List<Pessoa> pessoas) {
        this.pessoas = pessoas;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public void visitar() {

    }

    public boolean validar() {
        boolean valido = true;
        if ("".equals(pessoa.getNome())) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Campo 'Nome' é obrigatório.", ""));
            valido = false;
        }
        if ("".equals(pessoa.getSobrenome())) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Campo 'Sobrenome' é obrigatório.", ""));
            valido = false;
        }
        if ("".equals(pessoa.getRg())) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Campo 'RG' é obrigatório.", ""));
            valido = false;
        }
        if ("".equals(pessoa.getCpf())) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Campo 'CPF' é obrigatório.", ""));
            valido = false;
        }
        return valido;
    }

    public String submitAcao() {
        int acao = ManagedBeanUtil.getAcaoParam();
        long id = 0;
        if (acao != ManagedBeanUtil.ACAO_IR_PARA_PESQUISAR && acao != ManagedBeanUtil.ACAO_PESQUISAR
                && acao != ManagedBeanUtil.ACAO_IR_PARA_LISTAR) {
            id = ManagedBeanUtil.getIdParam();
        }
        Pessoa p = new Pessoa();

        switch (acao) {
            case ManagedBeanUtil.ACAO_VISITAR:
                p.setId(id);
                p = daoPessoa.consultarPorId(p);
                p.setQuantidadeVisita(p.getQuantidadeVisita() + 1);
                daoPessoa.atualizar(p);
                setPessoa(new Pessoa());
                pessoas = daoPessoa.listar(true);
                return "lista-visitante.xhtml";
            case ManagedBeanUtil.ACAO_IR_PARA_LISTAR:
                pessoas = daoPessoa.listar(true);
                setPessoa(new Pessoa());
                return "lista-visitante.xhtml";
            case ManagedBeanUtil.ACAO_EXCLUIR:
                p.setId(id);
                p = daoPessoa.consultarPorId(p);
                daoPessoa.excluir(p);
                setPessoa(new Pessoa());
                pessoas = daoPessoa.listar(true);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Exclusão realizada com sucesso!", ""));
                return "lista-visitante.xhtml";
            case ManagedBeanUtil.ACAO_IR_PARA_ALTERAR:
                p.setId(id);
                setPessoa(daoPessoa.consultarPorId(p));
                return "cadastro-visitante.xhtml";
            case ManagedBeanUtil.ACAO_IR_PARA_PESQUISAR:
                pessoa = new Pessoa();
                pessoa.setTipoPessoa(Pessoa.TIPO_PESSOA_VISITANTE);
                setPessoa(pessoa);
                return "pesquisa-visitante.xhtml";
            case ManagedBeanUtil.ACAO_PESQUISAR:
                pessoa.setTipoPessoa(Pessoa.TIPO_PESSOA_VISITANTE);
                pessoas = daoPessoa.consultarPorFiltro(pessoa);
                return "lista-visitante.xhtml";
            case ManagedBeanUtil.ACAO_IR_PARA_INCLUIR:
                setPessoa(new Pessoa());
                return "cadastro-visitante.xhtml";
            case ManagedBeanUtil.ACAO_ATUALIZAR:
                if (!validar()) {
                    return "cadastro-visitante.xhtml";
                }

                String mensagem = (pessoa.getId() != 0L)
                        ? "Alteração realizada com sucesso!"
                        : "Cadastrado com sucesso!";

                pessoa.setTipoPessoa(Pessoa.TIPO_PESSOA_VISITANTE);
                daoPessoa.atualizar(pessoa);

                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                        mensagem, ""));

                pessoas = daoPessoa.listar(true);
                setPessoa(new Pessoa());
                return "lista-visitante.xhtml";
            default:
                return "lista-visitante.xhtml";
        }
    }
    
    private String getRandomImageName() {
        int i = (int) (Math.random() * 10000000);
         
        return String.valueOf(i);
    }
    
    public void oncapture(CaptureEvent captureEvent) {
    	String filename = getRandomImageName();
        byte[] data = captureEvent.getData();
 
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        
        String newFileName = externalContext.getRealPath("") + "resources" + File.separator + "visitante" + File.separator + filename + ".jpg";
        pessoa.setImagem(filename);
         System.out.println("Caminho " + newFileName);
        FileImageOutputStream imageOutput;
        try {
            imageOutput = new FileImageOutputStream(new File(newFileName));
            imageOutput.write(data, 0, data.length);
            imageOutput.close();
        }
        catch(IOException e) {
            throw new FacesException("Erro ao Capiturar imagem.", e);
        }
    }
    
    /*
     * String newFileName = externalContext.getRealPath("") + "resources" + File.separator + "visitante" + File.separator + pessoa.getImagem();
     *  try {
            imageOutput = new FileImageInputStream(new File(newFileName));
            imageOutput.read(data, 0, data.length);
            imageOutput.close();
        }
        catch(IOException e) {
            throw new FacesException("Erro ao Capiturar imagem.", e);
        }
     * 
     */
}
