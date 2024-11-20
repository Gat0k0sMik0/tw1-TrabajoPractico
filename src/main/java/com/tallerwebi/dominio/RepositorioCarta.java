package com.tallerwebi.dominio;

import java.util.List;

public interface RepositorioCarta {
    List<Carta> obtenerCartas();

    Carta buscarCartaPorId(Long id);

    List<Carta> obtenerCartasDeJugadorPorId(Long idJugador);

    void guardarCarta(Carta c);

    void guardarVariasCartas(List<Carta> c);
}
