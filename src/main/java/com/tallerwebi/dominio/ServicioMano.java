package com.tallerwebi.dominio;

public interface ServicioMano {
    void empezar(Partida truco);
    void tirarCarta(Partida truco, Mano mano, Long idCarta, String nroJugador);


    void guardar(Mano mano);



    Mano reset(Partida truco);

    Mano obtenerManoPorId(Long id);

    Jugador saberQuienTiraAhora();

    Integer obtenerPuntosEnJuegoPorTruco();


    Jugador preguntar(Mano mano, String accion, Integer nroJugador);

    Integer obtenerPuntosEnJuegoDelEnvido();

    void determinarGanadorRonda(Partida truco, Mano mano);

    Jugador responder(Mano mano, String accion, String respuesta, Integer nroJugador);
}
