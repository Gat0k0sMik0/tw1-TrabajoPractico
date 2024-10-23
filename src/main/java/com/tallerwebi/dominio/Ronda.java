package com.tallerwebi.dominio;

public class Ronda {

    private Integer nroRonda;
    private Jugador jugador;
    private Integer valorCarta;
    private Integer nroCarta;
    private String palo;

    public Ronda(Integer nroRonda, Jugador jugador, Carta carta) {
        this.nroRonda = nroRonda;
        this.jugador = jugador;
        this.valorCarta = carta.getValor();
        this.nroCarta = carta.getNumero();
        this.palo = carta.getPalo();
    }

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