package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Carta;
import com.tallerwebi.dominio.RepositorioCarta;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RepositorioCartaImp implements RepositorioCarta {

    private SessionFactory sessionFactory;

    @Autowired
    public RepositorioCartaImp(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List obtenerCartas() {
        final Session session = sessionFactory.getCurrentSession();
        return session.createCriteria(Carta.class)
                .list();
    }

    @Override
    public Carta buscarCartaPorId (Long id ) {
       final Session session = sessionFactory.getCurrentSession();
       return (Carta) session.get(Carta.class, id);
    }


}
