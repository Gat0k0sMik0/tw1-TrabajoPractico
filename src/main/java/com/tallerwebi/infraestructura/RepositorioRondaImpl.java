package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.*;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

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

    @Override
    public List<Ronda2> obtenerRondasDeUnaMano(Long manoId) {
        return (List<Ronda2>) sessionFactory.getCurrentSession()
                .createCriteria(Ronda2.class)
                .add(Restrictions.eq("mano_id", manoId))
                .list();
    }

}
