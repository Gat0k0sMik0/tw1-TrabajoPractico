package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Carta;
import com.tallerwebi.dominio.RepositorioCarta;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
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

    @Override
    public Carta buscarCartaPorId (Long id ) {
       final Session session = sessionFactory.getCurrentSession();
       return (Carta) session.get(Carta.class, id);
    }


}
