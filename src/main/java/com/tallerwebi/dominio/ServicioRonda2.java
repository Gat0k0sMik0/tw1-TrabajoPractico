package com.tallerwebi.dominio;

public interface ServicioRonda2 {
    Ronda2 empezar(Mano2 mano);
    void registrarRonda(Mano2 mano, Ronda2 ronda);
    Ronda2 obtenerRondaPorId(Long id);
}
