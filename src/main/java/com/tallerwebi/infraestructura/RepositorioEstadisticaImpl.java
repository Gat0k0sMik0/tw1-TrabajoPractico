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
        sessionFactory.getCurrentSession().save(e);
    }

    @Transactional
    @Override
    public void actualizarEstadistica(Estadistica e) {
        sessionFactory.getCurrentSession().update(e);
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

    @Transactional
    @Override
    public List<Estadistica> obtenerTodasLasEstadisticas() {
        return sessionFactory.getCurrentSession()
                .createCriteria(Estadistica.class, "e")
                .createAlias("e.usuario", "u") // Alias para facilitar consultas con relaciones
                .addOrder(Order.asc("e.id")) // Ordenar opcionalmente por el ID u otro atributo
                .list();
    }

    @Transactional
    @Override
    public List<Estadistica> obtenerTop5JugadoresPorVictorias() {
        return sessionFactory.getCurrentSession()
                .createCriteria(Estadistica.class, "e")
                .createAlias("e.usuario", "u") // Alias para relacionar usuario
                .addOrder(Order.desc("e.ganadas")) // Ordenar por victorias (ganadas) en orden descendente
                .setMaxResults(5) // Limitar a los 5 primeros
                .list();
    }
}
