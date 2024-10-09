package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.*;
import com.tallerwebi.dominio.excepcion.AmistadesException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        List<Amistad> amistadesDelUsuario = repositorioAmistad.verAmigos(id);
        if (amistadesDelUsuario.isEmpty()) {
            return new ArrayList<>();
        } else {
            List<Usuario> amigosDelUsuario = new ArrayList<>();
            for (Amistad amistadDelUsuario : amistadesDelUsuario) {
                Usuario amigo = amistadDelUsuario.getAmigo();
                amigosDelUsuario.add(amigo);
            }
            return amigosDelUsuario;
        }

    }


    @Override
    public void agregarAmigo(Usuario usuario, Usuario amigo) throws AmistadesException {
        repositorioAmistad.agregarAmigo(new Amistad(usuario, amigo));
    }

    @Override
    public void eliminarAmigo(Usuario usuario, Usuario amigo) throws AmistadesException {
        repositorioAmistad.eliminarAmigo(usuario, amigo);
    }
}
