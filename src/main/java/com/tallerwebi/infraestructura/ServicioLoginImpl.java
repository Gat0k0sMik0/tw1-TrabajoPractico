package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.RepositorioUsuario;
import com.tallerwebi.dominio.ServicioLogin;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.dominio.excepcion.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service("servicioLogin")
@Transactional
public class ServicioLoginImpl implements ServicioLogin {

    private final RepositorioUsuario repositorioUsuario;

    @Autowired
    public ServicioLoginImpl(RepositorioUsuario repositorioUsuario){
        this.repositorioUsuario = repositorioUsuario;
    }

    @Override
    public Usuario consultarUsuario (String email, String password) {
        return repositorioUsuario.buscarUsuario(email, password);
    }

    @Override
    public void registrar(Usuario usuario) throws ActualizarUsuarioException {
        Usuario usuarioEncontradoConMail = repositorioUsuario.buscarPorMail(usuario.getEmail());
        Usuario usuarioEncontradoConNombre = repositorioUsuario.buscarPorNombre(usuario.getNombreUsuario());

        // Validacion correo
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        if (!usuario.getEmail().matches(emailRegex) || !usuario.getEmail().endsWith(".com"))
            throw new ActualizarUsuarioException("El e-mail está mal escrito");
        // Que no exista usuario con mismo correo
        if (usuarioEncontradoConMail != null)
            throw new ActualizarUsuarioException("No se encontró el usuario para actualizar");
        // Validacion largo de nombre de usuario
        if (usuario.getNombreUsuario().length() > 16 || usuario.getNombreUsuario().length() < 4)
            throw new ActualizarUsuarioException("El largo del nombre de usuario debe ser de entre 5 y 16 caracteres");
        // Que no exista usuario con el mismo nombre
        if (usuarioEncontradoConNombre != null)
            throw new ActualizarUsuarioException("Ya existe un usuario con ese nombre de usuario");
        // Validacion repetir contraseña
        if (!usuario.getPassword().equals(usuario.getConfirmPassword()))
            throw new ActualizarUsuarioException("Las contraseñas que ingresaste son distintas. Deben ser iguales");
        // Validacion largo de contraseña
        if (usuario.getPassword().length() > 15 || usuario.getPassword().length() < 5)
            throw new ActualizarUsuarioException("El largo dela contraseña debe ser de entre 5 y 15 caracteres");
    }

    @Override
    public void guardarUsuario(Usuario usuario) {
        repositorioUsuario.guardar(usuario);
    }


}

