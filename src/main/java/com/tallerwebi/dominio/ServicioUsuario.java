package com.tallerwebi.dominio;


public interface ServicioUsuario {
    Usuario buscar (String email);
    Usuario registrar(String email, String password, String nombre);
    void modificarEmail( String email, String nuevoEmail);
    void modificarNombreDeUsuario( String email, String nuevoUsuario);
    void modificarContra( String email, String nuevoPassword);
}
