package com.tallerwebi.dominio;

import java.util.List;

public interface ServicioPartida {

    Partida preparar(Jugador j1, Integer puntosMaximos);
    void agregarJugador(Jugador j2, Partida partida);
    void empezar(Partida truco);

    Partida obtenerPartidaPorId(Long id);


    List<Partida> getPartidasDisponibles();


    void reset(Jugador j1, Jugador j2);

    void guardarJugador(Jugador jugador1);

    List<Partida> getTodasLasPartidas();

    ;
}
