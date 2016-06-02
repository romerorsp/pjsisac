package br.com.sisac.view;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import br.com.sisac.dao.DAOPessoa;
import br.com.sisac.model.Pessoa;


@Named(value="managedBeanFuncionarioCondomino")
@Scope(value="session")
public class ManagedBeanFuncionarioCondomino {

	private @Autowired DAOPessoa daoPessoa;
	private List<Pessoa> pessoas;
	private Pessoa pessoa;

	@PostConstruct
	public void init() {
		pessoas = daoPessoa.listar(false);
		pessoa = new Pessoa();
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

	public boolean validar() {
		boolean valido = true;
		if ("".equals(pessoa.getNome()) || "".equals(pessoa.getSobrenome()) || "".equals(pessoa.getRg())
				|| "".equals(pessoa.getCpf())) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Os campos com (*) devem ser preenchidos obrigatoriamente!", ""));
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
		case ManagedBeanUtil.ACAO_IR_PARA_LISTAR:
			pessoas = daoPessoa.listar(false);
			setPessoa(new Pessoa());
			return "lista-funcionario-condomino.xhtml";
		case ManagedBeanUtil.ACAO_EXCLUIR:
			p.setId(id);
			p = daoPessoa.consultarPorId(p);
			daoPessoa.excluir(p);
			setPessoa(new Pessoa());
			pessoas = daoPessoa.listar(false);
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Exclusão realizada com sucesso!", ""));
			return "lista-funcionario-condomino.xhtml";
		case ManagedBeanUtil.ACAO_IR_PARA_ALTERAR:
			p.setId(id);
			setPessoa(daoPessoa.consultarPorId(p));
			return "cadastro-funcionario-condomino.xhtml";
		case ManagedBeanUtil.ACAO_IR_PARA_PESQUISAR:
			pessoa = new Pessoa();
			setPessoa(pessoa);
			return "pesquisa-funcionario-condomino.xhtml";
		case ManagedBeanUtil.ACAO_PESQUISAR:
			pessoas = daoPessoa.consultarPorFiltro(pessoa);
			return "lista-funcionario-condomino.xhtml";
		case ManagedBeanUtil.ACAO_IR_PARA_INCLUIR:
			setPessoa(new Pessoa());
			return "cadastro-funcionario-condomino.xhtml";
		case ManagedBeanUtil.ACAO_ATUALIZAR:
			if (!validar()) {
				return "cadastro-funcionario-condomino.xhtml";
			}

			String mensagem = (pessoa.getId() != 0L) ? "Alteração realizada com sucesso!" : "Cadastrado com sucesso!";

			daoPessoa.atualizar(pessoa);

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, mensagem, ""));

			pessoas = daoPessoa.listar(false);
			setPessoa(new Pessoa());
			return "lista-funcionario-condomino.xhtml";
		default:
			return "lista-funcionario-condomino.xhtml";
		}
	}

	public String autenticar() {

		if ("".equals(pessoa.getNome()) || "".equals(pessoa.getSenha())) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Os campos com (*) devem ser preenchidos obrigatoriamente!", ""));
			return "login.xhtml";
		}

		try {
			Pessoa p = daoPessoa.consultarPorLogin(pessoa);
			if (p != null) {
				FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
						.put(ManagedBeanUtil.USUARIO_SESSAO, p);
				Object acessoEscrita = !Pessoa.TIPO_PESSOA_VISITANTE.equals(p.getTipoPessoa()) ? new Object() : null;
				FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("acessoEscrita",
						acessoEscrita);
				return "lista-funcionario-condomino.xhtml";
			} else {
				return "login.xhtml";
			}
		} catch (Exception ex) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Usuário não localizado.", ""));
			return "login.xhtml";
		}
	}

	public String desautenticar() {
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(ManagedBeanUtil.USUARIO_SESSAO,
				null);
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("acessoEscrita", null);
		pessoa = new Pessoa();
		return "login.xhtml";
	}
}
