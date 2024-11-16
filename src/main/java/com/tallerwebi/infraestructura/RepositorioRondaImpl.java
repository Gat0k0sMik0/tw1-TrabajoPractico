package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.*;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public class RepositorioRondaImpl implements RepositorioRonda2 {
    private SessionFactory sessionFactory;

    @Autowired
    public RepositorioRondaImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void guardar(Ronda2 ronda) {
        sessionFactory.getCurrentSession().saveOrUpdate(ronda);
    }

}
