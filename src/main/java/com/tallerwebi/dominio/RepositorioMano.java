package com.tallerwebi.dominio;

public interface RepositorioMano {
    void guardar(Mano mano);
    Mano obtenerUltimaMano(Long idPartida);

    Mano obtenerManoPorId(Long id);
}
