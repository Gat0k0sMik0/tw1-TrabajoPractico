package com.tallerwebi.dominio;


import java.util.List;

public interface ServicioEstadisticas {
    void guardarResultado(Partida truco);

    void agregarEstadisticasFicticias(Usuario usuario);

    Estadistica obtenerEstadisticasDeUnJugador(Usuario usuario);

    List<Estadistica> obtenerTopJugadores();
}
