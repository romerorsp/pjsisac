package br.sisac.dao;

import br.sisac.model.Veiculo;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@SuppressWarnings("unchecked")
public class DAOVeiculo {

    private SessionFactory sessionFactory;
    
    @Transactional(readOnly = false)
    public void atualizar(Veiculo veiculo) {
        getSessionFactory().getCurrentSession().saveOrUpdate(veiculo);
    }

    @Transactional(readOnly = false)
    public void excluir(Veiculo veiculo) {
        getSessionFactory().getCurrentSession().delete(veiculo);
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<Veiculo> listar() {
        List<Veiculo> veiculos;
            veiculos = getSessionFactory().getCurrentSession().createQuery("FROM Veiculo v ORDER BY v.marca ASC")
                    .list();
        return veiculos;
    }

    public Veiculo consultarPorId(Veiculo veiculo) {
        String hql = "FROM br.sisac.model.Veiculo v WHERE v.id= :id";
        Query query = getSessionFactory().getCurrentSession().createQuery(hql);
        query.setLong("id", veiculo.getId());
        veiculo = (Veiculo) query.uniqueResult();
        return veiculo;
    }
    
    public List<Veiculo> consultarPorPlaca(Veiculo veiculo) {
        String hql = "FROM br.sisac.model.Veiculo v WHERE v.placa like :placa";
        Query query = getSessionFactory().getCurrentSession().createQuery(hql);
        query.setParameter("placa", "%" + veiculo.getPlaca() + "%");
        List<Veiculo> veiculos = query.list();
        return veiculos;
    }
}
