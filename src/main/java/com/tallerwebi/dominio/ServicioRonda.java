package com.tallerwebi.dominio;

import java.util.List;

public interface ServicioRonda {
    void empezar();
    List<Ronda> getRondas();
    void crearRonda(Jugador jugador, Carta carta);

    String getGanadorDeRondaPorNumero(int i);
}
