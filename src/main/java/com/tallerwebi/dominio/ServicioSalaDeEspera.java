package com.tallerwebi.dominio;

import java.util.List;

public interface ServicioSalaDeEspera {
    List<SalaDeEspera> obtenerSalas();
    void guardarSala(SalaDeEspera salaDeEspera);
    SalaDeEspera obtenerSalaPorId(Long idSala);
}
