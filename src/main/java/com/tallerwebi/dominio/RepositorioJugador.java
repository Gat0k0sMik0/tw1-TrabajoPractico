package com.tallerwebi.dominio;

public interface RepositorioJugador {
    void guardar(Jugador jugador);

    void modificar(Jugador jugador);

    Jugador buscarPorId(Long id);
}
