package com.tallerwebi.dominio;

public interface ServicioPartida2 {
    Truco2 empezar(Jugador j1, Jugador j2);

    Truco2 empezarTest(Jugador j1, Jugador j2);

    Ronda2 tirarCarta(Mano2 mano, Jugador jugador, Carta carta, String nroJugador);

    void determinarGanadorRonda(Jugador jugador1, Jugador jugador2);

    void cambiarTurno(Jugador jugador);

    Integer preguntar(String accion, Jugador ejecutor, Jugador receptor);
}
