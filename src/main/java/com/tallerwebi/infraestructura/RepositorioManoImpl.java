package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Mano;
import com.tallerwebi.dominio.RepositorioMano;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Repository
public class RepositorioManoImpl implements RepositorioMano {

    private SessionFactory sessionFactory;
    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    public RepositorioManoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Transactional
    @Override
    public void guardar(Mano mano) {
        System.out.println("Guardando Mano");
        System.out.println(mano);
        sessionFactory.getCurrentSession().saveOrUpdate(mano);
    }

    @Transactional
    @Override
    public void merge(Mano mano) {
        entityManager.merge(mano);
    }

    @Transactional
    @Override
    public Mano obtenerUltimaMano(Long idPartida) {
        System.out.println("Buscando mano con partida ID: " + idPartida);
        return (Mano) sessionFactory.getCurrentSession()
                .createCriteria(Mano.class)
                .add(Restrictions.eq("confirmacionTerminada", false))
                .add(Restrictions.eq("partida.id", idPartida))
                .addOrder(Order.desc("id"))
                .setMaxResults(1)
                .uniqueResult();
    }


    @Transactional
    @Override
    public Mano obtenerManoPorId(Long id) {
        return (Mano) sessionFactory.getCurrentSession()
                .createCriteria(Mano.class)
                .add(Restrictions.eq("id", id))
                .uniqueResult();
    }
}