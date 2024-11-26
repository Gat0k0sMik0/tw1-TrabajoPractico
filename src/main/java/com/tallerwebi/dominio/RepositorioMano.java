package com.tallerwebi.dominio;

public interface RepositorioMano {
    void guardar(Mano mano);
    Mano obtenerManoPorId(Long id);
}
