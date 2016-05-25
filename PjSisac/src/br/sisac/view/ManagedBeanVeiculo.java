package br.sisac.view;

import br.sisac.dao.DAOPessoa;
import br.sisac.dao.DAOVeiculo;
import br.sisac.model.Pessoa;
import br.sisac.model.Veiculo;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

public class ManagedBeanVeiculo {

    private DAOVeiculo daoVeiculo;
    private DAOPessoa daoPessoa;
    private List<Veiculo> veiculos;
    private List<Pessoa> pessoas;
    private Veiculo veiculo;

    @PostConstruct
    public void init() {
        veiculos = daoVeiculo.listar();
    }

    public DAOVeiculo getDaoVeiculo() {
        return daoVeiculo;
    }

    public void setDaoVeiculo(DAOVeiculo daoVeiculo) {
        this.daoVeiculo = daoVeiculo;
    }

    public List<Veiculo> getVeiculos() {
        return veiculos;
    }

    public void setVeiculos(List<Veiculo> veiculos) {
        this.veiculos = veiculos;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
    }

    public void atualizarPessoa() {
        for (Pessoa p : pessoas) {
            if (veiculo.getPessoa().getId() == p.getId()) {
                veiculo.setPessoa(p);
            }
        }
    }

    public boolean validar() {
        boolean valido = true;
        if ("".equals(veiculo.getAno()) || "".equals(veiculo.getMarca())
                || "".equals(veiculo.getModelo()) || "".equals(veiculo.getPlaca())) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Os campos com (*) devem ser preenchidos obrigatoriamente!", ""));
            valido = false;
        }
        return valido;
    }

    public List<SelectItem> getPessoas() {
        List<Pessoa> pessoas = daoPessoa.listar(false);
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
        Veiculo p = new Veiculo();

        switch (acao) {
            case ManagedBeanUtil.ACAO_IR_PARA_LISTAR:
                veiculos = daoVeiculo.listar();
                setVeiculo(new Veiculo());
                return "lista-veiculo.xhtml";
            case ManagedBeanUtil.ACAO_EXCLUIR:
                p.setId(id);
                p = daoVeiculo.consultarPorId(p);
                daoVeiculo.excluir(p);
                setVeiculo(new Veiculo());
                veiculos = daoVeiculo.listar();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Exclusão realizada com sucesso!", ""));
                return "lista-veiculo.xhtml";
            case ManagedBeanUtil.ACAO_IR_PARA_ALTERAR:
                p.setId(id);
                setVeiculo(daoVeiculo.consultarPorId(p));
                pessoas = daoPessoa.listar(false);
                return "cadastro-veiculo.xhtml";
            case ManagedBeanUtil.ACAO_IR_PARA_PESQUISAR:
                veiculo = new Veiculo();
                setVeiculo(veiculo);
                return "pesquisa-veiculo.xhtml";
            case ManagedBeanUtil.ACAO_PESQUISAR:
                veiculos = daoVeiculo.consultarPorPlaca(veiculo);
                return "lista-veiculo.xhtml";
            case ManagedBeanUtil.ACAO_IR_PARA_INCLUIR:
                setVeiculo(new Veiculo());
                pessoas = daoPessoa.listar(false);
                return "cadastro-veiculo.xhtml";
            case ManagedBeanUtil.ACAO_ATUALIZAR:
                if (!validar()) {
                    return "cadastro-veiculo.xhtml";
                }
                String mensagem = (veiculo.getId() != 0L)
                        ? "Alteração realizada com sucesso!"
                        : "Cadastrado com sucesso!";

                daoVeiculo.atualizar(veiculo);
                veiculos = daoVeiculo.listar();
                setVeiculo(new Veiculo());
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                        mensagem, ""));
                return "lista-veiculo.xhtml";
            default:
                return "lista-veiculo.xhtml";
        }
    }

    public DAOPessoa getDaoPessoa() {
        return daoPessoa;
    }

    public void setDaoPessoa(DAOPessoa daoPessoa) {
        this.daoPessoa = daoPessoa;
    }

}
