package com.tallerwebi.dominio;

import javax.persistence.*;

@Entity
public class Jugador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private Integer puntosRonda = 0;
    private Integer puntosPartida = 0;
    private Integer numero;

    public Jugador() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getPuntosRonda() {
        return puntosRonda;
    }

    public void setPuntosRonda(Integer puntosRonda) {
        this.puntosRonda = puntosRonda;
    }

    public Integer getPuntosPartida() {
        return puntosPartida;
    }

    public void setPuntosPartida(Integer puntosPartida) {
        this.puntosPartida = puntosPartida;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    @Override
    public String toString() {
        return "Jugador{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                '}';
    }

    //
//    public List<Carta> getCartasTiradas() {
//        return cartasTiradas;
//    }
//
//    public void setCartasTiradas(List<Carta> cartasTiradas) {
//        this.cartasTiradas = cartasTiradas;
}