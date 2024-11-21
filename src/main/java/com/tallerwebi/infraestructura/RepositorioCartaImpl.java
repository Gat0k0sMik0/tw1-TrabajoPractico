package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Carta;
import com.tallerwebi.dominio.RepositorioCarta;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@SuppressWarnings("unchecked")
@Repository
public class RepositorioCartaImpl implements RepositorioCarta {

    private SessionFactory sessionFactory;

    @Autowired
    public RepositorioCartaImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Transactional
    @Override
    public List<Carta> obtenerCartas() {
        return (List<Carta>) sessionFactory.getCurrentSession()
                .createCriteria(Carta.class)
                .list();
    }

    @Transactional
    @Override
    public Carta buscarCartaPorId (Long id ) {
       final Session session = sessionFactory.getCurrentSession();
       return (Carta) session.get(Carta.class, id);
    }

    @Transactional
    @Override
    public List<Carta> obtenerCartasDeJugadorPorId(Long idJugador) {
        Session session = sessionFactory.getCurrentSession();
        List<Carta> c = session.createCriteria(Carta.class)
                .add(Restrictions.eq("jugador.id", idJugador))
                .list();
        return c;
    }

    @Transactional
    @Override
    public void guardarCarta(Carta c) {
        sessionFactory.getCurrentSession().save(c);
    }

    @Transactional
    @Override
    public void guardarVariasCartas(List<Carta> c) {
        for (Carta carta : c) {
            sessionFactory.getCurrentSession().save(carta);
        }
    }


}
