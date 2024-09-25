package com.tallerwebi.dominio;


public interface ServicioUsuario {
    Usuario buscar (String email);
    Usuario registrar(String email, String password);
}
