package com.tallerwebi.dominio;

import java.util.List;

public interface ServicioTruco {
    void empezar(List<Jugador> jugadores);
    void empezar(List<Jugador> jugadores, List<Carta> cartas); // solo test
    // de juego
    void tirarCarta(Jugador jugador, Carta cartaSeleccionada);
    void determinarGanadorRonda(Jugador jugador1, Jugador jugador2);
    void cambiarTurno(Jugador jugador1);

    // de comprobación
    List<Ronda> getRondasJugadas();

    Boolean esLaPrimerRonda();

    void verificarEnvido(Jugador jugador1, Jugador jugador2);

    List<Carta> getCartasJugadas(Jugador j);
    Boolean esTurnoJugador(String jugadorNombre);
    Jugador getTurnoJugador();
    Jugador ganadorGeneral();
    void validarCartas(List<Carta> cartasJugador2);
    Boolean saberSiLaManoEstaTerminada();
    List<Ronda> getRondasDeLaManoActual();
    Integer getNumeroDeRondasJugadasDeLaManoActual();
    Integer getMovimientosDeLaManoActual();
    Truco getTruco();
    List<Mano> getManosJugadas();
}
