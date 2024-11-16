package com.tallerwebi.dominio;

public interface RepositorioMano {
    void guardar(Mano2 mano);
    Mano2 obtenerManoPorId(Long id);
}
