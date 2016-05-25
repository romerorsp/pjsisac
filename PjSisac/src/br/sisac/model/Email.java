package br.sisac.model;

import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "tb_email")
@SequenceGenerator(name = "sq_email", sequenceName = "sq_email")


public class Email {
	private Pessoa pessoa;
	

	public Email(Pessoa pessoa) {
        this.pessoa = pessoa;
    }


	public Pessoa getPessoa() {
		return pessoa;
	}


	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}
    
	
}


