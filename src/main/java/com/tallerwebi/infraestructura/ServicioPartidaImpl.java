package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Jugador;
import com.tallerwebi.dominio.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class ServicioPartidaImpl implements ServicioPartida {

    @Autowired
    RepositorioTruco repositorioTruco;
    @Autowired
    RepositorioMano repositorioMano;
    @Autowired
    RepositorioCarta repositorioCarta;


    public ServicioPartidaImpl(RepositorioTruco repositorioTruco, RepositorioCarta repositorioCarta, RepositorioMano repositorioMano) {
        this.repositorioTruco = repositorioTruco;
        this.repositorioCarta = repositorioCarta;
        this.repositorioMano = repositorioMano;
    }


    @Transactional
    @Override
    public Partida obtenerPartidaPorId(Long id) {
        return this.repositorioTruco.buscarPartidaPorId(id);
    }

    @Override
    public Partida preparar(Jugador j1, Integer puntosMaximos) {
        Partida truco = new Partida();
        truco.setPuedeEmpezar(false);
        truco.setJ1(j1);
        truco.setPuntosParaGanar(puntosMaximos);
        truco.setPuntosJ1(0);
        truco.setJ2(null);
        truco.setPuntosJ2(0);
        this.repositorioTruco.guardarJugador(j1);
        this.repositorioTruco.guardarPartida(truco);
        return truco;
    }

    @Override
    public void agregarJugador(Jugador j2, Partida truco) {
        truco.setJ2(j2);
        truco.setPuntosJ2(0);
        this.repositorioTruco.guardarJugador(j2);
        this.repositorioTruco.merge(truco);
    }


    @Override
    public List<Partida> getPartidasDisponibles() {
        return this.repositorioTruco.getPartidasDisponibles();
    }


    @Override
    public void empezar(Partida truco) {
        truco.setPuedeEmpezar(true);
        this.repositorioTruco.guardarPartida(truco);
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
    public void guardarJugador(Jugador jugador) {
        repositorioTruco.guardarJugador(jugador);
    }

    @Override
    public List<Partida> getTodasLasPartidas() {
        return this.repositorioTruco.getTodasLasPartidas();
    }


}
