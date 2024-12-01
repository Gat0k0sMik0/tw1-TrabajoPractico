package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.*;
import com.tallerwebi.dominio.excepcion.TrucoException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service("servicioEstadisticas")
@Transactional
public class ServicioEstadisticasImpl implements ServicioEstadisticas {

    private RepositorioEstadistica repositorioEstadistica;
    private RepositorioTruco repositorioTruco;

    @Autowired
    public ServicioEstadisticasImpl(RepositorioEstadistica repositorioEstadistica, RepositorioTruco repositorioTruco) {
        this.repositorioEstadistica = repositorioEstadistica;
        this.repositorioTruco = repositorioTruco;
    }

    @Override
    public void guardarResultado(Partida truco) {
        if (truco == null) throw new TrucoException("No se puede guardar el resultado");

        Estadistica estadisticaJ1 = this.repositorioEstadistica.obtenerEstadisticaDeJugador(truco.getJ1().getId());
        Estadistica estadisticaJ2 = this.repositorioEstadistica.obtenerEstadisticaDeJugador(truco.getJ2().getId());

        if (estadisticaJ1 == null) estadisticaJ1 = crearEstadisticaParaJugador(truco, 1);
        if (estadisticaJ2 == null) estadisticaJ2 = crearEstadisticaParaJugador(truco, 2);

        if (truco.getGanador().getNumero().equals(truco.getJ1().getNumero())) {
            // gano J1
            estadisticaJ1.setGanadas(estadisticaJ1.getGanadas() + 1);
        } else if (truco.getGanador().getNumero().equals(truco.getJ2().getNumero())) {
            // gano J2
            estadisticaJ2.setGanadas((estadisticaJ2.getGanadas() + 1));
        } else {
            throw new TrucoException("El ganador no fue ni el J1 ni el J2");
        }

        estadisticaJ1.setJugadas(estadisticaJ1.getJugadas() + 1);
        estadisticaJ2.setJugadas(estadisticaJ2.getJugadas() + 1);

        this.repositorioEstadistica.guardarEstadistica(estadisticaJ1);
        this.repositorioEstadistica.guardarEstadistica(estadisticaJ2);
    }

    @Override
    public List<Estadistica> obtenerEstadisticasDeUnJugador(Long id) {
        System.out.println("Buscando estadisticas de jugador ID: " + id);
        return this.repositorioEstadistica.obtenerTodasLasEstadisticasDeUnJugador(id);
    }

    @Override
    public void agregarEstadisticasFicticias(Usuario usuario) {
        Jugador j = new Jugador();
        j.setNombre(usuario.getNombreUsuario());
        j.setUsuario(usuario);

        this.repositorioTruco.guardarJugador(j);


        Estadistica e1 = new Estadistica();
        e1.setJuego("Truco");
        e1.setJugador(j);
        e1.setGanadas(25);
        e1.setJugadas(80);
        Estadistica e2 = new Estadistica();
        e2.setJuego("Otro juego 1");
        e2.setJugador(j);
        e2.setGanadas(5);
        e2.setJugadas(80);
        Estadistica e3 = new Estadistica();
        e3.setJuego("Otro juego 2");
        e3.setJugador(j);
        e3.setGanadas(60);
        e3.setJugadas(80);


        this.repositorioEstadistica.guardarEstadistica(e1);
        this.repositorioEstadistica.guardarEstadistica(e2);
        this.repositorioEstadistica.guardarEstadistica(e3);

    }

    private Estadistica crearEstadisticaParaJugador(Partida truco, Integer nroJugador) {
        Estadistica e = new Estadistica();
        e.setJugador(nroJugador.equals(1) ? truco.getJ1() : truco.getJ2());
        e.setGanadas(0);
        e.setJugadas(0);
        e.setJuego("Truco");
        return e;
    }

}
