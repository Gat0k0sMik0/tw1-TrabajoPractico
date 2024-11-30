package com.tallerwebi.dominio;

import javax.transaction.Transactional;
import java.util.List;

public interface ServicioSalaDeEspera {
    List<SalaDeEspera> obtenerSalas();
    void guardarSala(SalaDeEspera salaDeEspera);
    SalaDeEspera obtenerSalaPorId(Long idSala);
    void actualizarSala(SalaDeEspera sala);

    Jugador obtenerJugadorPorId(Long id);
}
