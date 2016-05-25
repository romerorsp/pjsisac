package br.sisac.model;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "tb_edital")
@SequenceGenerator(name = "sq_edital", sequenceName = "sq_edital")
public class Edital implements Serializable {
    
    public Edital() {
        this.pessoa = new Pessoa();
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sq_edital")
    @Column(name = "id", unique = true, nullable = false)
    private long id;
    
    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="id_pessoa")
    private Pessoa pessoa;
    
     @Column(name = "tipo_anuncio")
    private String tipoAnuncio;
    private String descricao;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public String getTipoAnuncio() {
        return tipoAnuncio;
    }

    public void setTipoAnuncio(String tipoAnuncio) {
        this.tipoAnuncio = tipoAnuncio;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}