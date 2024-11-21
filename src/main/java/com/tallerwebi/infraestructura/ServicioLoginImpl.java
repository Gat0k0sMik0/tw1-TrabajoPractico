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
    public void registrar(Usuario usuario) throws MailExistenteException, ContraseniaInvalidaException, UsuarioInvalidoException, UsuarioExistenteException, EmailInvalidoException, ContraseniasDiferentesException {
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

    @Override
    public void guardarUsuario(Usuario usuario) {
        repositorioUsuario.guardar(usuario);
    }


}

