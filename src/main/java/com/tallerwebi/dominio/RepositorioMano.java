package com.tallerwebi.dominio;

import javax.transaction.Transactional;

public interface RepositorioMano {
    @Transactional
    void eliminarCartasDeManosPorUsuario(Long usuarioId);

    void guardar(Mano mano);

    void merge(Mano mano);

    Mano obtenerUltimaMano(Long idPartida);

    Mano obtenerManoPorId(Long id);
}
