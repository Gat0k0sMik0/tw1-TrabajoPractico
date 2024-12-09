package com.tallerwebi.dominio;

import java.util.List;

import javax.transaction.Transactional;

public interface ServicioPartida {

    Partida preparar(Usuario usuario, Integer puntosMaximos);
    void agregarJugador(Usuario usuario, Partida partida);
    void empezar(Partida truco);

    Partida obtenerPartidaPorId(Long id);


    List<Partida> getPartidasDisponibles();


    void guardarJugador(Jugador jugador1);

    List<Partida> getTodasLasPartidas();

    void finalizarPartida(Long idPartida, Jugador ganador);

    List<Partida> obtenerUltimas3PartidasDeUnJugador(Usuario usuario);

    Partida obtenerUltimaPartidaDeUnJugador(Usuario usuario);

    ;
}
