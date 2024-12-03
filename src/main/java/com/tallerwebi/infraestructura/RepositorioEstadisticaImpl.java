package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Estadistica;
import com.tallerwebi.dominio.RepositorioEstadistica;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public class RepositorioEstadisticaImpl implements RepositorioEstadistica {

    private SessionFactory sessionFactory;

    @Autowired
    public RepositorioEstadisticaImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Transactional
    @Override
    public Estadistica obtenerEstadisticaDeJugador(Long idUsuario) {
        List<Estadistica> estadisticas = sessionFactory.getCurrentSession()
                .createCriteria(Estadistica.class, "e")
                .createAlias("e.usuario", "u")
                .add(Restrictions.eq("u.id", idUsuario))
                .add(Restrictions.eq("e.juego", "Truco"))
                .list();

        if (estadisticas.isEmpty()) {
            return null; // No se encontró ninguna estadística
        }

        // Devuelve la primera estadística encontrada o maneja el caso si hay más de una
        return estadisticas.get(0);
    }

    @Transactional
    @Override
    public void guardarEstadistica(Estadistica e) {
        System.out.println("Estadistica guardada: " + e);
        sessionFactory.getCurrentSession().save(e);
    }

    @Override
    public List<Estadistica> obtenerTodasLasEstadisticasDeUnJugador(Long idUsuario) {
        return (List<Estadistica>) sessionFactory.getCurrentSession()
                .createCriteria(Estadistica.class, "e")
                .createAlias("e.usuario", "u") // Alias para Usuario
                .add(Restrictions.eq("u.id", idUsuario)) // Filtrar por ID del usuario
                .list();
    }

    @Transactional
    @Override
    public List<Estadistica> obtenerTopJugadoresPorVictorias(int limite) {
        return sessionFactory.getCurrentSession()
                .createCriteria(Estadistica.class, "e")
                .createAlias("e.usuario", "u")
                .addOrder(Order.desc("e.ganadas")) // Ordenar por victorias (ganadas) en orden descendente
                .setMaxResults(limite) // Limitar el número de resultados
                .list();
    }
}
