package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.*;

public interface ServicioLogin {

    Usuario consultarUsuario(String email, String password);
    void registrar(Usuario usuario) throws MailExistenteException, ContraseniaInvalidaException, UsuarioInvalidoException, UsuarioExistenteException, EmailInvalidoException, ContraseniasDiferentesException;

}
