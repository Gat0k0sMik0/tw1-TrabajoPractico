package com.tallerwebi.dominio;

import java.util.List;

public interface RepositorioUsuario {

    Usuario buscarPorId(Long id);
    Usuario buscarUsuario(String email, String password);
    void guardar(Usuario usuario);
    Usuario buscarPorMail(String email);
    Usuario buscarPorNombre(String nombreUsuario);
    void modificar(Usuario usuario);
    List<Usuario> getUsuariosRandom(Integer cantidad, Long idUsuario);

}

