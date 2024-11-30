package com.tallerwebi.dominio;


import com.tallerwebi.dominio.excepcion.*;

import java.util.List;

public interface ServicioUsuario {
    Usuario buscar (String email);
    Usuario registrar(String email, String password, String nombre);
   /* void modificarEmail( String email, String nuevoEmail);
    void modificarNombreDeUsuario( String email, String nuevoUsuario);
    void modificarContra( String email, String nuevoPassword);*/
    Usuario actualizarPerfil(Usuario usuarioNuevo);
    Usuario buscarPorId(Long id);
    void verificarDatos(Usuario viejo, Usuario usuarioNuevo) throws ActualizarUsuarioException;
    List<Usuario> getUsuariosRandom(Integer cantidad, Long idUsuarioExcluido);
}
