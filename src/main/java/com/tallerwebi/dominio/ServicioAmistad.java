package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.AmistadesException;

import java.util.List;

public interface ServicioAmistad {
    List<Usuario> getAmigosDeUnUsuarioPorId(Long id);
    void agregarAmigo(Usuario usuario, Usuario amigo) throws AmistadesException;
    void eliminarAmigo(Usuario usuario, Usuario amigo) throws AmistadesException;

    List<Usuario> obtenerRecomendacionesQueNoSeanSusAmigos(Long userId);
}
