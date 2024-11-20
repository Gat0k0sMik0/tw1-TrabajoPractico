package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public class RepositorioTrucoImpl implements RepositorioTruco {

    private SessionFactory sessionFactory;

    @Autowired
    public RepositorioTrucoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Transactional
    @Override
    public void guardarPartida(Truco2 truco) {
        sessionFactory.getCurrentSession().save(truco);
    }

    @Transactional
    @Override
    public Truco2 buscarPartidaPorId(Long id) {
        Session session = sessionFactory.getCurrentSession();
        return (Truco2) session.createCriteria(Truco2.class)
                .add(Restrictions.eq("id", id))
                .uniqueResult();
    }

    @Transactional
    @Override
    public void guardarJugador(Jugador jugador) {
        sessionFactory.getCurrentSession().save(jugador);
    }

    @Transactional
    @Override
    public Jugador obtenerJugadorPorID(Long id) {
        Session session = sessionFactory.getCurrentSession();
        return (Jugador) session.createCriteria(Jugador.class)
                .add(Restrictions.eq("id", id))
                .uniqueResult();

    }



}
