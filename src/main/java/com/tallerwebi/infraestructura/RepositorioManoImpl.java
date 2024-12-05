package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Mano;
import com.tallerwebi.dominio.Partida;
import com.tallerwebi.dominio.RepositorioMano;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

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
    public void eliminarCartasDeManosPorUsuario(Long usuarioId) {
        Session session = sessionFactory.getCurrentSession();

        // 1. Obtener las partidas donde el usuario asociado a jugador 1 o jugador 2 coincida con el ID dado
        List<Partida> partidas = session.createQuery(
                        "FROM Partida p WHERE p.j1.usuario.id = :usuarioId OR p.j2.usuario.id = :usuarioId", Partida.class)
                .setParameter("usuarioId", usuarioId)
                .getResultList();

        if (partidas.isEmpty()) {
            System.out.println("No se encontraron partidas para el usuario con ID: " + usuarioId);
            return;
        }

        // 2. Obtener las manos asociadas a esas partidas
        List<Mano> manos = session.createQuery(
                        "FROM Mano m WHERE m.partida IN :partidas", Mano.class)
                .setParameter("partidas", partidas)
                .getResultList();

        if (manos.isEmpty()) {
            System.out.println("No se encontraron manos asociadas a las partidas.");
            return;
        }

        // 3. Limpiar las cartas asociadas a cada mano
        for (Mano mano : manos) {
            mano.getCartasJ1().clear();
            mano.getCartasJ2().clear();
            mano.getCartasTiradasJ1().clear();
            mano.getCartasTiradasJ2().clear();

            // Actualizar la entidad Mano
            session.merge(mano);
        }

        System.out.println("Cartas eliminadas de las manos asociadas a las partidas del usuario con ID: " + usuarioId);
    }



    @Transactional
    @Override
    public void guardar(Mano mano) {
        System.out.println("Guardando Mano: ");
        System.out.println(mano);
        sessionFactory.getCurrentSession().save(mano);
    }

    @Transactional
    @Override
    public void merge(Mano mano) {
        System.out.println("Merge de mano: ");
        System.out.println(mano);
        entityManager.merge(mano);
    }

    @Transactional
    @Override
    public Mano obtenerUltimaMano(Long idPartida) {
        return (Mano) sessionFactory.getCurrentSession()
                .createCriteria(Mano.class)
                .add(Restrictions.eq("confirmacionTerminada", false))
                .add(Restrictions.eq("partida.id", idPartida))
                .addOrder(Order.desc("id"))
                .setMaxResults(1)
                .uniqueResult();
    }

    @Transactional
    public List<Mano> getAllManos() {
        return sessionFactory.getCurrentSession()
                .createCriteria(Mano.class)
                .list();
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
