package com.tallerwebi.dominio;

import javax.transaction.Transactional;

public interface RepositorioTruco {
    @Transactional
    void guardarPartida(Truco2 truco);

    @Transactional
    Truco2 buscarPartidaPorId (Long id);

    @Transactional
    void guardarJugador(Jugador jugador);

    @Transactional
    Jugador obtenerJugadorPorID(Long id);
}
