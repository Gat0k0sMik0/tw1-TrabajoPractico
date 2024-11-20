package com.tallerwebi.dominio;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Jugador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Truco2 truco;

    private String nombre;
    private Integer puntosRonda = 0;
    private Integer puntosPartida = 0;
    private Integer numero;

    @OneToMany(mappedBy = "jugador", cascade = CascadeType.ALL)
    private List<Carta> cartas;  // Relación uno a muchos con Carta

    @OneToOne
    private Truco2 partida;

//    @OneToMany(mappedBy = "jugador", cascade = CascadeType.ALL)
//    private List<Carta> cartasTiradas = new ArrayList<>();

    public Jugador() {

    }

    public Truco2 getPartida() {
        return partida;
    }

    public void setPartida(Truco2 partida) {
        this.partida = partida;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Truco2 getTruco() {
        return truco;
    }

    public void setTruco(Truco2 truco) {
        this.truco = truco;
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

    public List<Carta> getCartas() {
        return cartas;
    }

    public void setCartas(List<Carta> cartas) {
        this.cartas = cartas;
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