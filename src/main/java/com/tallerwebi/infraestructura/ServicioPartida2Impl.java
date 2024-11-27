package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Jugador;
import com.tallerwebi.dominio.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class ServicioPartida2Impl implements ServicioPartida2 {

    @Autowired
    RepositorioTruco repositorioTruco;
    @Autowired
    RepositorioMano repositorioMano;
    @Autowired
    RepositorioCarta repositorioCarta;


    public ServicioPartida2Impl(RepositorioTruco repositorioTruco, RepositorioCarta repositorioCarta, RepositorioMano repositorioMano) {
        this.repositorioTruco = repositorioTruco;
        this.repositorioCarta = repositorioCarta;
        this.repositorioMano = repositorioMano;
    }


    @Transactional
    @Override
    public Truco2 obtenerPartidaPorId(Long id) {
        return this.repositorioTruco.buscarPartidaPorId(id);
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

        // Guardar partida en la base de datos
        this.repositorioTruco.guardarJugador(j1);
        this.repositorioTruco.guardarJugador(j2);
        this.repositorioTruco.guardarPartida(truco);

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
    public void guardarJugador(Jugador jugador) {
        repositorioTruco.guardarJugador(jugador);
    }



}
