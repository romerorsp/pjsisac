package br.sisac.dao;

import br.sisac.model.Reserva;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@SuppressWarnings("unchecked")
public class DAOReserva {

    private SessionFactory sessionFactory;
    
    @Transactional(readOnly = false)
    public void atualizar(Reserva reserva) {
        getSessionFactory().getCurrentSession().saveOrUpdate(reserva);
    }

    @Transactional(readOnly = false)
    public void excluir(Reserva reserva) {
        getSessionFactory().getCurrentSession().delete(reserva);
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<Reserva> listar() {
        List<Reserva> reservas;
            reservas = getSessionFactory().getCurrentSession().createQuery("FROM Reserva r ORDER BY r.dataUso ASC")
                    .list();
        return reservas;
    }

    public Reserva consultarPorId(Reserva reserva) {
        String hql = "FROM br.sisac.model.Reserva r WHERE r.id= :id";
        Query query = getSessionFactory().getCurrentSession().createQuery(hql);
        query.setLong("id", reserva.getId());
        reserva = (Reserva) query.uniqueResult();
        return reserva;
    }
    
    public List<Reserva> consultarPorFiltro(Reserva reserva) {
        String hql = "FROM br.sisac.model.Reserva r WHERE r.pessoa.id= :id";
        Query query = getSessionFactory().getCurrentSession().createQuery(hql);
        query.setLong("id", reserva.getPessoa().getId());
        List<Reserva> reservas = query.list();
        return reservas;
    }
}
