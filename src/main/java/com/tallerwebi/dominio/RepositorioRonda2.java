package com.tallerwebi.dominio;

import java.util.List;

public interface RepositorioRonda2 {
    void guardar(Ronda2 ronda2);

    List<Ronda2> obtenerRondasDeUnaMano(Long manoId);
}
