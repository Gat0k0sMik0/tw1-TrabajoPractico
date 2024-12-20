package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Jugador;
import com.tallerwebi.dominio.*;
import com.tallerwebi.dominio.excepcion.TrucoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
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
    public Partida preparar(Usuario usuarioActivo, Integer puntosMaximos) {
        Partida truco = new Partida();
        Jugador jugador1 = crearJugador(usuarioActivo, 1);
        prepararTruco(truco, jugador1, puntosMaximos);
        this.repositorioTruco.guardarJugador(jugador1);
        this.repositorioTruco.guardarPartida(truco);
        this.repositorioMano.eliminarCartasDeManosPorUsuario(usuarioActivo.getId());
        return truco;
    }

    private void prepararTruco(Partida truco, Jugador j, Integer puntosMaximos) {
        truco.setPuedeEmpezar(false);
        truco.setJ1(j);
        truco.setPuntosParaGanar(puntosMaximos);
        truco.setPuntosJ1(0);
        truco.setJ2(null);
        truco.setPuntosJ2(0);
        truco.setSeGuardo(false);
    }

    private Jugador crearJugador(Usuario usuario, Integer numero) {
        Jugador j = new Jugador();
        j.setNombre(usuario.getNombreUsuario());
        j.setNumero(numero);
        j.setUsuario(usuario);
        return j;
    }

    @Override
    public void agregarJugador(Usuario usuario, Partida truco) {
        Jugador jugador2 = crearJugador(usuario, 2);
        agregarJugadorTruco(truco, jugador2);
        this.repositorioTruco.guardarJugador(jugador2);
        this.repositorioTruco.merge(truco);
        this.repositorioMano.eliminarCartasDeManosPorUsuario(usuario.getId());
    }

    private void agregarJugadorTruco(Partida truco, Jugador jugador2) {
        truco.setJ2(jugador2);
        truco.setPuntosJ2(0);
        truco.setPuedeEmpezar(true);
    }

    @Override
    public List<Partida> getPartidasDisponibles(Long idUsuario) {
        return this.repositorioTruco.getPartidasDisponibles(idUsuario);
    }

    @Override
    public List<Partida> obtenerPartidasNoTerminadas(Long idUsuario) {
        return repositorioTruco.buscarPartidasNoTerminadas(idUsuario);
    }

    @Override
    public void empezar(Partida truco) {
        truco.setPuedeEmpezar(true);
        this.repositorioTruco.merge(truco);
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
        if (partida == null) throw new TrucoException("La partida no existe.");

        // Verificar si ya tiene un ganador
        if (partida.isPartidaFinalizada()) throw new TrucoException("La partida ya ha finalizado.");

        // Determinar el ganador
        partida.setGanador(ganador);
        this.registrarVictoria(ganador);

        // Guardar los cambios
        repositorioTruco.merge(partida);
    }

    @Override
    public void jugadorAbandona(Long idPartida, Long idUsuario) {
        Partida partida = repositorioTruco.buscarPartidaPorId(idPartida);
        if (partida.getJ1().getUsuario().getId().equals(idUsuario)) {
            partida.setGanador(partida.getJ2());
        } else {
            partida.setGanador(partida.getJ1());
        }
        repositorioTruco.merge(partida);
    }

    // Método para registrar la victoria de un jugador
    private void registrarVictoria(Jugador ganador) {
        ganador.getUsuario().setVictorias(ganador.getUsuario().getVictorias() + 1); // Incrementa las victorias
        actualizarNivel(ganador.getUsuario()); // Actualiza el nivel según las victorias
    }

    // Método para actualizar el nivel según las victorias
    private void actualizarNivel(Usuario jugador) {
        if (jugador.getVictorias() >= 50) {
            jugador.setNivel("Oro");
        } else if (jugador.getVictorias() >= 30) {
            jugador.setNivel("Plata");
        } else if (jugador.getVictorias() >= 10) {
            jugador.setNivel("Bronce");
        } else {
            jugador.setNivel("Sin Categoria");
        }
    }

    @Override
    public List<Partida> obtenerUltimas3PartidasDeUnJugador(Usuario usuario) {
        // Obtener todas las partidas desde el repositorio
        List<Partida> partidas = repositorioTruco.getTodasLasPartidas();

        // Lista para almacenar las partidas del jugador
        List<Partida> partidasDelJugador = new ArrayList<>();

        for (Partida partida : partidas) {
            Jugador jugador1 = partida.getJ1();
            Jugador jugador2 = partida.getJ2();

            // Verificamos si el jugador1 o jugador2 tiene el mismo id que el usuario
            if (((jugador1 != null && jugador1.getUsuario().getId().equals(usuario.getId())) ||
                    (jugador2 != null && jugador2.getUsuario().getId().equals(usuario.getId()))) && (partida.isPartidaFinalizada())) {
                partidasDelJugador.add(partida);
            }
        }

        if (!partidasDelJugador.isEmpty()) {
            // Ordenar las partidas por ID en orden descendente
            partidasDelJugador.sort((p1, p2) -> p2.getId().compareTo(p1.getId()));

            // Seleccionar solo las tres primeras partidas, si hay menos de 3, se tomarán todas
            if (partidasDelJugador.size() > 3) {
                partidasDelJugador = partidasDelJugador.subList(0, 3);
            }
        }

        return partidasDelJugador;
    }

    @Override
    public Partida obtenerUltimaPartidaDeUnJugador(Usuario usuario) {
        // Obtener todas las partidas desde el repositorio
        List<Partida> partidas = repositorioTruco.getTodasLasPartidas();

        // Variable para almacenar la última partida encontrada
        Partida ultimaPartida = null;

        for (Partida partida : partidas) {
            Jugador jugador1 = partida.getJ1();
            Jugador jugador2 = partida.getJ2();

            // Verificamos si el jugador1 o jugador2 tiene el mismo id que el usuario
            if (((jugador1 != null && jugador1.getUsuario().getId().equals(usuario.getId())) ||
                    (jugador2 != null && jugador2.getUsuario().getId().equals(usuario.getId()))) && (partida.isPartidaFinalizada())) {

                // Si es la primera partida encontrada o tiene un ID más alto que la actual
                if (ultimaPartida == null || partida.getId() > ultimaPartida.getId()) {
                    ultimaPartida = partida;
                }
            }
        }

        return ultimaPartida;
    }
}
