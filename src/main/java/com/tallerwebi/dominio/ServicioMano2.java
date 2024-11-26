package com.tallerwebi.dominio;

public interface ServicioMano2 {
    Mano2 empezar(Truco2 truco, Jugador j1, Jugador j2 );

    Ronda tirarCarta(Mano2 mano, Jugador jugador, Long idCarta, String nroJugador);

    Mano2 reset(Truco2 truco);

    Mano2 obtenerManoPorId(Long id);

    Integer obtenerPuntosEnJuegoPorTruco();

    Integer obtenerMovimientosDeLaMano(Mano2 mano);

    Jugador preguntar(Mano2 mano, String accion, Jugador ejecutor, Jugador receptor);

    Integer obtenerPuntosEnJuegoDelEnvido();

    Jugador responder(Truco2 truco, String accion, String respuesta, Jugador ejecutor, Jugador receptor);
}
