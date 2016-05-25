package br.com.sisac.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.sisac.model.Edital;

@Service
@Transactional(readOnly = true)
@SuppressWarnings("unchecked")
public class DAOEdital {

    private SessionFactory sessionFactory;
    
    @Transactional(readOnly = false)
    public void atualizar(Edital edital) {
        getSessionFactory().getCurrentSession().saveOrUpdate(edital);
    }

    @Transactional(readOnly = false)
    public void excluir(Edital edital) {
        getSessionFactory().getCurrentSession().delete(edital);
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<Edital> listar() {
        List<Edital> editals;
            editals = getSessionFactory().getCurrentSession().createQuery("FROM Edital e ORDER BY e.descricao ASC")
                    .list();
        return editals;
    }

    public Edital consultarPorId(Edital edital) {
        String hql = "FROM br.sisac.model.Edital e WHERE e.id= :id";
        Query query = getSessionFactory().getCurrentSession().createQuery(hql);
        query.setLong("id", edital.getId());
        edital = (Edital) query.uniqueResult();
        return edital;
    }
    
    public List<Edital> consultarPorFiltro(Edital edital) {
        String hql = "FROM br.sisac.model.Edital e WHERE e.pessoa.id= :id";
        Query query = getSessionFactory().getCurrentSession().createQuery(hql);
        query.setLong("id", edital.getPessoa().getId());
        List<Edital> editals = query.list();
        return editals;
    }
}
