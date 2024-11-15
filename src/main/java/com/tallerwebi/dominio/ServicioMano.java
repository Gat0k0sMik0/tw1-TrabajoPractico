package com.tallerwebi.dominio;

import java.util.List;

public interface ServicioMano {
    void empezar();
    List<Mano> getManos();

    Boolean estaLaManoTerminada();

    void guardarRonda(Jugador jugador, Carta cartaSeleccionada);

    List<Ronda> getRondas();

    void sumarPuntoDeRonda(Jugador jugador1);

    Integer guardarAccion(Jugador cantador, String accion, boolean b, Integer puntosEnJuego);

    List<Accion> getAcciones();

    void agregarPuntosEnJuegoManoActual(Integer puntosViejos);

    Jugador getGanadorDeManoActual();

    void setGanadorDeMano(Jugador jugador1);

    String getGanadorDeRondaPorNumero(int i);

    void jugarCarta(Long idMano, Long idCartaJugada, Integer idJugador);
}
