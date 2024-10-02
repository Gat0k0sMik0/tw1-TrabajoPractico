package com.tallerwebi.dominio;

public class Mano {

    private Jugador jugador;
    private Integer puntos = 0;

    public Mano (Jugador jugador1, Integer puntos) {
        this.jugador = jugador1;
        this.puntos += puntos;
    }

    public Jugador getJugador() {
        return jugador;
    }

    public void setJugador(Jugador jugador) {
        this.jugador = jugador;
    }

    public Integer getPuntos() {
        return puntos;
    }

    public void setPuntos(Integer puntos) {
        this.puntos = puntos;
    }
}
