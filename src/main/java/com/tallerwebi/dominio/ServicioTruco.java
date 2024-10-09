package com.tallerwebi.dominio;

import java.util.List;

public interface ServicioTruco {
    void empezar(Jugador j1, Jugador j2);
    void tirarCarta(Jugador jugador, Carta cartaSeleccionada);
    List<Carta> getCartasJugadas();

}
