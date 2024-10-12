package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class ServicioTrucoImpl implements ServicioTruco {

    final Truco truco;
    private Jugador jugador;
    private List<Carta> mazo;
    final List<Carta> cartasJugadas;
    final RepositorioCarta repositorioCarta;

    @Autowired
    public ServicioTrucoImpl(Truco truco,  RepositorioCarta repositorioCarta) {
        this.truco = truco;
        this.cartasJugadas = new ArrayList<>();
        this.repositorioCarta = repositorioCarta;
    }

    @Override
    public void empezar(Jugador j1, Jugador j2) {
        truco.cargarCartasAlMazo();
        List<Carta> seisAleatorias = truco.getMazo().getSeisCartasAleatoriasSinRepetir();
        truco.getMazo().asignarCartasAJugadores(j1,j2,seisAleatorias);
    }

        public Carta tirarCarta(Jugador jugador, Long idCartaSeleccionada) {
            Carta cartaTirada = jugador.tirarCarta(repositorioCarta.buscarCartaPorId(idCartaSeleccionada));
            return cartaTirada;
    }

        public List<Carta> getCartasJugadas() {
            return cartasJugadas;
        }
    }



