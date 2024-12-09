package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.*;
import com.tallerwebi.dominio.excepcion.AmistadesException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class ServicioSolicitudAmistadImpl implements ServicioSolicitudAmistad {
    private final RepositorioUsuario repositorioUsuario;
    private final RepositorioSolicitudAmistad repositorioSolicitudAmistad;
    private final RepositorioAmistad repositorioAmistad;

    @Autowired
    public ServicioSolicitudAmistadImpl(RepositorioSolicitudAmistad repositorioSolicitudAmistad, RepositorioUsuario repositorioUsuario, RepositorioAmistad repositorioAmistad) {
        this.repositorioSolicitudAmistad = repositorioSolicitudAmistad;
        this.repositorioUsuario = repositorioUsuario;
        this.repositorioAmistad = repositorioAmistad;
    }

    @Override
    @Transactional
    public void enviarSolicitudAmistad(Long solicitanteId, Long receptorId) {
        Usuario solicitante = repositorioUsuario.buscarPorId(solicitanteId);
        Usuario receptor = repositorioUsuario.buscarPorId(receptorId);

        // Verificar que no haya una solicitud previa pendiente entre los usuarios
        boolean existeSolicitud = repositorioSolicitudAmistad.existePorSolicitanteReceptorYEstado(
                solicitanteId, receptorId, "enviado");

        if (existeSolicitud) {
            throw new RuntimeException("Ya existe una solicitud de amistad pendiente o enviada entre estos usuarios.");
        }

        // Crear nueva solicitud
        SolicitudAmistad solicitud = new SolicitudAmistad();
        solicitud.setSolicitante(solicitante);
        solicitud.setReceptor(receptor);
        solicitud.setEstado("enviado");

        repositorioSolicitudAmistad.guardarSolicitudAmistad(solicitud);
    }

    @Override
    public void aceptarSolicitudAmistad(Long solicitudId) throws AmistadesException {
        SolicitudAmistad solicitud = repositorioSolicitudAmistad.buscarSolicitudPorId(solicitudId);

        if (!solicitud.getEstado().equals("enviado")) {
            throw new RuntimeException("La solicitud no puede ser procesada");
        }

        solicitud.setEstado("aceptada");
        repositorioSolicitudAmistad.guardarSolicitudAmistad(solicitud);

        // Crear amistad
        Amistad amistad = new Amistad();
        amistad.setUsuario(solicitud.getSolicitante());
        amistad.setAmigo(solicitud.getReceptor());
        repositorioAmistad.agregarAmigo(amistad);
    }

    @Override
    public void rechazarSolicitudAmistad(Long solicitudId) {
        SolicitudAmistad solicitud = repositorioSolicitudAmistad.buscarSolicitudPorId(solicitudId);

        if (!solicitud.getEstado().equals("enviado")) {
            throw new RuntimeException("La solicitud no puede ser procesada");
        }

        solicitud.setEstado("rechazada");
        repositorioSolicitudAmistad.guardarSolicitudAmistad(solicitud);
    }

    @Override
    public List<SolicitudAmistad> obtenerSolicitudesEnviadas(Long idUsuario) {
        // Listar todas las solicitudes de amistad
        List<SolicitudAmistad> todasLasSolicitudes = repositorioSolicitudAmistad.listarSolicitudesDeAmistad();

        // Crear una lista para almacenar las solicitudes enviadas por el usuario
        List<SolicitudAmistad> solicitudesEnviadas = new ArrayList<>();

        // Filtrar las solicitudes que tengan el idUsuario como solicitante y el estado 'ENVIADO'
        for (SolicitudAmistad solicitud : todasLasSolicitudes) {
            if (solicitud.getSolicitante().getId().equals(idUsuario) && solicitud.getEstado().equals("enviada")) {
                solicitudesEnviadas.add(solicitud);
            }
        }

        return solicitudesEnviadas;
    }

    @Override
    public List<SolicitudAmistad> obtenerSolicitudesRecibidasNoAceptadas(Long idUsuario) {
        // Listar todas las solicitudes de amistad
        List<SolicitudAmistad> todasLasSolicitudes = repositorioSolicitudAmistad.listarSolicitudesDeAmistad();

        // Crear una lista para almacenar las solicitudes recibidas y no aceptadas
        List<SolicitudAmistad> solicitudesRecibidasNoAceptadas = new ArrayList<>();

        // Filtrar las solicitudes que tengan el idUsuario como receptor y el estado no sea ACEPTADO
        for (SolicitudAmistad solicitud : todasLasSolicitudes) {
            if (solicitud.getReceptor().getId().equals(idUsuario) && !solicitud.getEstado().equals("aceptada")) {
                solicitudesRecibidasNoAceptadas.add(solicitud);
            }
        }

        return solicitudesRecibidasNoAceptadas;
    }

    @Override
    public List<Usuario> queNoTenganSolicitudesEntreEllos(Long idUsuario, List<Usuario> usuarios) {
        List<Usuario> usuariosSinSolicitud = new ArrayList<>();

        // Obtén todas las solicitudes de amistad
        List<SolicitudAmistad> todasLasSolicitudes = repositorioSolicitudAmistad.listarSolicitudesDeAmistad();

        // Revisa cada usuario en la lista
        for (Usuario usuario : usuarios) {
            boolean tieneSolicitud = false;

            // Revisa si existe una solicitud entre idUsuario y el usuario actual
            for (SolicitudAmistad solicitud : todasLasSolicitudes) {
                if ((solicitud.getSolicitante().getId().equals(idUsuario) && solicitud.getReceptor().getId().equals(usuario.getId())) ||
                        (solicitud.getSolicitante().getId().equals(usuario.getId()) && solicitud.getReceptor().getId().equals(idUsuario))) {
                    tieneSolicitud = true;
                    break;  // Si ya hay solicitud, no es necesario seguir buscando
                }
            }

            // Si no tienen solicitud, lo agregamos a la lista
            if (!tieneSolicitud) {
                usuariosSinSolicitud.add(usuario);
            }
        }

        return usuariosSinSolicitud;
    }

    @Override
    public void eliminarSolicitudEntreAmbos(Long idUsuario, Long idAmigo) {
        repositorioSolicitudAmistad.eliminarSolicitudEntreAmbos(idUsuario, idAmigo);
    }

    @Override
    public void eliminarSolicitud(Long idSolicitud) {
        repositorioSolicitudAmistad.eliminarSolicitudAmistad(idSolicitud);
    }
}
