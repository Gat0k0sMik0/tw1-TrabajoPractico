package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Amistad;
import com.tallerwebi.dominio.Mensaje;
import com.tallerwebi.dominio.RepositorioMensaje;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class RepositorioMensajeImpl  implements RepositorioMensaje {

    SessionFactory sessionFactory;

    @Autowired
    public RepositorioMensajeImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Transactional
    @Override
    public void guardar(Mensaje mensaje) {
        this.sessionFactory.getCurrentSession().save(mensaje);
    }

    @Transactional
    @Override
    public List<Mensaje> obtenerMensajeEntreUsuarios(Long fromUserId, Long toUserId) {
        return (List<Mensaje>) sessionFactory.getCurrentSession()
                .createCriteria(Mensaje.class)
                .add(Restrictions.or(
                        Restrictions.and(
                                Restrictions.eq("fromUser.id", fromUserId),
                                Restrictions.eq("toUser.id", toUserId)
                        ),
                        Restrictions.and(
                                Restrictions.eq("fromUser.id", toUserId),
                                Restrictions.eq("toUser.id", fromUserId)
                        )
                ))
                .list();
    }
}
