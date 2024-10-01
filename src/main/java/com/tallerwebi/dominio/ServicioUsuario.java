package com.tallerwebi.dominio;


public interface ServicioUsuario {
    Usuario buscar (String email);
    Usuario registrar(String email, String password, String nombre);
    void modificarUsuario(String nombreNuevo, String email, String nuevoEmail, String nuevaContra);
}
