package com.tallerwebi.dominio;

import java.util.List;

public interface ServicioTruco {

    // PRINCIPALES
    void empezar(List<Jugador> jugadores);
    void empezar(List<Jugador> jugadores, List<Carta> cartas);
    Jugador responder(String accion, Jugador ejecutor, Jugador receptor, Integer nroAccion);
    Integer preguntar(String accion, Jugador ejecutor, Jugador receptor);

    // Comenzar partida (TEST)
    void empezar(Jugador j1, Jugador j2);

    Integer accion (String accion, Jugador cantador, Jugador receptor, Integer puntosEnJuego);
    void cambiarTurno(Jugador jugador1);

    // Comenzar partida (TEST)
    void empezar(Jugador j1, Jugador j2, List<Carta> cartas);

    void tirarCarta(Jugador jugador, Carta cartaSeleccionada);

    // de juego
    void determinarGanadorRonda(Jugador jugador1, Jugador jugador2);
    Accion getAccionPorNro (Integer nro);
    Integer getPuntosDeJugador (Jugador jugador);


    // Si se termina la mano


    // DE COMPROBACION (DESARROLLO)
    List<Ronda> getRondasJugadas();
    Integer calcularTantosDeCartasDeUnJugador(Jugador j);
    Boolean esLaPrimerRonda();
    List<Accion> getAcciones();
    Integer getPuntosEnJuegoDeAccion(Integer nroAccion);

    Jugador getTurnoJugador();
    Jugador ganadorGeneral();
    Boolean saberSiLaManoEstaTerminada();
    List<Ronda> getRondasDeLaManoActual();
    Integer getNumeroDeRondasJugadasDeLaManoActual();
    Integer getMovimientosDeLaManoActual();
    Truco getTruco();
    List<Mano> getManosJugadas();
    Boolean cantarTruco();

    String saberAccion(Integer nroAccion);

    void terminarMano();

    // Nuevos
    Integer getCantidadJugadores();
    List<Jugador> getJugadores();
    List<Ronda> getRondas();
    List<Mano> getManos();

    // nuevos - solo test
    Jugador getUltimoGanadorDeMano();

    Integer getCartasTiradas();

    String getGanadorDeRondaPorNumero(int i);
}
