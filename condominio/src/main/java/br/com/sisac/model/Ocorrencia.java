package br.com.sisac.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "tb_ocorrencia")
//@SequenceGenerator(name = "sq_ocorrencia", sequenceName = "sq_ocorrencia")
public class Ocorrencia implements Serializable {
    
	private static final long serialVersionUID = -4918577397662795754L;

	public Ocorrencia() {
        this.pessoa = new Pessoa();
    }
    
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO, generator = "sq_ocorrencia")
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

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "data_ocorrencia")
    private Date dataOcorrencia;
    
    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="id_pessoa")
    private Pessoa pessoa;
    
    private String descricao;

    public Date getDataOcorrencia() {
        return dataOcorrencia;
    }

    public void setDataOcorrencia(Date dataOcorrencia) {
        this.dataOcorrencia = dataOcorrencia;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}