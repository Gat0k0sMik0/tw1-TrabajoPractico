package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.RepositorioUsuario;
import com.tallerwebi.dominio.ServicioUsuario;
import com.tallerwebi.dominio.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class ServicioUsuarioImpl implements ServicioUsuario {

    RepositorioUsuario repositorioUsuario;

    @Autowired
    public ServicioUsuarioImpl (RepositorioUsuario servicioUsuario) {
     this.repositorioUsuario = servicioUsuario;
    }

    @Override
    public Usuario buscar(String email) {
        Usuario usuario = repositorioUsuario.buscar(email);
        if (usuario == null) {
            return null;
        }
        return usuario;
    }

    @Override
    public Usuario registrar(String email, String password, String nombre) {
        Usuario existeOtro = repositorioUsuario.buscar(email);
        if (existeOtro != null) {
            return null;
        }
        Usuario u = new Usuario();
        u.setEmail(email);
        u.setPassword(password);
        u.setNombre(nombre);
        repositorioUsuario.guardar(u);
        return u;
    }

    @Override
    public void modificarEmail( String email, String nuevoEmail) {
        Usuario usuario = repositorioUsuario.buscar(email);
        if(usuario != null) {
            usuario.setEmail(nuevoEmail);
            repositorioUsuario.modificar(usuario);
        }
    }

    @Override
    public void modificarNombreDeUsuario( String email, String nuevoNombre) {
        Usuario usuario = repositorioUsuario.buscar(email);
        if(usuario != null) {
            usuario.setNombre(nuevoNombre);
            repositorioUsuario.modificar(usuario);
        }
    }

    @Override
    public void modificarContra( String email, String nuevaContra) {
        Usuario usuario = repositorioUsuario.buscar(email);
        if(usuario != null) {
            usuario.setPassword(nuevaContra);
            repositorioUsuario.modificar(usuario);
        }
    }
}