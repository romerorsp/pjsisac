package br.com.sisac.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.sisac.model.Ocorrencia;

@Repository
@Transactional(readOnly = true)
@SuppressWarnings("unchecked")
public class DAOOcorrencia {

    private @Autowired SessionFactory sessionFactory;
    
    @Transactional(readOnly = false)
    public void atualizar(Ocorrencia ocorrencia) {
        getSessionFactory().getCurrentSession().saveOrUpdate(ocorrencia);
    }

    @Transactional(readOnly = false)
    public void excluir(Ocorrencia ocorrencia) {
        getSessionFactory().getCurrentSession().delete(ocorrencia);
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<Ocorrencia> listar() {
        List<Ocorrencia> ocorrencias;
            ocorrencias = getSessionFactory().getCurrentSession().createQuery("FROM Ocorrencia r ORDER BY r.dataOcorrencia DESC")
                    .list();
        return ocorrencias;
    }

    public Ocorrencia consultarPorId(Ocorrencia ocorrencia) {
        String hql = "FROM br.sisac.model.Ocorrencia r WHERE r.id= :id";
        Query query = getSessionFactory().getCurrentSession().createQuery(hql);
        query.setLong("id", ocorrencia.getId());
        ocorrencia = (Ocorrencia) query.uniqueResult();
        return ocorrencia;
    }
    
    public List<Ocorrencia> consultarPorFiltro(Ocorrencia ocorrencia) {
        String hql = "FROM br.sisac.model.Ocorrencia r WHERE r.pessoa.id= :id";
        Query query = getSessionFactory().getCurrentSession().createQuery(hql);
        query.setLong("id", ocorrencia.getPessoa().getId());
        List<Ocorrencia> ocorrencias = query.list();
        return ocorrencias;
    }
}
