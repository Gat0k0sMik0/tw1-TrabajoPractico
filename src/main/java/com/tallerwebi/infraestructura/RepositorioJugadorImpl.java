package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Jugador;
import com.tallerwebi.dominio.RepositorioJugador;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public class RepositorioJugadorImpl implements RepositorioJugador {
    private SessionFactory sessionFactory;

    @Autowired
    public RepositorioJugadorImpl (SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    @Transactional
    public void guardar(Jugador jugador) {
        sessionFactory.getCurrentSession().save(jugador);
    }

    @Override
    @Transactional
    public void modificar(Jugador jugador) {
        sessionFactory.getCurrentSession().update(jugador);
    }

    @Override
    @Transactional
    public Jugador buscarPorId(Long id) {
        return (Jugador) sessionFactory.getCurrentSession()
                .createCriteria(Jugador.class)
                .add(Restrictions.eq("id", id))
                .uniqueResult();
    }

}
