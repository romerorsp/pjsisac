package br.com.sisac.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.sisac.model.Pessoa;

@Service
@Transactional(readOnly = true)
@SuppressWarnings("unchecked")
public class DAOPessoa {

    private SessionFactory sessionFactory;
    public static final String FILTRO_NOME = "FILTRO_NOME";

    @Transactional(readOnly = false)
    public void atualizar(Pessoa pessoa) {
        getSessionFactory().getCurrentSession().saveOrUpdate(pessoa);
    }

    @Transactional(readOnly = false)
    public void excluir(Pessoa pessoa) {
        getSessionFactory().getCurrentSession().delete(pessoa);
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<Pessoa> listar() {
        List<Pessoa> pessoas = getSessionFactory().getCurrentSession().createQuery("FROM br.sisac.model.Pessoa p ORDER BY p.nome ASC")
                .list();
        return pessoas;
    }

    public List<Pessoa> listar(boolean visitante) {
        List<Pessoa> pessoas;

        if (visitante == false) {
            pessoas = getSessionFactory().getCurrentSession().createQuery("FROM Pessoa m WHERE m.tipoPessoa = 'FUNCIONARIO' OR m.tipoPessoa = 'CONDOMINO' OR m.tipoPessoa = 'SINDICO' ORDER BY m.nome ASC")
                    .list();
        } else {
            pessoas = getSessionFactory().getCurrentSession().createQuery("FROM Pessoa m WHERE m.tipoPessoa = 'VISITANTE' ORDER BY m.nome ASC")
                    .list();

        }
        return pessoas;
    }
    
    public Pessoa consultarPorLogin(Pessoa pessoa) {
        String hql = "FROM br.sisac.model.Pessoa p WHERE p.nome = :nome and p.senha = :senha";
        Query query = getSessionFactory().getCurrentSession().createQuery(hql);
        query.setString("nome", pessoa.getNome());
        query.setString("senha", pessoa.getSenha());
        pessoa = (Pessoa) query.uniqueResult();
        Hibernate.initialize(pessoa.getReservas());
        return pessoa;
    }

    public Pessoa consultarPorId(Pessoa pessoa) {
        String hql = "FROM br.sisac.model.Pessoa p WHERE p.id= :id";
        Query query = getSessionFactory().getCurrentSession().createQuery(hql);
        query.setLong("id", pessoa.getId());
        pessoa = (Pessoa) query.uniqueResult();
        //Hibernate.initialize(pessoa.getReservas());
        return pessoa;
    }

    public List<Pessoa> consultarPorFiltro(Pessoa pessoa) {
        StringBuffer sql = new StringBuffer("SELECT NOME,SOBRENOME,RG,CPF,BLOCO,APARTAMENTO,TIPO_PESSOA,TELEFONE_COMERCIAL,TELEFONE_RESIDENCIAL,ID FROM TB_PESSOA WHERE TRUE = TRUE");
        List<Pessoa> pessoas = new ArrayList<Pessoa>();
        if (pessoa.getTipoPessoa() != null && !"".equals(pessoa.getTipoPessoa())) {
            sql.append(" AND TIPO_PESSOA = '" + pessoa.getTipoPessoa() + "'");
        }
        if (pessoa.getNome() != null && !"".equals(pessoa.getNome())) {
            sql.append(" AND NOME LIKE '%" + pessoa.getNome() + "%'");
        }
        if (pessoa.getRg() != null && !"".equals(pessoa.getRg())) {
            sql.append(" AND RG LIKE '%" + pessoa.getRg() + "%'");
        }
        if (pessoa.getCpf() != null && !"".equals(pessoa.getCpf())) {
            sql.append(" AND CPF LIKE '%" + pessoa.getCpf() + "%'");
        }
        if (pessoa.getBloco() != null && !"".equals(pessoa.getBloco())) {
            sql.append(" AND BLOCO = '" + pessoa.getBloco() + "'");
        }
        if (pessoa.getApartamento() != 0L) {
            sql.append(" AND APARTAMENTO = '" + pessoa.getApartamento() + "'");
        }
        
        System.out.println("SQL " + sql.toString());
        
        SQLQuery query = getSessionFactory().getCurrentSession().createSQLQuery(sql.toString());
        query.addScalar("NOME", org.hibernate.type.StandardBasicTypes.STRING).
                addScalar("SOBRENOME", org.hibernate.type.StandardBasicTypes.STRING).
                addScalar("RG", org.hibernate.type.StandardBasicTypes.STRING).
                addScalar("CPF", org.hibernate.type.StandardBasicTypes.STRING).
                addScalar("BLOCO", org.hibernate.type.StandardBasicTypes.STRING).
                addScalar("APARTAMENTO", org.hibernate.type.StandardBasicTypes.LONG).
                addScalar("TIPO_PESSOA", org.hibernate.type.StandardBasicTypes.STRING).
                addScalar("TELEFONE_COMERCIAL", org.hibernate.type.StandardBasicTypes.STRING).
                addScalar("TELEFONE_RESIDENCIAL", org.hibernate.type.StandardBasicTypes.STRING).
                addScalar("ID", org.hibernate.type.StandardBasicTypes.LONG);
        List<Object[]> result = query.list();
        
        if (result != null && !result.isEmpty()) {
            for (Object[] row : result) {
                Pessoa a = new Pessoa();
                a.setNome(row[0].toString());
                a.setSobrenome(row[1].toString());
                a.setRg(row[2].toString());
                a.setCpf(row[3].toString());
                a.setBloco(row[4].toString());
                a.setApartamento((Long) row[5]);
                a.setTipoPessoa(row[6].toString());
                a.setTelefoneComercial(row[7].toString());
                a.setTelefoneResidencial(row[8].toString());
                a.setId((Long) row[9]);
                pessoas.add(a);
            }
        }
        return pessoas;
    }
}
