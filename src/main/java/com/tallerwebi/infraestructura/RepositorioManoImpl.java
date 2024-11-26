package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Mano2;
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
    public void guardar(Mano2 mano) {
        sessionFactory.getCurrentSession().saveOrUpdate(mano);
        System.out.println("Ya guardé la mano");
    }

    @Transactional
    @Override
    public Mano2 obtenerManoPorId(Long id) {
        return (Mano2) sessionFactory.getCurrentSession()
                .createCriteria(Mano2.class)
                .add(Restrictions.eq("id", id))
                .uniqueResult();
    }
}
