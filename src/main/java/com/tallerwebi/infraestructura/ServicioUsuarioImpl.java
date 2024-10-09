package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.RepositorioUsuario;
import com.tallerwebi.dominio.ServicioUsuario;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.dominio.excepcion.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
    public Usuario actualizarPerfil(Usuario usuarioLogueado, Usuario usuarioNuevo) {
        usuarioLogueado.setNombreUsuario(usuarioNuevo.getNombreUsuario());
        usuarioLogueado.setEmail(usuarioNuevo.getEmail());
        usuarioLogueado.setPassword(usuarioNuevo.getPassword());
        usuarioLogueado.setConfirmPassword(usuarioNuevo.getConfirmPassword());
        repositorioUsuario.modificar(usuarioLogueado);
        return usuarioLogueado;
    }

    @Override
    public Usuario buscarPorId(Long id) {
        return repositorioUsuario.buscarPorId(id);
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
        if (usuarioEncontradoConMail != null) {
            throw new MailExistenteException();
        }
        // Validacion largo de nombre de usuario
        if (usuario.getNombreUsuario().length() > 16 || usuario.getNombreUsuario().length() < 4) {
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

    @Override
    public List<Usuario> getUsuariosRandom(Integer cantidad, Long idUsuario) {
        return repositorioUsuario.getUsuariosRandom(cantidad, idUsuario);
    }

    private Boolean saberSiExisteUnUsuarioEnLaLista(Usuario usuario, List<Usuario> lista){
        return lista.contains(usuario);
    }
}