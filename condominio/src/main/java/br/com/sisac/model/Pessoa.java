/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sisac.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "tb_pessoa")
@SequenceGenerator(name = "sq_pessoa", sequenceName = "sq_pessoa")
public class Pessoa implements Serializable {
	private static final long serialVersionUID = 5908427021537898784L;
	public static String TIPO_PESSOA_SINDICO = "SINDICO";
	public static String TIPO_PESSOA_FUNCIONARIO = "FUNCIONÁRIO";
    public static final String TIPO_PESSOA_CONDOMINO = "CONDÔMINO";
    public static final String TIPO_PESSOA_VISITANTE = "VISITANTE";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sq_pessoa")
    @Column(name = "id", unique = true, nullable = false)
    private long id;
    
    @Column(nullable = false)
    private String nome;
    
    
    private String sobrenome;
    
    @Column(name = "email")
    private String email;
    
    @Column(name = "imagem")
    private String imagem;

    
    private String rg;
    
    private String cpf;
    
    private String bloco;
    
    private long apartamento;
    
    @Column(name = "tipo_pessoa")
    private String tipoPessoa;
    
    @Column(name = "telefone_residencial")
    private String telefoneResidencial;
    
    @Column(name = "telefone_comercial")
    private String telefoneComercial;
    
    @OneToMany(mappedBy="pessoa")
    private List<Reserva> reservas;
    
    @OneToMany(mappedBy="pessoa")
    private List<Ocorrencia> ocorrencias;
    
     @OneToMany(mappedBy="pessoa")
    private List<Edital> editais;
    
    @Column(name = "quantidade_visita")
    private long quantidadeVisita;
    
    private String senha;
    
    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getBloco() {
        return bloco;
    }

    public void setBloco(String bloco) {
        this.bloco = bloco;
    }

    public long getApartamento() {
        return apartamento;
    }

    public void setApartamento(long apartamento) {
        this.apartamento = apartamento;
    }

    public String getTipoPessoa() {
        return tipoPessoa;
    }

    public void setTipoPessoa(String tipoPessoa) {
        this.tipoPessoa = tipoPessoa;
    }

    public String getTelefoneResidencial() {
        return telefoneResidencial;
    }

    public void setTelefoneResidencial(String telefoneResidencial) {
        this.telefoneResidencial = telefoneResidencial;
    }

    public String getTelefoneComercial() {
        return telefoneComercial;
    }

    public void setTelefoneComercial(String telefoneComercial) {
        this.telefoneComercial = telefoneComercial;
    }

    public List<Reserva> getReservas() {
        return reservas;
    }

    public void setReservas(List<Reserva> reservas) {
        this.reservas = reservas;
    }

    public List<Ocorrencia> getOcorrencias() {
        return ocorrencias;
    }

    public void setOcorrencias(List<Ocorrencia> ocorrencias) {
        this.ocorrencias = ocorrencias;
    }
    
    public void addReserva(Reserva reserva) {
        this.reservas.add(reserva);
    }

    public long getQuantidadeVisita() {
        return quantidadeVisita;
    }

    public void setQuantidadeVisita(long quantidadeVisita) {
        this.quantidadeVisita = quantidadeVisita;
    }

    public List<Edital> getEditais() {
        return editais;
    }

    public void setEditais(List<Edital> editais) {
        this.editais = editais;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
    
    public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getImagem() {
		return imagem;
	}

	public void setImagem(String imagem) {
		this.imagem = imagem;
	}
	
}