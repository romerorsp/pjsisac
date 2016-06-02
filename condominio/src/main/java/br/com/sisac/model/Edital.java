package br.com.sisac.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "tb_edital")
//@SequenceGenerator(name = "sq_edital", sequenceName = "sq_edital")
public class Edital implements Serializable {
    
	private static final long serialVersionUID = 531666476953754460L;

	public Edital() {
        this.pessoa = new Pessoa();
    }
    
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO, generator = "sq_edital")
    @Id
    @GenericGenerator(
        name = "table", 
        strategy = "enhanced-table", 
        parameters = {
            @org.hibernate.annotations.Parameter(
                name = "table_name", 
                value = "sequence_table"
            )
    })
    @GeneratedValue(generator = "table", strategy=GenerationType.TABLE)
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