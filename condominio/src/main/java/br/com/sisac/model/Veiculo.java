package br.com.sisac.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;


@Entity
@Table(name = "tb_veiculo")
//@SequenceGenerator(name = "sq_veiculo", sequenceName = "sq_veiculo")
public class Veiculo implements Serializable {
   
	private static final long serialVersionUID = -3619207234272928891L;

//	@Id
//    @GeneratedValue(strategy = GenerationType.AUTO, generator = "sq_veiculo")
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
    
    @OneToOne(cascade = CascadeType.ALL, optional = false, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name="id_pessoa")
    private Pessoa pessoa;
    
    private String marca;
    
    private String modelo;
    
    private String ano;
    
    private String placa;
    
    public Veiculo() {
        setPessoa(new Pessoa());
    }

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

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getAno() {
        return ano;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

 
}   