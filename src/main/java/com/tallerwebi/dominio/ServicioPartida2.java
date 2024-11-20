package com.tallerwebi.dominio;

import java.util.List;

public interface ServicioPartida2 {
    // TODO: despues cambiar
    List<Carta> getCartasTiradasJ1();

    List<Carta> getCartasTiradasJ2();

    Truco2 obtenerPartidaPorId(Long id);

    Truco2 empezar(Jugador j1, Jugador j2);

    void reset(Jugador j1, Jugador j2);

    Truco2 empezarTest(Jugador j1, Jugador j2);

    Ronda tirarCarta(Mano2 mano, Jugador jugador, Carta carta, String nroJugador);

    void determinarGanadorRonda(Truco2 truco, Jugador jugador1, Jugador jugador2);

    ;
}
