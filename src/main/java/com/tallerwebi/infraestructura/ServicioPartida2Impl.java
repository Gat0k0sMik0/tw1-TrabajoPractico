package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Jugador;
import com.tallerwebi.dominio.*;
import com.tallerwebi.dominio.excepcion.TrucoException;
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

    private Integer movimientos;
    private Integer puntosJ1;
    private Integer puntosJ2;
    private Integer nroAccion;

    private Jugador turno;

    private List<Accion> acciones;

    public ServicioPartida2Impl(RepositorioTruco repositorioTruco, RepositorioCarta repositorioCarta) {
        this.repositorioTruco = repositorioTruco;
        this.repositorioCarta = repositorioCarta;
        this.movimientos = 0;
        this.puntosJ1 = 0;
        this.puntosJ2 = 0;
        this.nroAccion = 0;
        this.turno = null;
        this.acciones = new ArrayList<>();
        this.turno = null;
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

    @Override // BORRAR DESPUÉS
    public Truco2 empezarTest(Jugador j1, Jugador j2) {
        Truco2 truco = new Truco2();
        truco.setJ1(j1);
        truco.setJ2(j2);
        return truco;
    }

    @Override
    public Ronda2 tirarCarta(Mano2 mano, Jugador jugador, Carta carta, String nroJugador) {
        if (!mano.getEstaTerminada()) {
            List<Carta> cartasJugador = jugador.getCartas();
            if (cartasJugador.contains(carta)) {
                jugador.tirarCarta(carta);
                movimientos++;
                Ronda2 r = new Ronda2();
                r.setNombreJugador(jugador.getNombre());
                r.setPaloCarta(carta.getPalo());
                r.setValorCarta(carta.getValor());
                r.setNroCarta(carta.getNumero());
                return r;
            } else {
                throw new TrucoException("La carta seleccionada no está en la mano del jugador.");
            }
        } else {
            throw new TrucoException("Estas tirando estando la mano terminada.");
        }
    }

    @Override
    public void determinarGanadorRonda(Jugador jugador1, Jugador jugador2) {
        Jugador ganador = obtenerGanadorDeRonda(jugador1, jugador2);
        if (ganador.getNombre().equals(jugador1.getNombre())) {
            puntosJ1++;
            jugador1.setPuntosPartida(jugador1.getPuntosPartida() + 1);
        } else {
            puntosJ2++;
            jugador2.setPuntosPartida(jugador2.getPuntosPartida() + 1);
        }
    }

    @Override
    public void cambiarTurno(Jugador jugador) {
        this.turno = jugador;
    }

    @Override
    public Integer preguntar(String accion, Jugador ejecutor, Jugador receptor) {
        String accionEncontrada = saberAccion(accion);
        if (accionEncontrada.equalsIgnoreCase("TRUCO")) {
            return manejarTruco(accionEncontrada, ejecutor, receptor);
        } else {
            return manejarEnvido(accionEncontrada, ejecutor, receptor);
        }
    }

    private Integer manejarTruco(String accion, Jugador cantador, Jugador receptor) {
        if (accion.equalsIgnoreCase("TRUCO")) {
            return this.crearAccion(accion, cantador, receptor, 2);
        } else if (accion.equals("RE TRUCO")) {
            return this.crearAccion(accion, cantador, receptor, 3);
        } else if (accion.equals("VALE 4")) {
            return this.crearAccion(accion, cantador, receptor, 4);
        } else {
            // todo
        }
        return null;
    }

    private Integer manejarEnvido(String accion, Jugador cantador, Jugador receptor) {
        if (accion.equalsIgnoreCase("ENVIDO")) {
            return this.crearAccion(accion, cantador, receptor, 2);
        } else if (accion.equals("REAL ENVIDO")) {
            return this.crearAccion(accion, cantador, receptor, 3);
        } else if (accion.equals("FALTA ENVIDO")) {
            // todo
        } else {
            // todo
        }
        return null;
    }

    private Integer crearAccion (String accion,
                                 Jugador cantador,
                                 Jugador receptor,
                                 Integer puntosEnJuego) {
        Accion a = new Accion(this.nroAccion++, cantador, accion, false, puntosEnJuego);
        this.acciones.add(a);
        return a.getNroAccion();
    }


    private Jugador obtenerGanadorDeRonda(Jugador jugador1, Jugador jugador2) {
        // Si ya tiraron los 2
        if (jugador1.getCartasTiradas().size() == jugador2.getCartasTiradas().size()) {
            // Conseguimos las últimas tiradas.
            Carta cartaJ1 = jugador1.getCartasTiradas().get(jugador1.getCartasTiradas().size() - 1);
            Carta cartaJ2 = jugador2.getCartasTiradas().get(jugador2.getCartasTiradas().size() - 1);
            // Comparamos quien tiró la más alta, en base a eso damos poder
            if (cartaJ1.getValor() > cartaJ2.getValor()) {
                jugador1.setPuntosRonda(jugador1.getPuntosRonda() + 1);
                return jugador1;
            } else if (cartaJ2.getValor() > cartaJ1.getValor()) {
                jugador2.setPuntosRonda(jugador2.getPuntosRonda() + 1);
                return jugador2;
            }
        }
        throw new TrucoException("No hay ganador, empate o error.");
    }

    private String saberAccion(String numberValue) {
        String respuestaDada = "";
        switch (Integer.parseInt(numberValue)) {
            case 0:
                respuestaDada = "NO QUIERO";
                break;
            case 1:
                respuestaDada = "QUIERO";
                break;
            case 2:
                respuestaDada = "ENVIDO";
                break;
            case 3:
                respuestaDada = "REAL ENVIDO";
                break;
            case 4:
                respuestaDada = "FALTA ENVIDO";
                break;
            case 5:
                respuestaDada = "TRUCO";
                break;
            case 6:
                respuestaDada = "RE TRUCO";
                break;
            case 7:
                respuestaDada = "VALE 4";
                break;
            case 8:
                respuestaDada = "FLOR";
                break;
            case 9:
                respuestaDada = "MAZO";
                break;
            default:
                return "";
        }
        return respuestaDada;
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
