package com.tallerwebi.dominio;

import javax.transaction.Transactional;
import java.util.List;

public interface RepositorioEstadistica {
    @Transactional
    Estadistica obtenerEstadisticaDeJugador(Long idJugador);

    @Transactional
    void guardarEstadistica(Estadistica e);

    List<Estadistica> obtenerTodasLasEstadisticasDeUnJugador(Long id);

    @Transactional
    List<Estadistica> obtenerTopJugadoresPorVictorias(int limite);
}
