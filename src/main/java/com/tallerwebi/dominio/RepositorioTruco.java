package com.tallerwebi.dominio;

import javax.transaction.Transactional;
import java.util.List;

public interface RepositorioTruco {
    void guardarPartida(Partida truco);

    @Transactional
    void merge(Partida truco);

    @Transactional
    Partida buscarPartidaPorId (Long id);

    @Transactional
    void guardarJugador(Jugador jugador);

    @Transactional
    Jugador obtenerJugadorPorID(Long id);

    @Transactional
    List<Partida> getPartidasDisponibles(Long idUsuario);

    @Transactional
    List<Partida> buscarPartidasNoTerminadas(Long idUsuario);

    List<Partida> getTodasLasPartidas();
}
