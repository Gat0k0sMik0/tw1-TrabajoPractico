package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.AmistadesException;

import java.util.List;

public interface ServicioSolicitudAmistad {
    void enviarSolicitudAmistad(Long solicitanteId, Long receptorId);

    void aceptarSolicitudAmistad(Long solicitudId) throws AmistadesException;

    void rechazarSolicitudAmistad(Long solicitudId);

    List<SolicitudAmistad> obtenerSolicitudesEnviadas(Long idUsuario);

    List<SolicitudAmistad> obtenerSolicitudesRecibidasNoAceptadas(Long idUsuario);

    List<Usuario> queNoTenganSolicitudesEntreEllos(Long idUsuario, List<Usuario> usuarios);

    void eliminarSolicitudEntreAmbos(Long idUsuario, Long idAmigo);

    void eliminarSolicitud(Long idSolicitud);
}
