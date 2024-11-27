package com.tallerwebi.dominio;

public interface ServicioMano {
    Mano empezar(Truco2 truco, Jugador j1, Jugador j2 );

    Ronda tirarCarta(Truco2 truco, Mano mano, Long idCarta, String nroJugador);

    Mano reset(Truco2 truco);

    Mano obtenerManoPorId(Long id);

    Integer obtenerPuntosEnJuegoPorTruco();

    Integer obtenerMovimientosDeLaMano(Mano mano);

    Jugador preguntar(Mano mano, String accion, Jugador ejecutor, Jugador receptor);

    Integer obtenerPuntosEnJuegoDelEnvido();

    Jugador responder(Truco2 truco, String accion, String respuesta, Jugador ejecutor, Jugador receptor);
}
