package com.tallerwebi.dominio;

import javax.persistence.*;

@Entity
public class Ronda2 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer nroRonda;

    private String nombreJugador;

    private Integer valorCarta;
    private Integer nroCarta;
    private String paloCarta;

    @ManyToOne
    @JoinColumn(name = "mano_id")
    private Mano2 mano;  // Relación muchos a uno con Mano

    public Ronda2() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNroRonda() {
        return nroRonda;
    }

    public void setNroRonda(Integer nroMano) {
        this.nroRonda = nroMano;
    }

    public String getNombreJugador() {
        return nombreJugador;
    }

    public void setNombreJugador(String nombreJugador) {
        this.nombreJugador = nombreJugador;
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

    public String getPaloCarta() {
        return paloCarta;
    }

    public void setPaloCarta(String paloCarta) {
        this.paloCarta = paloCarta;
    }

    public Mano2 getMano() {
        return mano;
    }

    public void setMano(Mano2 mano) {
        this.mano = mano;
    }
}