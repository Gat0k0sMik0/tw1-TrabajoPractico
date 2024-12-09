package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.*;
import com.tallerwebi.dominio.excepcion.TrucoException;
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

        // Obtener estadísticas existentes
        Estadistica estadisticaJ1 = this.repositorioEstadistica.obtenerEstadisticaDeJugador(truco.getJ1().getUsuario().getId());
        Estadistica estadisticaJ2 = this.repositorioEstadistica.obtenerEstadisticaDeJugador(truco.getJ2().getUsuario().getId());

        // Crear estadísticas nuevas si no existen
        if (estadisticaJ1 == null) {
            estadisticaJ1 = crearEstadisticaParaUsuario(truco.getJ1().getUsuario());
        }
        if (estadisticaJ2 == null) {
            estadisticaJ2 = crearEstadisticaParaUsuario(truco.getJ2().getUsuario());
        }

        // Actualizar estadísticas según el ganador
        if (truco.getGanador() != null) {
            if (truco.getGanador().getNumero().equals(truco.getJ1().getNumero())) {
                estadisticaJ1.getUsuario().setVictorias(estadisticaJ1.getUsuario().getVictorias() + 1);
            } else if (truco.getGanador().getNumero().equals(truco.getJ2().getNumero())) {
                estadisticaJ2.getUsuario().setVictorias(estadisticaJ2.getUsuario().getVictorias() + 1);
            } else {
                throw new TrucoException("El ganador no fue ni el J1 ni el J2");
            }
        }

        // Incrementar partidas jugadas
        estadisticaJ1.setJugadas(estadisticaJ1.getJugadas() + 1);
        estadisticaJ2.setJugadas(estadisticaJ2.getJugadas() + 1);

        // Guardar o actualizar estadísticas
        this.repositorioEstadistica.guardarEstadistica(estadisticaJ1);
        this.repositorioEstadistica.guardarEstadistica(estadisticaJ2);
    }

    private Estadistica crearEstadisticaParaUsuario(Usuario usuario) {
        Estadistica nuevaEstadistica = new Estadistica();
        nuevaEstadistica.setUsuario(usuario);
        nuevaEstadistica.setJugadas(0); // Inicialmente 0
        nuevaEstadistica.setGanadas(0); // Inicialmente 0
        nuevaEstadistica.setJuego("Truco"); // Juego asociado
        return nuevaEstadistica;
    }


    @Override
    public void agregarEstadisticasFicticias(Usuario usuario) {
        Jugador j = new Jugador();
        j.setNombre(usuario.getNombreUsuario());
        j.setUsuario(usuario);

        this.repositorioTruco.guardarJugador(j);


        Estadistica e1 = new Estadistica();
        e1.setJuego("Truco");
        e1.setUsuario(usuario);
        e1.setGanadas(25);
        e1.setJugadas(80);
        Estadistica e2 = new Estadistica();
        e2.setJuego("Otro juego 1");
        e2.setUsuario(usuario);
        e2.setGanadas(5);
        e2.setJugadas(80);
        Estadistica e3 = new Estadistica();
        e3.setJuego("Otro juego 2");
        e3.setUsuario(usuario);
        e3.setGanadas(60);
        e3.setJugadas(80);


        this.repositorioEstadistica.guardarEstadistica(e1);
        this.repositorioEstadistica.guardarEstadistica(e2);
        this.repositorioEstadistica.guardarEstadistica(e3);

    }

    private Estadistica crearEstadisticaParaUsuario(Partida truco, Integer nroJugador) {
        Estadistica e = new Estadistica();
        e.setUsuario(nroJugador.equals(1) ? truco.getJ1().getUsuario() : truco.getJ2().getUsuario());
        e.setGanadas(0);
        e.setJugadas(0);
        e.setJuego("Truco");
        return e;
    }

    @Override
    public Estadistica obtenerEstadisticasDeUnJugador(Usuario usuario) {
        // Obtener todas las partidas desde el repositorio
        List<Estadistica> estadisticas = repositorioEstadistica.obtenerTodasLasEstadisticas();

        for (Estadistica estadistica : estadisticas) {
            Usuario usuarioBuscado = estadistica.getUsuario();

            // Verificamos si el jugador1 o jugador2 tiene el mismo id que el usuario
            if ((usuarioBuscado != null && usuarioBuscado.getId().equals(usuario.getId()))) {
                return estadistica;
            }
        }

        return null;
    }

    @Override
    public List<Estadistica> obtenerTopJugadores() {
       return this.repositorioEstadistica.obtenerTodasLasEstadisticas();
    }


}
