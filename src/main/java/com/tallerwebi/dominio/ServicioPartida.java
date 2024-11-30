package com.tallerwebi.dominio;

import java.util.List;

import javax.transaction.Transactional;

public interface ServicioPartida {

    Partida preparar(Jugador j1, Integer puntosMaximos);
    void agregarJugador(Jugador j2, Partida partida);
    void empezar(Partida truco);

    Partida obtenerPartidaPorId(Long id);


    List<Partida> getPartidasDisponibles();


    void guardarJugador(Jugador jugador1);

    List<Partida> getTodasLasPartidas();

    void finalizarPartida(Long idPartida);

    ;
}
