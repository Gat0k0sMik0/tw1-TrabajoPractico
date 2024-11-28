package com.tallerwebi.dominio;

import javax.transaction.Transactional;

public interface ServicioPartida2 {
    // TODO: despues cambiar

    Partida obtenerPartidaPorId(Long id);

    Partida empezar(Jugador j1, Jugador j2);

    void reset(Jugador j1, Jugador j2);

    void guardarJugador(Jugador jugador1);

    @Transactional
    Jugador verificarGanador(Partida partida);

    ;
}
