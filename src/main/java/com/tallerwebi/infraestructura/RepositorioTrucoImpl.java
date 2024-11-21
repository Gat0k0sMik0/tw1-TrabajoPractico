package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.*;
import org.hibernate.Hibernate;
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
        Truco2 t = (Truco2) session.createCriteria(Truco2.class)
                .add(Restrictions.eq("id", id))
                .uniqueResult();
        System.out.println("Encontre: " + t);
        return t;
    }

    @Transactional
    @Override
    public void guardarJugador(Jugador jugador) {
        Session session = sessionFactory.getCurrentSession();
        session.save(jugador);
        System.out.println("Jugador guardado en repo con ID: " + jugador.getId());
    }

    @Transactional
    @Override
    public Jugador obtenerJugadorPorID(Long id) {
        Session session = sessionFactory.getCurrentSession();
        Jugador j = (Jugador) session.createCriteria(Jugador.class)
                .add(Restrictions.eq("id", id))
                .uniqueResult();
        Hibernate.initialize(j.getCartas());
        return j;
    }



}
