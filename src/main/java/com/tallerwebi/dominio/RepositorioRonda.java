package com.tallerwebi.dominio;

import java.util.List;

public interface RepositorioRonda {
    void guardar(Ronda ronda2);

    List<Ronda> obtenerRondasDeUnaMano(Long manoId);
}
