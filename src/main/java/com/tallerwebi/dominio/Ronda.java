package com.tallerwebi.dominio;

import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.List;

public class Ronda {

    private Integer nroRonda;
    private Jugador jugador;
    private Integer valorCarta;
    private Integer nroCarta;
    private String palo;

    @ManyToOne
    private Mano mano;

    @OneToMany
    private List<Carta> cartasJ1;
    @OneToMany
    private List<Carta> cartasJ2;

    @OneToMany
    private List<Carta> cartasTiradasJ1;
    @OneToMany
    private List<Carta> cartasTiradasJ2;



    public Integer getNroRonda() {
        return nroRonda;
    }

    public void setNroRonda(Integer nroRonda) {
        this.nroRonda = nroRonda;
    }

    public Jugador getJugador() {
        return jugador;
    }

    public void setJugador(Jugador jugador) {
        this.jugador = jugador;
    }

    public Integer getValorCarta() {
        return valorCarta;
    }

    public void setValorCarta(Integer valorCarta) {
        this.valorCarta = valorCarta;
    }

    public Integer getNroCarta() {
        return nroCarta;
    }

    public void setNroCarta(Integer nroCarta) {
        this.nroCarta = nroCarta;
    }

    public String getPalo() {
        return palo;
    }

    public void setPalo(String palo) {
        this.palo = palo;
    }
}