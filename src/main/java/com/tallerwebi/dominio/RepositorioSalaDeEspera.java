package com.tallerwebi.dominio;

import java.util.List;

public interface RepositorioSalaDeEspera {
    void guardar(SalaDeEspera salaDeEspera);

    List<SalaDeEspera> obtenerSalas();

    SalaDeEspera obtenerSalaPorId(Long id);
}
