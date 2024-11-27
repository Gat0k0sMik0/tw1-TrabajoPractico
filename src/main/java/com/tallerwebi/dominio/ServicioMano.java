package com.tallerwebi.dominio;

public interface ServicioMano {
    Mano empezar(Partida truco, Jugador j1, Jugador j2 );

    Ronda tirarCarta(Partida truco, Mano mano, Long idCarta, String nroJugador);

    Mano reset(Partida truco);

    Mano obtenerManoPorId(Long id);

    Jugador saberQuienTiraAhora();

    Integer obtenerPuntosEnJuegoPorTruco();

    Integer obtenerMovimientosDeLaMano(Mano mano);

    Jugador preguntar(Mano mano, String accion, Jugador ejecutor, Jugador receptor);

    Integer obtenerPuntosEnJuegoDelEnvido();

    void determinarGanadorRonda(Partida truco, Mano mano);

    Jugador responder(Partida truco, String accion, String respuesta, Jugador ejecutor, Jugador receptor);
}
