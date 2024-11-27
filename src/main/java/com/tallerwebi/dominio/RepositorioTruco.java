package com.tallerwebi.dominio;

import javax.transaction.Transactional;

public interface RepositorioTruco {
    @Transactional
    void guardarPartida(Partida truco);

    @Transactional
    void merge(Partida truco);

    @Transactional
    Partida buscarPartidaPorId (Long id);

    @Transactional
    void guardarJugador(Jugador jugador);

    @Transactional
    Jugador obtenerJugadorPorID(Long id);
}
