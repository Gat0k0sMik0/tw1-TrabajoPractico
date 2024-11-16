package com.tallerwebi.dominio;

public interface ServicioRonda2 {
    Ronda2 empezar(Mano2 mano);
    void registrarRonda(Mano2 mano, Jugador jugador, Carta carta);
}
