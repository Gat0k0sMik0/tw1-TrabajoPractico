package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Mano;
import com.tallerwebi.dominio.RepositorioMano;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public class RepositorioManoImpl implements RepositorioMano {

    private SessionFactory sessionFactory;

    @Autowired
    public RepositorioManoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Transactional
    @Override
    public void guardar(Mano mano) {
        sessionFactory.getCurrentSession().saveOrUpdate(mano);
        System.out.println("Ya guardé la mano");
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
