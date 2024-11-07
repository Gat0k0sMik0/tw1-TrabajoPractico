package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.RepositorioTruco;
import com.tallerwebi.dominio.Truco;
import com.tallerwebi.dominio.Usuario;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@SuppressWarnings("unchecked")
@Repository
public class RepositorioTrucoImpl implements RepositorioTruco {
    private SessionFactory sessionFactory;

    @Autowired
    public RepositorioTrucoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Transactional
    @Override
    public void guardarPartida(Truco truco) {
        sessionFactory.getCurrentSession().save(truco);
    }

    @Override
    public Truco buscarPartidaPorId(Long id) {
        Session session = sessionFactory.getCurrentSession();
        return (Truco) session.createCriteria(Truco.class)
                .add(Restrictions.eq("id", id))
                .uniqueResult();
    }
}
