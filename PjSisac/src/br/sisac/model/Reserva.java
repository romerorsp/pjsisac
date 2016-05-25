package br.sisac.model;

import java.util.Calendar;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "tb_reserva")
@SequenceGenerator(name = "sq_reserva", sequenceName = "sq_reserva")
public class Reserva {
    
    public Reserva() {
        this.pessoa = new Pessoa();
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sq_reserva")
    @Column(name = "id", unique = true, nullable = false)
    private long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "data_uso")
    private Date dataUso;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "data_reserva")
    private Calendar dataReserva;
    
    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="id_pessoa")
    private Pessoa pessoa;
    
    private String tipo;

    public Date getDataUso() {
        return dataUso;
    }

    public void setDataUso(Date dataUso) {
        this.dataUso = dataUso;
    }

    public Calendar getDataReserva() {
        return dataReserva;
    }

    public void setDataReserva(Calendar dataReserva) {
        this.dataReserva = dataReserva;
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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}