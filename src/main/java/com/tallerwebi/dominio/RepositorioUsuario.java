package com.tallerwebi.dominio;

public interface RepositorioUsuario {

    Usuario buscarUsuario(String email, String password);
    void guardar(Usuario usuario);
    Usuario buscarPorMail(String email);
    Usuario buscarPorNombre(String nombreUsuario);
    void modificar(Usuario usuario);

}

