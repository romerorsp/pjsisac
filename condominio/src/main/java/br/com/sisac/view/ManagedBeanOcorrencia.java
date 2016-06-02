package br.com.sisac.view;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Named;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import br.com.sisac.dao.DAOOcorrencia;
import br.com.sisac.dao.DAOPessoa;
import br.com.sisac.model.Ocorrencia;
import br.com.sisac.model.Pessoa;

@Named
@Scope(value="session")
public class ManagedBeanOcorrencia {

    private @Autowired DAOOcorrencia daoOcorrencia;
    private @Autowired DAOPessoa daoPessoa;
    private List<Ocorrencia> ocorrencias;
    private List<Pessoa> pessoas;
    private Ocorrencia ocorrencia;

    @PostConstruct
    public void init() {
        ocorrencias = daoOcorrencia.listar();
        pessoas = daoPessoa.listar(false);
    }

    public DAOOcorrencia getDaoOcorrencia() {
        return daoOcorrencia;
    }

    public void setDaoOcorrencia(DAOOcorrencia daoOcorrencia) {
        this.daoOcorrencia = daoOcorrencia;
    }

    public void atualizarPessoa() {
        for (Pessoa p : pessoas) {
            if (ocorrencia.getPessoa().getId() == p.getId()) {
                ocorrencia.setPessoa(p);
            }
        }
    }

    public boolean validar() {
        boolean valido = true;
        if ("".equals(ocorrencia.getDataOcorrencia()) || "".equals(ocorrencia.getDataOcorrencia())) {
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

    public String submitAcao() {
        int acao = ManagedBeanUtil.getAcaoParam();
        long id = 0;
        if (acao != ManagedBeanUtil.ACAO_IR_PARA_PESQUISAR && acao != ManagedBeanUtil.ACAO_PESQUISAR
                && acao != ManagedBeanUtil.ACAO_IR_PARA_LISTAR) {
            id = ManagedBeanUtil.getIdParam();
        }
        Ocorrencia r = new Ocorrencia();

        switch (acao) {
            case ManagedBeanUtil.ACAO_IR_PARA_LISTAR:
                ocorrencias = daoOcorrencia.listar();
                setOcorrencia(new Ocorrencia());
                return "lista-ocorrencia.xhtml";
            case ManagedBeanUtil.ACAO_EXCLUIR:
                r.setId(id);
                r = daoOcorrencia.consultarPorId(r);
                daoOcorrencia.excluir(r);
                setOcorrencia(new Ocorrencia());
                ocorrencias = daoOcorrencia.listar();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Exclusão realizada com sucesso!", ""));
                return "lista-ocorrencia.xhtml";
            case ManagedBeanUtil.ACAO_IR_PARA_ALTERAR:
                r.setId(id);
                setOcorrencia(daoOcorrencia.consultarPorId(r));
                pessoas = daoPessoa.listar(false);
                return "cadastro-ocorrencia.xhtml";
            case ManagedBeanUtil.ACAO_IR_PARA_PESQUISAR:
                ocorrencia = new Ocorrencia();
                setOcorrencia(ocorrencia);
                return "pesquisa-ocorrencia.xhtml";
            case ManagedBeanUtil.ACAO_PESQUISAR:
                ocorrencias = daoOcorrencia.consultarPorFiltro(ocorrencia);
                return "lista-ocorrencia.xhtml";
            case ManagedBeanUtil.ACAO_IR_PARA_INCLUIR:
                setOcorrencia(new Ocorrencia());
                pessoas = daoPessoa.listar(false);
                return "cadastro-ocorrencia.xhtml";
            case ManagedBeanUtil.ACAO_ATUALIZAR:
                if (!validar()) {
                    return "cadastro-ocorrencia.xhtml";
                }
                Pessoa p = daoPessoa.consultarPorId(ocorrencia.getPessoa());
                ocorrencia.setPessoa(p);
                String mensagem = (ocorrencia.getId() != 0L)
                        ? "Alteração realizada com sucesso!"
                        : "Cadastrado com sucesso!";

                daoOcorrencia.atualizar(ocorrencia);
                ocorrencias = daoOcorrencia.listar();
                setOcorrencia(new Ocorrencia());
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                        mensagem, ""));
                return "lista-ocorrencia.xhtml";
            default:
                return "lista-ocorrencia.xhtml";
        }
    }

    public DAOPessoa getDaoPessoa() {
        return daoPessoa;
    }

    public void setDaoPessoa(DAOPessoa daoPessoa) {
        this.daoPessoa = daoPessoa;
    }

    public List<Ocorrencia> getOcorrencias() {
        return ocorrencias;
    }

    public void setOcorrencias(List<Ocorrencia> ocorrencias) {
        this.ocorrencias = ocorrencias;
    }

    public Ocorrencia getOcorrencia() {
        return ocorrencia;
    }

    public void setOcorrencia(Ocorrencia ocorrencia) {
        this.ocorrencia = ocorrencia;
    }

    public void setPessoas(List<Pessoa> pessoas) {
        this.pessoas = pessoas;
    }
}
