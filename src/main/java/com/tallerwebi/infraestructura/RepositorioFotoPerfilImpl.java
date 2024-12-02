package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Carta;
import com.tallerwebi.dominio.FotoPerfil;
import com.tallerwebi.dominio.RepositorioFotoPerfil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public class RepositorioFotoPerfilImpl implements RepositorioFotoPerfil {
    private SessionFactory sessionFactory;

    @Autowired
    public RepositorioFotoPerfilImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Transactional
    @Override
    public List<FotoPerfil> obtenerFotos() {
        return (List<FotoPerfil>) sessionFactory.getCurrentSession()
                .createCriteria(FotoPerfil.class)
                .list();
    }

    @Transactional
    @Override
    public FotoPerfil buscarFotoPorId(Long id) {
        final Session session = sessionFactory.getCurrentSession();
        return (FotoPerfil) session.get(FotoPerfil.class, id);
    }

    @Transactional
    @Override
    public void guardarFotoPerfil(FotoPerfil foto) {
        sessionFactory.getCurrentSession().save(foto);
    }

}
