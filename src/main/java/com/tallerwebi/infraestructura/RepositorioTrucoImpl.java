package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class RepositorioTrucoImpl implements RepositorioTruco {

    private SessionFactory sessionFactory;

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    public RepositorioTrucoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Transactional
    @Override
    public void guardarPartida(Partida truco) {
        System.out.println("Guardo: ");
        System.out.println(truco);
        sessionFactory.getCurrentSession().save(truco);
    }

    @Transactional
    @Override
    public void merge(Partida truco) {
        entityManager.merge(truco);
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
        sessionFactory.getCurrentSession().save(jugador);
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

    @Transactional
    @Override
    public List<Partida> getPartidasDisponibles() {
        return (List<Partida>) sessionFactory.getCurrentSession()
                .createCriteria(Partida.class)
                .add(Restrictions.isNull("j2"))
                .add(Restrictions.eq("puedeEmpezar", false))
                .list();
    }

    @Transactional
    @Override
    public List<Partida> getTodasLasPartidas() {
        return (List<Partida>) sessionFactory.getCurrentSession()
                .createCriteria(Partida.class)
                .list();
    }


}
