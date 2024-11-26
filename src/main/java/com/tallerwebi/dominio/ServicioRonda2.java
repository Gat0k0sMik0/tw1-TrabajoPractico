package com.tallerwebi.dominio;

public interface ServicioRonda2 {
    Ronda empezar(Mano mano);
    void registrarRonda(Mano mano, Ronda ronda);
    Ronda obtenerRondaPorId(Long id);
    void reset();
}
