package com.tallerwebi.dominio;


import com.tallerwebi.dominio.excepcion.*;

public interface ServicioUsuario {
    Usuario buscar (String email);
    Usuario registrar(String email, String password, String nombre);
   /* void modificarEmail( String email, String nuevoEmail);
    void modificarNombreDeUsuario( String email, String nuevoUsuario);
    void modificarContra( String email, String nuevoPassword);*/
    Usuario actualizarPerfil(Usuario usuarioLogueado, Usuario usuarioNuevo);

    void verificarDatos(Usuario usuarioNuevo) throws EmailInvalidoException, MailExistenteException, UsuarioInvalidoException, UsuarioExistenteException, ContraseniasDiferentesException, ContraseniaInvalidaException;
}
