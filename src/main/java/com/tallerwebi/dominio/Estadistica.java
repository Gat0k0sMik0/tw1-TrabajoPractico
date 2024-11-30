package com.tallerwebi.dominio;

import javax.persistence.*;

@Entity
public class Estadistica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer ganadas;
    private Integer jugadas;
    private String juego;

    @ManyToOne
    private Jugador jugador;

    public Estadistica() {
    }

    public Estadistica(Integer ganadas, Integer jugadas, Jugador jugador, String juego) {
        this.ganadas = ganadas;
        this.jugadas = jugadas;
        this.jugador = jugador;
        this.juego = juego;
    }

    public String getJuego() {
        return juego;
    }

    public void setJuego(String juego) {
        this.juego = juego;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getGanadas() {
        return ganadas;
    }

    public void setGanadas(Integer ganadas) {
        this.ganadas = ganadas;
    }

    public Integer getJugadas() {
        return jugadas;
    }

    public void setJugadas(Integer jugadas) {
        this.jugadas = jugadas;
    }

    public Jugador getJugador() {
        return jugador;
    }

    public void setJugador(Jugador jugador) {
        this.jugador = jugador;
    }
}
