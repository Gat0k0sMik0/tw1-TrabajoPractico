package com.tallerwebi.dominio;

import java.util.ArrayList;
import java.util.List;

public class Jugador {
    private List<Carta> cartas = new ArrayList<>();
    private String nombre;

    public Jugador(String nombre) {
        this.nombre = nombre;
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
            return carta;
        } else return null;
    }
}