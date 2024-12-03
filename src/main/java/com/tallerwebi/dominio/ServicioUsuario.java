package com.tallerwebi.dominio;


import com.tallerwebi.dominio.excepcion.*;

import java.util.List;

public interface ServicioUsuario {
    Usuario buscar (String email);
    Usuario registrar(String email, String password, String nombre);
    Usuario actualizarPerfil(Usuario usuarioNuevo);
    Usuario buscarPorId(Long id);
    void verificarDatos(Usuario viejo, Usuario usuarioNuevo) throws ActualizarUsuarioException;;

    void agregarFotoPerfil(Usuario usuario, FotoPerfil foto);
}
