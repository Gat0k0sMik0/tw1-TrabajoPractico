package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.*;
import com.tallerwebi.dominio.excepcion.AmistadesException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class ServicioAmistadImpl implements ServicioAmistad {

    @Autowired
    RepositorioAmistad repositorioAmistad;
    @Autowired
    RepositorioUsuario repositorioUsuario;

    public ServicioAmistadImpl(RepositorioAmistad repositorioAmistad, RepositorioUsuario repositorioUsuario) {
        this.repositorioAmistad = repositorioAmistad;
        this.repositorioUsuario = repositorioUsuario;
    }

    @Override
    public List<Usuario> getAmigosDeUnUsuarioPorId(Long id) {
        List<Amistad> todasLasAmistades = repositorioAmistad.obtenerTodasLasAmistades();
        List<Usuario> amigosDelUsuario = new ArrayList<>();

        for (Amistad amistad : todasLasAmistades) {
            if (amistad.getUsuario().getId().equals(id)) {
                amigosDelUsuario.add(amistad.getAmigo());
            } else if (amistad.getAmigo().getId().equals(id)) {
                amigosDelUsuario.add(amistad.getUsuario());
            }
        }

        return amigosDelUsuario;
    }


    @Override
    public void agregarAmigo(Usuario usuario, Usuario amigo) throws AmistadesException {
        repositorioAmistad.agregarAmigo(new Amistad(usuario, amigo));
    }

    @Override
    public void eliminarAmigo(Usuario usuario, Usuario amigo) throws AmistadesException {
        List<Amistad> todasLasAmistades = repositorioAmistad.obtenerTodasLasAmistades();

        for (Amistad amistad : todasLasAmistades) {
            if (amistad.getUsuario().getId().equals(usuario.getId()) &&
                    amistad.getAmigo().getId().equals(amigo.getId())) {
                repositorioAmistad.eliminarAmigo(usuario, amigo);
            } else if (amistad.getUsuario().getId().equals(amigo.getId()) &&
                        amistad.getAmigo().getId().equals(usuario.getId())) {
                repositorioAmistad.eliminarAmigo(amigo, usuario);
            }
        }
    }

    @Transactional
    @Override
    public List<Usuario> obtenerRecomendacionesQueNoSeanSusAmigos( Long userId) {
        List<Usuario> amigosDelUsuario = getAmigosDeUnUsuarioPorId(userId);
        List<Usuario> usuariosDeLaBaseDeDatos = repositorioUsuario.getUsuariosRandom(10,userId);
        List<Usuario> recomendaciones = new ArrayList<>();
        for (Usuario desconocido : usuariosDeLaBaseDeDatos) {
            if (!amigosDelUsuario.contains(desconocido)) {
                recomendaciones.add(desconocido);
            }
        }
        return recomendaciones;
    }
}
