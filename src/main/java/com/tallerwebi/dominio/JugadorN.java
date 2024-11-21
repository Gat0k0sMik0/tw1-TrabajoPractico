/*package com.tallerwebi.dominio;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class JugadorN {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    private Integer puntosPartida = 0;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Mano> manos;

    @OneToMany
    private List<Carta> cartas = new ArrayList<>();
    @OneToMany

    private List<Carta> cartasTiradas = new ArrayList<>();

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public List<Carta> getCartas() {
        return cartas;
    }

    public void setCartas(List<Carta> cartas) {
        this.cartas = cartas;
    }

    public List<Carta> getCartasTiradas() {
        return cartasTiradas;
    }

    public void setCartasTiradas(List<Carta> cartasTiradas) {
        this.cartasTiradas = cartasTiradas;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getPuntosMano() {
        return puntosMano;
    }

    public void setPuntosMano(Integer puntosMano) {
        this.puntosMano = puntosMano;
    }

    public Integer getPuntosPartida() {
        return puntosPartida;
    }

    public void setPuntosPartida(Integer puntosPartida) {
        this.puntosPartida = puntosPartida;
    }

    public Integer getPuntosRonda() {
        return puntosRonda;
    }

    public void setPuntosRonda(Integer puntosRonda) {
        this.puntosRonda = puntosRonda;
    }
}*/
