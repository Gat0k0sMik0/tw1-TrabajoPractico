package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Carta;
import com.tallerwebi.dominio.Estadistica;
import com.tallerwebi.dominio.RepositorioEstadistica;
import org.hibernate.SessionFactory;
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
    public Estadistica obtenerEstadisticaDeJugador(Long idJugador) {
        return (Estadistica) sessionFactory.getCurrentSession()
                .createCriteria(Estadistica.class)
                .createAlias("jugador", "j")
                .add(Restrictions.eq("j.id", idJugador))
                .add(Restrictions.eq("juego", "Truco"))
                .uniqueResult();
    }

    @Transactional
    @Override
    public void guardarEstadistica(Estadistica e) {
        sessionFactory.getCurrentSession().save(e);
    }

    @Override
    public List<Estadistica> obtenerTodasLasEstadisticasDeUnJugador(Long idUsuario) {
        return (List<Estadistica>) sessionFactory.getCurrentSession()
                .createCriteria(Estadistica.class, "e")
                .createAlias("e.jugador", "j") // Alias para Jugador
                .createAlias("j.usuario", "u") // Alias para Usuario
                .add(Restrictions.eq("u.id", idUsuario))
                .list();
    }

}
