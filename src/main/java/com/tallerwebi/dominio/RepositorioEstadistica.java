package com.tallerwebi.dominio;

import javax.transaction.Transactional;
import java.util.List;

public interface RepositorioEstadistica {
    @Transactional
    Estadistica obtenerEstadisticaDeJugador(Long idJugador);

    @Transactional
    void guardarEstadistica(Estadistica e);

    @Transactional
    void actualizarEstadistica(Estadistica e);

    List<Estadistica> obtenerTodasLasEstadisticasDeUnJugador(Long id);

    @Transactional
    List<Estadistica> obtenerTopJugadoresPorVictorias(int limite);

    @Transactional
    List<Estadistica> obtenerTodasLasEstadisticas();

    @Transactional
    List<Estadistica> obtenerTop5JugadoresPorVictorias();
}
