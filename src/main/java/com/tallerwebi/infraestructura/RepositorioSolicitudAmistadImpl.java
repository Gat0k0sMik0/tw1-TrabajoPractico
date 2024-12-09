package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.RepositorioSolicitudAmistad;
import com.tallerwebi.dominio.SolicitudAmistad;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public class RepositorioSolicitudAmistadImpl implements RepositorioSolicitudAmistad {
    private SessionFactory sessionFactory;

    @Autowired
    public RepositorioSolicitudAmistadImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Transactional
    @Override
    public SolicitudAmistad buscarSolicitudPorId(Long idSolicitud) {
        return (SolicitudAmistad) sessionFactory.getCurrentSession()
                .createCriteria(SolicitudAmistad.class)
                .add(Restrictions.eq("id", idSolicitud))
                .uniqueResult();
    }

    @Transactional
    @Override
    public void guardarSolicitudAmistad(SolicitudAmistad solicitud) {
        sessionFactory.getCurrentSession().save(solicitud);
    }

    @Transactional
    @Override
    public void eliminarSolicitudAmistad(Long idSolicitud) {
        /// Buscar la solicitud por su ID
        SolicitudAmistad solicitud = (SolicitudAmistad) sessionFactory.getCurrentSession()
                .createCriteria(SolicitudAmistad.class)
                .add(Restrictions.eq("id", idSolicitud))
                .uniqueResult();

        // Si la solicitud existe, eliminarla
        if (solicitud != null) {
            sessionFactory.getCurrentSession().delete(solicitud);
        }
    }

    @Transactional
    @Override
    public void eliminarSolicitudEntreAmbos(Long idUsuario, Long idAmigo) {
        // Buscar la solicitud de amistad entre los dos usuarios
        SolicitudAmistad solicitud = (SolicitudAmistad) sessionFactory.getCurrentSession()
                .createCriteria(SolicitudAmistad.class)
                .add(Restrictions.or(
                        Restrictions.and(
                                Restrictions.eq("solicitante.id", idUsuario),
                                Restrictions.eq("receptor.id", idAmigo)
                        ),
                        Restrictions.and(
                                Restrictions.eq("solicitante.id", idAmigo),
                                Restrictions.eq("receptor.id", idUsuario)
                        )
                ))
                .uniqueResult();

        // Si la solicitud existe, eliminarla
        if (solicitud != null) {
            eliminarSolicitudAmistad(solicitud.getId()); // Llamamos al método que ya tienes para eliminarla por ID
        } else {
            throw new IllegalStateException("No se encontró una solicitud de amistad entre estos usuarios");
        }
    }

    @Transactional
    @Override
    public List<SolicitudAmistad> listarSolicitudesDeAmistad() {
        return (List<SolicitudAmistad>) sessionFactory.getCurrentSession()
                .createCriteria(SolicitudAmistad.class)
                .list();
    }

    @Transactional
    @Override
    @SuppressWarnings("unchecked")
    public List<SolicitudAmistad> buscarPorSolicitanteYEstado(Long solicitanteId, String estado) {
        return (List<SolicitudAmistad>) sessionFactory.getCurrentSession()
                .createCriteria(SolicitudAmistad.class)
                .add(Restrictions.eq("solicitante.id", solicitanteId))
                .add(Restrictions.eq("estado", estado))
                .list();
    }

    @Transactional
    @Override
    @SuppressWarnings("unchecked")
    public List<SolicitudAmistad> buscarPorReceptorYEstado(Long receptorId, String estado) {
        return (List<SolicitudAmistad>) sessionFactory.getCurrentSession()
                .createCriteria(SolicitudAmistad.class)
                .add(Restrictions.eq("receptor.id", receptorId))
                .add(Restrictions.eq("estado", estado))
                .list();
    }

    @Override
    @Transactional
    public boolean existePorSolicitanteReceptorYEstado(Long solicitanteId, Long receptorId, String estado) {
        Long count = (Long) sessionFactory.getCurrentSession()
                .createCriteria(SolicitudAmistad.class)
                .setProjection(Projections.rowCount()) // Esto cuenta el número de registros
                .add(Restrictions.eq("solicitante.id", solicitanteId))
                .add(Restrictions.eq("receptor.id", receptorId))
                .add(Restrictions.eq("estado", estado))
                .uniqueResult(); // Obtiene el valor único, que será el conteo

        return count > 0; // Si el conteo es mayor a 0, existe la solicitud
    }

}
