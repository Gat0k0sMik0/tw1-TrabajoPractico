package com.tallerwebi.dominio;

import java.util.List;

public interface ServicioPartida2 {
    // TODO: despues cambiar

    Truco2 obtenerPartidaPorId(Long id);

    Truco2 empezar(Jugador j1, Jugador j2);

    void reset(Jugador j1, Jugador j2);

    void guardarJugador(Jugador jugador1);

    ;
}
