package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Jugador;
import com.tallerwebi.dominio.*;
import com.tallerwebi.dominio.excepcion.TrucoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class ServicioPartidaImpl implements ServicioPartida {

    RepositorioTruco repositorioTruco;
    RepositorioMano repositorioMano;
    RepositorioCarta repositorioCarta;

    @Autowired
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
    public void guardarJugador(Jugador jugador) {
        repositorioTruco.guardarJugador(jugador);
    }

    @Override
    public List<Partida> getTodasLasPartidas() {
        return this.repositorioTruco.getTodasLasPartidas();
    }






    @Override
    public void finalizarPartida(Long idPartida, Jugador ganador) {
        // Obtener la partida
        Partida partida = repositorioTruco.buscarPartidaPorId(idPartida);

        // Validar la existencia de la partida
        if (partida == null) {
            throw new TrucoException("La partida no existe.");
        }

        // Verificar si ya tiene un ganador
        if (partida.isPartidaFinalizada()) {
            throw new TrucoException("La partida ya ha finalizado.");
        }

        // Determinar el ganador
        partida.setGanador(ganador);
        registrarVictoria(ganador);

        // Guardar los cambios
        repositorioTruco.merge(partida);
}

    // Método para registrar la victoria de un jugador
    private void registrarVictoria(Jugador ganador) {
        ganador.getUsuario().setVictorias(ganador.getUsuario().getVictorias() + 1); // Incrementa las victorias
        actualizarNivel(ganador.getUsuario()); // Actualiza el nivel según las victorias
    }

    // Método para actualizar el nivel según las victorias
    private void actualizarNivel(Usuario jugador) {
        if (jugador.getVictorias() >= 30) {
            jugador.setNivel("Oro");
        } else if (jugador.getVictorias() >= 20) {
            jugador.setNivel("Plata");
        } else {
            jugador.setNivel("Bronce");
        }
    }
}
