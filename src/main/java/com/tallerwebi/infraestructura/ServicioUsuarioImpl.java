package com.tallerwebi.infraestructura;

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
    public ServicioUsuarioImpl (RepositorioUsuario servicioUsuario) {
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

  /*  @Override
    public void modificarEmail( String email, String nuevoEmail) {
        Usuario usuario = repositorioUsuario.buscarPorMail(email);
        if(usuario != null) {
            usuario.setEmail(nuevoEmail);
            repositorioUsuario.modificar(usuario);
        }
    }

    @Override
    public void modificarNombreDeUsuario( String email, String nuevoNombre) {
        Usuario usuario = repositorioUsuario.buscarPorMail(email);
        if(usuario != null) {
            usuario.setNombreUsuario(nuevoNombre);
            repositorioUsuario.modificar(usuario);
        }
    }

    @Override
    public void modificarContra( String email, String nuevaContra) {
        Usuario usuario = repositorioUsuario.buscarPorMail(email);
        if(usuario != null) {
            usuario.setPassword(nuevaContra);
            repositorioUsuario.modificar(usuario);
        }
    }*/

    @Override
    public Usuario actualizarPerfil(Usuario usuarioLogueado, Usuario usuarioNuevo) {
        usuarioLogueado.setNombreUsuario(usuarioNuevo.getNombreUsuario());
        usuarioLogueado.setEmail(usuarioNuevo.getEmail());
        usuarioLogueado.setPassword(usuarioNuevo.getPassword());
        usuarioLogueado.setConfirmPassword(usuarioNuevo.getConfirmPassword());
        repositorioUsuario.modificar(usuarioLogueado);
        return usuarioLogueado;
    }

    @Override
    public void verificarDatos(Usuario usuario) throws EmailInvalidoException, MailExistenteException, UsuarioInvalidoException, UsuarioExistenteException, ContraseniasDiferentesException, ContraseniaInvalidaException {
        Usuario usuarioEncontradoConMail = repositorioUsuario.buscarPorMail(usuario.getEmail());
        Usuario usuarioEncontradoConNombre = repositorioUsuario.buscarPorNombre(usuario.getNombreUsuario());

        // Validacion correo
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        if (!usuario.getEmail().matches(emailRegex) || !usuario.getEmail().endsWith(".com")) {
            throw new EmailInvalidoException();
        }
        // Que no exista usuario con mismo correo
        if(usuarioEncontradoConMail != null){
            throw new MailExistenteException();
        }
        // Validacion largo de nombre de usuario
        if (usuario.getNombreUsuario().length() > 16  || usuario.getNombreUsuario().length() < 4) {
            throw new UsuarioInvalidoException();
        }
        // Que no exista usuario con el mismo nombre
        if (usuarioEncontradoConNombre != null) {
            throw new UsuarioExistenteException();
        }
        // Validacion repetir contraseña
        if (!usuario.getPassword().equals(usuario.getConfirmPassword())) {
            throw new ContraseniasDiferentesException();
        }
        // Validacion largo de contraseña
        if (usuario.getPassword().length() > 15 || usuario.getPassword().length() < 5) {
            throw new ContraseniaInvalidaException();
        }
    }
}