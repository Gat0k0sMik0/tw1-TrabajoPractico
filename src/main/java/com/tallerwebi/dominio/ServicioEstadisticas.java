package com.tallerwebi.dominio;


import java.util.List;

public interface ServicioEstadisticas {
    void guardarResultado(Partida truco);

    List<Estadistica> obtenerEstadisticasDeUnJugador(Long id);


    void agregarEstadisticasFicticias(Usuario usuario);
}
