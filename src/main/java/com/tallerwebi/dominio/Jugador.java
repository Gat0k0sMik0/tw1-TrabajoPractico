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

    @OneToMany(mappedBy = "jugador", cascade = CascadeType.ALL)
    private List<Carta> cartasTiradas = new ArrayList<>();

    //    private List<Integer> puntos;
//    private Integer puntos;


    public Jugador() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Carta tirarCarta(Carta carta) {
        boolean existeLaCartaEnCartas = cartas.contains(carta);
        if (existeLaCartaEnCartas) {
            cartas.remove(carta);
            cartasTiradas.add(carta);
            return carta;
        } else return null;
    }

    public Integer getPuntosPartida() {
        return this.puntosPartida;
    }

    public void setPuntosPartida(Integer puntosPartida) {
        this.puntosPartida = puntosPartida;
    }

    public void acumularPuntosDePartida(Integer puntos) {
        this.puntosPartida += puntos;
    }

//        public List<Integer> getPuntos () {
//            return puntos;
//        }

//        public void setPuntos (List < Integer > puntos) {
//            this.puntos = puntos;
//        }

    public List<Carta> getCartasTiradas() {
        return cartasTiradas;
    }

    public void setCartasTiradas(List<Carta> cartasTiradas) {
        this.cartasTiradas = cartasTiradas;
    }


    public List<Carta> getCartas() {
        return cartas;
    }

    public void setCartas(List<Carta> cartas) {
        this.cartas = cartas;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    private int getIndiceDeCartaBuscada(Carta carta) {
        return cartas.indexOf(carta);

    }

    public Boolean saberSiExiste(Carta carta) {
        return cartas.contains(carta);
    }


    // Añadir carta a la mano
    public void recibirCarta(Carta carta) {
        this.cartas.add(carta);
    }

    public void agregarPuntoRonda() {
        this.puntosRonda++;
    }

    public Integer getPuntosRonda() {
        return puntosRonda;
    }

    public void setPuntosRonda(Integer puntosRonda) {
        this.puntosRonda = puntosRonda;
    }


    public void ganarPuntosPorIrseAlMazo() {
        this.puntosPartida += 2;
        this.cartasTiradas.clear();
        this.cartas.clear();
    }

    public void agregarCarta(Carta carta) {
        System.out.println("Carta agregada al jugador.");
        this.cartas.add(carta);
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
                ", truco=" + truco +
                ", nombre='" + nombre + '\'' +
                ", puntosRonda=" + puntosRonda +
                ", puntosPartida=" + puntosPartida +
                ", numero=" + numero +
                ", cartas=" + cartas +
                ", cartasTiradas=" + cartasTiradas +
                '}';
    }
}