package com.tallerwebi.dominio;

public interface ServicioMano {
    Mano empezar(Partida truco, Jugador j1, Jugador j2 );

    void guardar(Mano mano);

    void tirarCarta(Partida truco, Mano mano, Long idCarta, String nroJugador);

    Mano reset(Partida truco);

    Mano obtenerManoPorId(Long id);

    Jugador saberQuienTiraAhora();

    Integer obtenerPuntosEnJuegoPorTruco();


    Jugador preguntar(Mano mano, String accion, Integer nroJugador);

    Integer obtenerPuntosEnJuegoDelEnvido();

    void determinarGanadorRonda(Partida truco, Mano mano);

    Jugador responder(Mano mano, String accion, String respuesta, Integer nroJugador);
}
