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
    private Jugador jugador;
    private List<Carta> mazo;
    private List<Carta> cartasJugadas;

    @Autowired
    public ServicioTrucoImpl(Truco truco) {
        this.truco = truco;
        this.cartasJugadas = new ArrayList<>();
    }

    @Override
    public void empezar(Jugador j1, Jugador j2) {
        truco.cargarCartasAlMazo();
        List<Carta> seisAleatorias = truco.getMazo().getSeisCartasAleatoriasSinRepetir();
        truco.getMazo().asignarCartasAJugadores(j1,j2,seisAleatorias);
    }


        // Método para que el jugador tire una carta
        public void tirarCarta(Jugador jugador, Carta cartaSeleccionada) {
            List<Carta> cartasJugador = jugador.getCartas();
            if (cartasJugador.contains(cartaSeleccionada)) {
                cartasJugador.remove(cartaSeleccionada); // Elimina la carta de la mano del jugador
                cartasJugadas.add(cartaSeleccionada); // Agrega la carta a las cartas jugadas
                System.out.println(jugador.getNombre() + " ha jugado la carta " + cartaSeleccionada.getNumero() + " de " + cartaSeleccionada.getPalo());
            } else {
                throw new IllegalArgumentException("La carta seleccionada no está en la mano del jugador.");
            }
        }

        public List<Carta> getCartasJugadas() {
            return cartasJugadas;
        }
    }



