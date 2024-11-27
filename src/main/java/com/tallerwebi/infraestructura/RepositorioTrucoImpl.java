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
    public void guardarPartida(Partida truco) {
        System.out.println("Guardando partida. PTSJ1: " + truco.getPuntosJ1());
        System.out.println("Guardando partida. PTSJ2: " + truco.getPuntosJ2());
        sessionFactory.getCurrentSession().save(truco);
    }

    @Transactional
    @Override
    public Partida buscarPartidaPorId(Long id) {
        Session session = sessionFactory.getCurrentSession();
        Partida t = (Partida) session.createCriteria(Partida.class)
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
        return j;
    }



}
