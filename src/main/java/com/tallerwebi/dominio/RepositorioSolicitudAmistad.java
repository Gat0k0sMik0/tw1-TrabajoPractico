package com.tallerwebi.dominio;

import javax.transaction.Transactional;
import java.util.List;

public interface RepositorioSolicitudAmistad {
    SolicitudAmistad buscarSolicitudPorId(Long idSolicitud);
    void guardarSolicitudAmistad(SolicitudAmistad solicitud);
    void eliminarSolicitudAmistad(Long idSolicitud);

    @Transactional
    void eliminarSolicitudEntreAmbos(Long idUsuario, Long idAmigo);

    List<SolicitudAmistad> listarSolicitudesDeAmistad();

    @Transactional
    @SuppressWarnings("unchecked")
    List<SolicitudAmistad> buscarPorSolicitanteYEstado(Long solicitanteId, String estado);

    @Transactional
    @SuppressWarnings("unchecked")
    List<SolicitudAmistad> buscarPorReceptorYEstado(Long receptorId, String estado);

    @Transactional
    boolean existePorSolicitanteReceptorYEstado(Long solicitanteId, Long receptorId, String estado);
}
