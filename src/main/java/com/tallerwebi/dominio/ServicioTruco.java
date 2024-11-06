package com.tallerwebi.dominio;

import java.util.List;

public interface ServicioTruco {
    void empezar(List<Jugador> jugadores);
    void empezar(List<Jugador> jugadores, List<Carta> cartas); // solo test
    // de juego
    void tirarCarta(Jugador jugador, Carta cartaSeleccionada);
    void determinarGanadorRonda(Jugador jugador1, Jugador jugador2);
    void cambiarTurno(Jugador jugador1);
    Integer accion (String accion, Jugador cantador, Jugador receptor, Integer puntosEnJuego);
    Accion getAccionPorNro (Integer nro);
    void guardarPuntos(Jugador j, Integer puntos);
    Integer getPuntosDeUnJugador (Jugador jugador);
    void actualizarRespuestaDeAccion(Integer nroAccion, Boolean respuesta);
    Jugador responder(String accion, Jugador ejecutor, Jugador receptor, Integer nroAccion);
    Integer preguntar(String accion, Jugador ejecutor, Jugador receptor);

    void sumarPuntosEnJuego(Integer nroAccion, Integer puntosEnJuego);

    // de comprobación
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
}
