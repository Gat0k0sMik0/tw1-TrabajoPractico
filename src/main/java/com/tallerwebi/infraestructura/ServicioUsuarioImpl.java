package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.FotoPerfil;
import com.tallerwebi.dominio.RepositorioUsuario;
import com.tallerwebi.dominio.ServicioUsuario;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.dominio.excepcion.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class ServicioUsuarioImpl implements ServicioUsuario {

    RepositorioUsuario repositorioUsuario;

    @Autowired
    public ServicioUsuarioImpl(RepositorioUsuario servicioUsuario) {
        this.repositorioUsuario = servicioUsuario;
    }

    @Override
    public Usuario buscar(String email) {
        Usuario usuario = repositorioUsuario.buscarPorMail(email);
        if (usuario == null) {
            return null;
        }
        return usuario;
    }

    @Override
    public Usuario registrar(String email, String password, String nombre) {
        Usuario existeOtro = repositorioUsuario.buscarPorMail(email);
        if (existeOtro != null) {
            return null;
        }
        Usuario u = new Usuario();
        u.setEmail(email);
        u.setPassword(password);
        u.setNombreUsuario(nombre);
        repositorioUsuario.guardar(u);
        return u;
    }

    @Override
    public Usuario actualizarPerfil(Usuario usuarioNuevo) {
        repositorioUsuario.modificar(usuarioNuevo);
        return usuarioNuevo;
    }

    @Override
    public Usuario buscarPorId(Long id) {
        return repositorioUsuario.buscarPorId(id);
    }

    @Override
    public void verificarDatos(Usuario usuarioViejo, Usuario usuario) throws ActualizarUsuarioException {
        Usuario usuarioEncontradoConMail;
        Usuario usuarioEncontradoConNombre = repositorioUsuario.buscarPorNombre(usuario.getNombreUsuario());

        // Si cambio el email, fijarse que no existe otro con ese mail
        if (!usuarioViejo.getEmail().equals(usuario.getEmail())) {
            usuarioEncontradoConMail = repositorioUsuario.buscarPorMail(usuario.getEmail());
            if (usuarioEncontradoConMail != null) {
                throw new ActualizarUsuarioException("Ya existe un usuario con ese correo. Intenta otro.");
            }
        }

        if (usuario.getPassword().isEmpty() && usuario.getConfirmPassword().isEmpty()) {
            usuario.setPassword(usuarioViejo.getPassword());
            usuario.setConfirmPassword(usuarioViejo.getPassword());
        } else {
            // Si cambio la contraseña
            if (!usuario.getPassword().equals(usuarioViejo.getPassword())) {
                // Validacion repetir contraseña
                if (!usuario.getPassword().equals(usuario.getConfirmPassword()))
                    throw new ActualizarUsuarioException("Las contraseñas que ingresaste son distintas. Deben ser iguales");
                // Validacion largo de contraseña
                if (usuario.getPassword().length() > 15 || usuario.getPassword().length() < 5)
                    throw new ActualizarUsuarioException("El largo de la contraseña debe ser de entre 5 y 15 caracteres");
            }
        }

        // Validacion correo
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        if (!usuario.getEmail().matches(emailRegex) || !usuario.getEmail().endsWith(".com"))
            throw new ActualizarUsuarioException("El e-mail está mal escrito");
        // Validacion largo de nombre de usuario
        if (usuario.getNombreUsuario().length() > 16 || usuario.getNombreUsuario().length() < 4)
            throw new ActualizarUsuarioException("El largo del nombre de usuario debe ser de entre 5 y 16 caracteres");
        // Que no exista usuario con el mismo nombre
        if (usuarioEncontradoConNombre != null)
            throw new ActualizarUsuarioException("Ya existe un usuario con ese nombre de usuario");

    }

    @Override
    public void agregarFotoPerfil(Usuario usuario, FotoPerfil foto) {
        usuario.setFotoPerfil(foto);
        usuario.setUrlFotoPerfil(foto.getImagen());
    }
}