package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ServicioTrucoImpl implements ServicioTruco {

    private Truco truco;
    private Jugador jugador;
    private List<Carta> mazo;
    private List<Carta> cartasJugadas;
    private RepositorioCarta repositorioCarta;

    @Autowired
    public ServicioTrucoImpl(RepositorioCarta repositorioCarta) {
        this.repositorioCarta = repositorioCarta;
        this.cartasJugadas = new ArrayList<>();
    }

    @Override
    public void empezar(Jugador j1, Jugador j2) {
        this.truco = new Truco();
        this.truco.getMazo().setCartas(repositorioCarta.obtenerCartas());
        List<Carta> seisAleatorias = this.truco.getMazo().getSeisCartasAleatoriasSinRepetir();
        this.truco.getMazo().asignarCartasAJugadores(j1, j2, seisAleatorias);
    }

    // Método para que el jugador tire una carta
    public void tirarCarta(Jugador jugador, Carta cartaSeleccionada) {
        List<Carta> cartasJugador = jugador.getCartas();
        if (cartasJugador.contains(cartaSeleccionada)) {
            jugador.tirarCarta(cartaSeleccionada);
            truco.registrarMovimiento(jugador, cartaSeleccionada);
            cartasJugadas.add(cartaSeleccionada); // Agrega la carta a las cartas jugadas
        } else {
            throw new IllegalArgumentException("La carta seleccionada no está en la mano del jugador.");
        }
    }

    public List<Carta> getCartasJugadas(Jugador j) {
        return j.getCartasTiradas();
    }

    @Override
    public List<Ronda> getRondasJugadas() {
        return truco.getRondasJugadas();

    }
}



