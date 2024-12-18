package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.AmistadesException;

import javax.transaction.Transactional;
import java.util.List;

public interface RepositorioAmistad {
    void agregarAmigo(Amistad amistad) throws AmistadesException;
    Amistad eliminarAmigo(Usuario usuario, Usuario amigo) throws AmistadesException;
    List<Amistad> verAmigos(Long userId);
    Amistad buscarAmigoDeUsuario(Usuario usuario, Usuario amigo) throws AmistadesException;

    List<Amistad> obtenerTodasLasAmistades();

    @Transactional
    void eliminarAmistad(Long idAmistad);
}
