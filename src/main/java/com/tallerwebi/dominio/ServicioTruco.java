package com.tallerwebi.dominio;

import java.util.List;

public interface ServicioTruco {
    void empezar(Jugador j1, Jugador j2);
    Carta tirarCarta(Jugador jugador, Long idCartaSeleccionada);
    List<Carta> getCartasJugadas();

}
