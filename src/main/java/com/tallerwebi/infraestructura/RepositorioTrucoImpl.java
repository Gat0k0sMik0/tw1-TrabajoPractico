package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.*;
import org.hibernate.Criteria;
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
    public List<Partida> getPartidasDisponibles(Long idUsuario) {
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Partida.class);

        criteria.createAlias("j1", "jugador1"); // Alias para el jugador 1
        criteria.createAlias("jugador1.usuario", "usuario1"); // Alias para el usuario del jugador 1

        criteria.add(Restrictions.isNull("j2")); // Partidas donde no hay un segundo jugador
        criteria.add(Restrictions.eq("puedeEmpezar", false)); // Partidas que aún no pueden empezar
        criteria.add(Restrictions.ne("usuario1.id", idUsuario)); // Excluir al jugador actual

        return criteria.list();
}

    @Transactional
    @Override
    public List<Partida> buscarPartidasNoTerminadas(Long idUsuario) {
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Partida.class);

        // Crear alias para las relaciones
        criteria.createAlias("j1", "j1"); // Alias para jugador 1
        criteria.createAlias("j2", "j2"); // Alias para jugador 2
        criteria.createAlias("j1.usuario", "usuario1"); // Alias para usuario en jugador 1
        criteria.createAlias("j2.usuario", "usuario2"); // Alias para usuario en jugador 2

        // Condiciones
        criteria.add(Restrictions.isNotNull("j1")); // El jugador 1 no debe ser nulo
        criteria.add(Restrictions.isNotNull("j2")); // El jugador 2 no debe ser nulo
        criteria.add(Restrictions.isNull("ganador")); // La partida no está terminada (campo ganador es null)

        // El usuario debe ser jugador 1 o jugador 2
        criteria.add(Restrictions.or(
                Restrictions.eq("usuario1.id", idUsuario), // El usuario es jugador 1
                Restrictions.eq("usuario2.id", idUsuario)  // El usuario es jugador 2
        ));

        return criteria.list();
    }

    @Transactional
    @Override
    public List<Partida> getTodasLasPartidas() {
        return (List<Partida>) sessionFactory.getCurrentSession()
                .createCriteria(Partida.class)
                .list();
    }


}
