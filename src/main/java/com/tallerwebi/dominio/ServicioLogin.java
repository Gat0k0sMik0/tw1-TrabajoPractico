package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.*;
import org.springframework.core.io.FileSystemResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ServicioLogin {

    Usuario consultarUsuario(String email, String password);

    void registrar(Usuario usuario) throws ActualizarUsuarioException, MailExistenteException, ContraseniasDiferentesException, ContraseniaInvalidaException, UsuarioExistenteException, UsuarioInvalidoException;

    void guardarUsuario(Usuario usuario);

    void agregarFotoPerfil(Usuario usuario, MultipartFile fotoPerfil) throws IOException;

    void asignarFotoDefault(Usuario usuario) throws IOException;
}
