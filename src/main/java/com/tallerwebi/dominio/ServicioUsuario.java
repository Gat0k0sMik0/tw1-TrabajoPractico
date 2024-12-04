package com.tallerwebi.dominio;


import com.tallerwebi.dominio.excepcion.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ServicioUsuario {
    Usuario buscar (String email);
    Usuario registrar(String email, String password, String nombre);
    Usuario actualizarPerfil(Usuario usuarioNuevo);
    Usuario buscarPorId(Long id);
    void verificarDatos(Usuario viejo, Usuario usuarioNuevo) throws ActualizarUsuarioException;;

    void cargarFotoPerfil(Usuario usuario, MultipartFile fotoPerfil) throws IOException;

    void asignarFotoDefault(Usuario usuario);

    boolean borrarFotoPerfil(String filename);

    String copiarFotoPerfil(MultipartFile file) throws IOException;
}
