package com.tallerwebi.dominio;

import java.util.ArrayList;
import java.util.List;

public class Jugador {
    private List<Carta> cartas = new ArrayList<>();
    private List<Carta> cartasTiradas = new ArrayList<>();
    private String nombre;
    private List<Integer> puntos;

    public Jugador(String nombre) {
        this.nombre = nombre;
    }

    public List<Integer> getPuntos() {
        return puntos;
    }

    public void setPuntos(List<Integer> puntos) {
        this.puntos = puntos;
    }

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

    private int getIndiceDeCartaBuscada (Carta carta) {
        return cartas.indexOf(carta);

    }
    public Boolean saberSiExiste (Carta carta) {
        return cartas.contains(carta);
    }

    public Carta tirarCarta(Carta carta) {
        boolean existeLaCartaEnCartas = cartas.contains(carta);
        if (existeLaCartaEnCartas) {
            cartas.remove(carta);
            cartasTiradas.add(carta);
            return carta;
        } else return null;
    }

    // Añadir carta a la mano
    public void recibirCarta(Carta carta) {
        this.cartas.add(carta);
    }
}