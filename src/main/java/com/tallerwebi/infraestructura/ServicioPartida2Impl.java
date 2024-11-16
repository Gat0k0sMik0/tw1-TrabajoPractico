package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Jugador;
import com.tallerwebi.dominio.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class ServicioPartida2Impl implements ServicioPartida2 {

    @Autowired
    RepositorioTruco repositorioTruco;
    @Autowired
    RepositorioCarta repositorioCarta;

    public ServicioPartida2Impl(RepositorioTruco repositorioTruco, RepositorioCarta repositorioCarta) {
        this.repositorioTruco = repositorioTruco;
        this.repositorioCarta = repositorioCarta;
    }

    @Override
    public Truco2 empezar(Jugador j1, Jugador j2) {
        Truco2 truco = new Truco2();
        truco.setJ1(j1);
        truco.setJ2(j2);
        this.asignarCartasJugadores(j1, j2);
        this.repositorioTruco.guardarPartida(truco);
        return truco;
    }

    @Override
    public Truco2 empezarTest(Jugador j1, Jugador j2) {
        Truco2 truco = new Truco2();
        truco.setJ1(j1);
        truco.setJ2(j2);
        return truco;
    }

    private void asignarCartasJugadores (Jugador j1, Jugador j2) {
        List<Carta> cartas = repositorioCarta.obtenerCartas();
        List<Carta> seisCartasRandom = obtenerSeisCartasRandom(cartas);
        asignarCartasJugador(j1, seisCartasRandom);
        asignarCartasJugador(j2, seisCartasRandom);
    }

    private void asignarCartasJugador(Jugador j, List<Carta> seisCartasRandom) {
        for (Carta carta : seisCartasRandom) {
            if (j.getCartas().size() < 3) {
                j.agregarCarta(carta);
                seisCartasRandom.remove(carta);
            } else break;
        }
    }

    private List<Carta> obtenerSeisCartasRandom(List<Carta> cartas) {
        System.out.println("Largo de cartas: " + cartas.size());
        List<Carta> cartasBuscadas = new ArrayList<>();
        int indice = 0;
        int random = 0;
        Random r = new Random();
        while (indice < 6) {
            random = r.nextInt(cartas.size());
            Carta cartaRandom = cartas.get(random);
            if (cartasBuscadas.isEmpty()) {
                cartasBuscadas.add(cartaRandom);
                indice++;
            } else {
                if (!saberSiLaCartaSeRepiteEnMazo(cartasBuscadas, cartaRandom.getNumero(), cartaRandom.getPalo())) {
                    cartasBuscadas.add(cartaRandom);
                    indice++;
                }
            }

        }
        return cartasBuscadas;
    }

    private Boolean saberSiLaCartaSeRepiteEnMazo(List<Carta> cartas, Integer numero, String palo) {
        for (Carta carta : cartas) {
            if (carta.getNumero().equals(numero) && carta.getPalo().equalsIgnoreCase(palo)) {
                return true;
            }
        }
        return false;
    }
}
