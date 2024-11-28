package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.RepositorioSalaDeEspera;
import com.tallerwebi.dominio.SalaDeEspera;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public class RepositorioSalaDeEsperaImpl implements RepositorioSalaDeEspera {
    private SessionFactory sessionFactory;

    @Autowired
    public RepositorioSalaDeEsperaImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    @Transactional
    public void guardar(SalaDeEspera salaDeEspera) {
        sessionFactory.getCurrentSession().save(salaDeEspera);
    }

    @Override
    @Transactional
    public List<SalaDeEspera> obtenerSalas() {
        Session session = sessionFactory.getCurrentSession();
        Query<SalaDeEspera> query = session.createQuery("from SalaDeEspera where partidaIniciada = false", SalaDeEspera.class);
        return query.getResultList();
    }

    @Override
    @Transactional
    public SalaDeEspera obtenerSalaPorId(Long id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(SalaDeEspera.class, id);
    }
}
