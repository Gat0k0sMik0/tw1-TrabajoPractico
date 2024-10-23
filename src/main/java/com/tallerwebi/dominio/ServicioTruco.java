package com.tallerwebi.dominio;

import java.util.List;

public interface ServicioTruco {
    void empezar(Jugador j1, Jugador j2);
    void tirarCarta(Jugador jugador, Carta cartaSeleccionada);
    List<Carta> getCartasJugadas(Jugador j);
    List<Ronda> getRondasJugadas();
    void cambiarTurno(Jugador jugador1);
    Boolean esTurnoJugador(String jugadorNombre);
    Jugador getTurnoJugador();
    void determinarGanadorRonda(Jugador jugador1, Jugador jugador2);

    void validarCartas(List<Carta> cartasJugador2);

    void rechazarTruco(Jugador jugadorActual);

    void aceptarTruco(Jugador jugador1, Jugador jugador2);

    void cantarTruco(Jugador jugadorActual);
}
