package com.tallerwebi.dominio;

public interface ServicioRonda2 {
    Ronda empezar(Mano2 mano);

    void reset();

    void registrarRonda(Mano2 mano, Ronda ronda);
}
