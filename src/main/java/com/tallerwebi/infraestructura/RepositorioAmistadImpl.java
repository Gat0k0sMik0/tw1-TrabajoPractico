package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Amistad;
import com.tallerwebi.dominio.RepositorioAmistad;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.dominio.excepcion.AmistadesException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@SuppressWarnings("unchecked")
@Repository
public class RepositorioAmistadImpl implements RepositorioAmistad {

    private SessionFactory sessionFactory;

    @Autowired
    public RepositorioAmistadImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Transactional
    @Override
    public void agregarAmigo(Amistad amistad) throws AmistadesException {
        Session session = sessionFactory.getCurrentSession();
        Amistad siLaRelacionExiste = saberSiTieneEseAmigo(amistad.getUsuario().getId(), amistad.getAmigo().getId(), session);
        if (siLaRelacionExiste != null) {
            throw new AmistadesException("Ya existe ese amigo.");
        } else {
            sessionFactory.getCurrentSession().save(amistad);
        }
    }

    @Transactional
    @Override
    public Amistad eliminarAmigo(Usuario usuario, Usuario amigo) throws AmistadesException {
        // Buscamos si existe antes la amistad de el usuario con el amigo
        Session session = sessionFactory.getCurrentSession();
        Amistad esAmigo = saberSiTieneEseAmigo(usuario.getId(), amigo.getId(), session);
        if (esAmigo !=null) {
            session.delete(esAmigo);
            return esAmigo;
        } else {
           throw new AmistadesException("No existe el amigo que queres eliminar en tu lista de amigos.");
        }
    }

    @Transactional
    @Override
    public List<Amistad> verAmigos(Long userId) {
        return (List<Amistad>) sessionFactory.getCurrentSession()
                .createCriteria(Amistad.class)
                .add(Restrictions.eq("usuario.id", userId))
                .list();
    }

    @Override
    public Amistad buscarAmigoDeUsuario(Usuario usuario, Usuario amigo) {
        return (Amistad) sessionFactory.getCurrentSession()
                .createCriteria(Amistad.class)
                .add(Restrictions.eq("usuario.id", usuario.getId()))
                .add(Restrictions.eq("amigo.id", amigo.getId()))
                .uniqueResult();
    }

    private Amistad saberSiTieneEseAmigo(Long idUsuario, Long idAmigo, Session session) {
        Amistad encontrada = (Amistad) session
                .createCriteria(Amistad.class)
                .add(Restrictions.eq("usuario.id", idUsuario))
                .add(Restrictions.eq("amigo.id", idAmigo))
                .uniqueResult();
        return encontrada;
    }

    @Override
    @Transactional
    public List<Amistad> obtenerTodasLasAmistades() {
        return (List<Amistad>) sessionFactory.getCurrentSession()
                .createCriteria(Amistad.class)
                .list();
    }
}
