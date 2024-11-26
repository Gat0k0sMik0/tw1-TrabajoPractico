package com.tallerwebi.dominio;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Jugador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private Integer puntosRonda = 0;
    private Integer puntosPartida = 0;
    private Integer numero;

    @OneToMany(mappedBy = "j1", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Truco2> partidasComoJ1 = new ArrayList<>();

    @OneToMany(mappedBy = "j2", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Truco2> partidasComoJ2 = new ArrayList<>();

    public Jugador() {

    }

    public List<Truco2> getPartidasComoJ1() {
        return partidasComoJ1;
    }

    public void setPartidasComoJ1(List<Truco2> partidasComoJ1) {
        this.partidasComoJ1 = partidasComoJ1;
    }

    public List<Truco2> getPartidasComoJ2() {
        return partidasComoJ2;
    }

    public void setPartidasComoJ2(List<Truco2> partidasComoJ2) {
        this.partidasComoJ2 = partidasComoJ2;
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