package com.tallerwebi.dominio;

import javax.transaction.Transactional;
import java.util.List;

public interface RepositorioSalaDeEspera {
    void guardar(SalaDeEspera salaDeEspera);
    List<SalaDeEspera> obtenerSalas();
    SalaDeEspera obtenerSalaPorId(Long id);
    void actualizar(SalaDeEspera sala);
}
