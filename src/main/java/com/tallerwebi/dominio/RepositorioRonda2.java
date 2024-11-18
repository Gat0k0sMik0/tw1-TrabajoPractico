package com.tallerwebi.dominio;

import java.util.List;

public interface RepositorioRonda2 {
    void guardar(Ronda ronda2);

    List<Ronda> obtenerRondasDeUnaMano(Long manoId);
}
