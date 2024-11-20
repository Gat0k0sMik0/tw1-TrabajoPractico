package com.tallerwebi.dominio;

public interface ServicioRonda2 {
    Ronda empezar(Mano2 mano);
    void registrarRonda(Mano2 mano, Ronda ronda);
    Ronda obtenerRondaPorId(Long id);
    void reset();
}
