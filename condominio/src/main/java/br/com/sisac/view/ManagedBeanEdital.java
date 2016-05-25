package br.com.sisac.view;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import br.com.sisac.dao.DAOEdital;
import br.com.sisac.dao.DAOPessoa;
import br.com.sisac.model.Edital;
import br.com.sisac.model.Pessoa;

public class ManagedBeanEdital {
    private DAOEdital daoEdital;
    private DAOPessoa daoPessoa;
    private List<Edital> editals;
    private List<Pessoa> pessoas;
    private Edital edital;

    @PostConstruct
    public void init() {
        editals = daoEdital.listar();
        pessoas = daoPessoa.listar(false);
    }

    public void atualizarPessoa() {
        for (Pessoa p : pessoas) {
            if (edital.getPessoa().getId() == p.getId()) {
                edital.setPessoa(p);
            }
        }
    }

    public boolean validar() {
        boolean valido = true;
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
        Edital r = new Edital();

        switch (acao) {
            case ManagedBeanUtil.ACAO_IR_PARA_LISTAR:
                editals = daoEdital.listar();
                setEdital(new Edital());
                return "lista-edital.xhtml";
            case ManagedBeanUtil.ACAO_EXCLUIR:
                r.setId(id);
                r = daoEdital.consultarPorId(r);
                daoEdital.excluir(r);
                setEdital(new Edital());
                editals = daoEdital.listar();
                return "lista-edital.xhtml";
            case ManagedBeanUtil.ACAO_IR_PARA_ALTERAR:
                r.setId(id);
                setEdital(daoEdital.consultarPorId(r));
                pessoas = daoPessoa.listar(false);
                return "cadastro-edital.xhtml";
            case ManagedBeanUtil.ACAO_IR_PARA_PESQUISAR:
                edital = new Edital();
                setEdital(edital);
                return "pesquisa-edital.xhtml";
            case ManagedBeanUtil.ACAO_PESQUISAR:
                editals = daoEdital.consultarPorFiltro(edital);
                return "lista-edital.xhtml";
            case ManagedBeanUtil.ACAO_IR_PARA_INCLUIR:
                setEdital(new Edital());
                pessoas = daoPessoa.listar(false);
                return "cadastro-edital.xhtml";
            case ManagedBeanUtil.ACAO_ATUALIZAR:
                if (!validar()) {
                    return "cadastro-edital.xhtml";
                }

                String mensagem = (edital.getId() != 0)
                        ? "Alteração realizada com sucesso!"
                        : "Cadastrado com sucesso!";

                daoEdital.atualizar(edital);
                editals = daoEdital.listar();
                setEdital(new Edital());
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                        mensagem, ""));
                return "lista-edital.xhtml";
            default:
                return "lista-edital.xhtml";
        }
    }

    public DAOPessoa getDaoPessoa() {
        return daoPessoa;
    }

    public void setDaoPessoa(DAOPessoa daoPessoa) {
        this.daoPessoa = daoPessoa;
    }

    public List<Edital> getEditals() {
        return editals;
    }

    public void setEditals(List<Edital> editals) {
        this.editals = editals;
    }

    public Edital getEdital() {
        return edital;
    }

    public void setEdital(Edital edital) {
        this.edital = edital;
    }

    public void setPessoas(List<Pessoa> pessoas) {
        this.pessoas = pessoas;
    }

    public DAOEdital getDaoEdital() {
        return daoEdital;
    }

    public void setDaoEdital(DAOEdital daoEdital) {
        this.daoEdital = daoEdital;
    }
}
