package com.tallerwebi.dominio;

public interface ServicioMano2 {
    Mano2 empezar(Truco2 truco);
    Mano2 obtenerManoPorId(Long id);

    Integer obtenerPuntosEnJuegoPorTruco();

    Jugador preguntar(Truco2 truco, String accion, Jugador ejecutor, Jugador receptor);

    Integer obtenerPuntosEnJuegoDelEnvido();

    Jugador responder(Truco2 truco, String accion, String respuesta, Jugador ejecutor, Jugador receptor);
}
