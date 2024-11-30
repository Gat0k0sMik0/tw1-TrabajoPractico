package com.tallerwebi.dominio;

import javax.transaction.Transactional;

public interface RepositorioEstadistica {
    @Transactional
    Estadistica obtenerEstadisticaDeJugador(Long idJugador);

    @Transactional
    void guardarEstadistica(Estadistica e);
}
