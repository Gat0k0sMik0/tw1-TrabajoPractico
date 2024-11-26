package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Jugador;
import com.tallerwebi.dominio.*;
import com.tallerwebi.dominio.excepcion.TrucoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

@Service
public class ServicioPartida2Impl implements ServicioPartida2 {

    @Autowired
    RepositorioTruco repositorioTruco;
    @Autowired
    RepositorioMano repositorioMano;
    @Autowired
    RepositorioCarta repositorioCarta;

    private Integer movimientos;
    private Integer puntosJ1;
    private Integer puntosJ2;
    private Jugador j1;
    private Jugador j2;


    public ServicioPartida2Impl(RepositorioTruco repositorioTruco, RepositorioCarta repositorioCarta, RepositorioMano repositorioMano) {
        this.repositorioTruco = repositorioTruco;
        this.repositorioCarta = repositorioCarta;
        this.repositorioMano = repositorioMano;
        this.j1 = null;
        this.j2 = null;
        this.movimientos = 0;
        this.puntosJ1 = 0;
        this.puntosJ2 = 0;
    }


    @Transactional
    @Override
    public Truco2 obtenerPartidaPorId(Long id) {
        Truco2 partida = this.repositorioTruco.buscarPartidaPorId(id);
        Jugador j1 = this.repositorioTruco.obtenerJugadorPorID(partida.getJ1().getId());
        Jugador j2 = this.repositorioTruco.obtenerJugadorPorID(partida.getJ2().getId());

        this.j1 = partida.getJ1();
        this.j2 = partida.getJ2();
        this.puntosJ1 = partida.getPuntosJ1();
        this.puntosJ2 = partida.getPuntosJ2();
//        this.cartasJ1.addAll(cartasJ1);
//        this.cartasJ2.addAll(cartasJ2);
//        this.j1.setCartas(j1.getCartas());
//        this.j2.setCartas(j2.getCartas());

        return partida;
    }

    @Override
    @Transactional
    public Truco2 empezar(Jugador j1, Jugador j2) {
        Truco2 truco = new Truco2();

        truco.setJ1(j1);
        truco.setJ2(j2);
        truco.setPuntosParaGanar(0);
        truco.setPuntosJ1(0);
        truco.setPuntosJ2(0);

        j1.getPartidasComoJ1().add(truco);
        j2.getPartidasComoJ2().add(truco);

        // Guardar partida en la base de datos
        this.repositorioTruco.guardarJugador(j1);
        this.repositorioTruco.guardarJugador(j2);
        this.repositorioTruco.guardarPartida(truco);

        System.out.println(truco);
        return truco;
    }

    @Override
    public void reset(Jugador j1, Jugador j2) {
        this.vaciarCartasDeJugadores(j1, j2);
//        this.asignarCartasJugadores(j1, j2);
    }

    private void vaciarCartasDeJugadores(Jugador j1, Jugador j2) {
//        j1.getCartas().clear();
//        j2.getCartas().clear();
    }


    @Override
    public void determinarGanadorRonda(Truco2 truco, Jugador jugador1, Jugador jugador2) {
//        if (truco == null) {
//            throw new TrucoException("La partida es nula.");
//        }
//        if (jugador1 == null) {
//            throw new TrucoException("El jugador 1 es nulo.");
//        }
//        if (jugador2 == null) {
//            throw new TrucoException("El jugador 2 es nulo.");
//        }
//
//        Jugador ganador = obtenerGanadorDeRonda(jugador1, jugador2);
//        if (ganador != null) {
//            if (ganador.getNombre().equals(jugador1.getNombre())) {
//                puntosJ1++;
//                jugador1.setPuntosPartida(jugador1.getPuntosPartida() + 1);
//                truco.setPuntosJ1(jugador1.getPuntosPartida());
//            } else {
//                puntosJ2++;
//                jugador2.setPuntosPartida(jugador2.getPuntosPartida() + 1);
//                truco.setPuntosJ2(jugador2.getPuntosPartida());
//            }
//            this.repositorioTruco.guardarPartida(truco);
//        }
    }

    @Override
    public void guardarJugador(Jugador jugador) {
        repositorioTruco.guardarJugador(jugador);
    }


//    private Jugador obtenerGanadorDeRonda(Jugador jugador1, Jugador jugador2) {
//        // Si ya tiraron los 2
//        if (this.cartasTiradasJ1.size() == this.cartasTiradasJ2.size()) {
//            // Suponiendo que tienes una lista de cartas jugadas por cada jugador
//            List<Carta> cartasJugador1 = this.cartasJ1;
//            List<Carta> cartasJugador2 = this.cartasJ2;
//
//            // Verificar que las listas no estén vacías y que los índices sean válidos
//            if (cartasJugador1.isEmpty() || cartasJugador2.isEmpty()) {
//                throw new TrucoException("No hay cartas tiradas por uno de los jugadores.");
//            }
//
//            // Conseguimos las últimas tiradas.
//            Carta cartaJ1 = this.cartasTiradasJ1.get(this.cartasTiradasJ2.size() - 1);
//            Carta cartaJ2 = this.cartasTiradasJ2.get(this.cartasTiradasJ1.size() - 1);
//
//            // Comparamos quien tiró la más alta, en base a eso damos poder
//            if (cartaJ1.getValor() > cartaJ2.getValor()) {
//                jugador1.setPuntosRonda(jugador1.getPuntosRonda() + 1);
//                return jugador1;
//            } else if (cartaJ2.getValor() > cartaJ1.getValor()) {
//                jugador2.setPuntosRonda(jugador2.getPuntosRonda() + 1);
//                return jugador2;
//            }
//        }
//        return null;
//    }





}
