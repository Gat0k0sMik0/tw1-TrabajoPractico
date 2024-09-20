package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.UsuarioExistente;

public interface ServicioInicio {

    Usuario consultarUsuario(String nombre, String password);

}
