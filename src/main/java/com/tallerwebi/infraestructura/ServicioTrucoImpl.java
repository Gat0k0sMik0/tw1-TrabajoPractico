package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class ServicioTrucoImpl implements ServicioTruco {

    private Truco truco;
   // private PartidaTruco partida;
    private Jugador jugador;
   // private Robot robot;
    private List<Carta> mazo;
    private List<Carta> cartasJugadas;

    @Autowired
    public ServicioTrucoImpl(Truco truco) {
        this.truco = truco;
    }

    @Override
    public void empezar(Jugador j1, Jugador j2) {
        truco.cargarCartasAlMazo();
        List<Carta> seisAleatorias = truco.getMazo().getSeisCartasAleatoriasSinRepetir();
        truco.getMazo().asignarCartasAJugadores(j1,j2,seisAleatorias);
    }


    // Método para iniciar una partida
   /* public void iniciarPartida(String nombreJugador) {
        this.jugador = new Jugador(nombreJugador);
        this.robot = new Robot("Robot");

        // Repartir las cartas
        repartirCartas(jugador);
        repartirCartas(robot);
    }*/

    // Inicializa el mazo de cartas del Truco
    private void inicializarMazo() {
        mazo = new ArrayList<>();
        String[] palos = {"Espada", "Oro", "Basto", "Copa"};
        for (String palo : palos) {
            for (int numero = 1; numero <= 12; numero++) {
                if (numero != 8 && numero != 9) {
                    int valor = asignarValor(numero, palo);
                    mazo.add(new Carta(valor, numero, palo));
                }
            }
        }
        Collections.shuffle(mazo);
    }

    // Asigna el valor de la carta según las reglas del Truco
    private int asignarValor(int numero, String palo) {
        // Reglas específicas del Truco para asignar el valor
        if (numero == 1 && palo.equals("Espada")) return 14;
        if (numero == 1 && palo.equals("Basto")) return 13;
        if (numero == 7 && palo.equals("Espada")) return 12;
        if (numero == 7 && palo.equals("Oro")) return 11;
        if (numero == 3) return 10;
        if (numero == 2) return 9;
        if (numero == 1) return 8;
        if (numero == 12) return 7;
        if (numero == 11) return 6;
        if (numero == 10) return 5;
        if (numero == 7) return 4;
        if (numero == 6) return 3;
        if (numero == 5) return 2;
        if (numero == 4) return 1;

        return 0; // por defecto
    }

    // Repartir 3 cartas a un jugador
    private void repartirCartas(Jugador jugador) {
        for (int i = 0; i < 3; i++) {
            Carta carta = mazo.remove(0);
            jugador.recibirCarta(carta);
        }
    }

}
