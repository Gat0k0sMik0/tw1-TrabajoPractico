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
    private List<Carta> cartasJ1;
    private List<Carta> cartasJ2;
    private List<Carta> cartasTiradasJ1;
    private List<Carta> cartasTiradasJ2;
    private Jugador j1;
    private Jugador j2;


    public ServicioPartida2Impl(RepositorioTruco repositorioTruco, RepositorioCarta repositorioCarta, RepositorioMano repositorioMano) {
        this.repositorioTruco = repositorioTruco;
        this.repositorioCarta = repositorioCarta;
        this.repositorioMano = repositorioMano;
        this.cartasJ1 = new ArrayList<>();
        this.cartasJ2 = new ArrayList<>();
        this.cartasTiradasJ1 = new ArrayList<>();
        this.cartasTiradasJ2 = new ArrayList<>();
        this.j1 = null;
        this.j2 = null;
        this.movimientos = 0;
        this.puntosJ1 = 0;
        this.puntosJ2 = 0;
    }

    // TODO: despues cambiar
    @Override
    public List<Carta> getCartasTiradasJ1() {
        return this.cartasTiradasJ1;
    }
    @Override
    public List<Carta> getCartasTiradasJ2() {
        return this.cartasTiradasJ2;
    }



    @Transactional
    @Override
    public Truco2 obtenerPartidaPorId(Long id) {
        Truco2 partida = this.repositorioTruco.buscarPartidaPorId(id);
        List<Carta> cartasJ1 = repositorioCarta.obtenerCartasDeJugadorPorId(partida.getJ1().getId());
        List<Carta> cartasJ2 = repositorioCarta.obtenerCartasDeJugadorPorId(partida.getJ2().getId());
        Jugador j1 = this.repositorioTruco.obtenerJugadorPorID(partida.getJ1().getId());
        Jugador j2 = this.repositorioTruco.obtenerJugadorPorID(partida.getJ2().getId());

        this.j1 = partida.getJ1();
        this.j2 = partida.getJ2();
        this.puntosJ1 = partida.getPuntosJ1();
        this.puntosJ2 = partida.getPuntosJ2();
        this.cartasJ1.addAll(cartasJ1);
        this.cartasJ2.addAll(cartasJ2);

        System.out.println("Del repo j1 vino con: " + j1.getCartas().size());
        this.j1.setCartas(j1.getCartas());
        this.j2.setCartas(j2.getCartas());

        return partida;
    }

    @Override
    public Truco2 empezar(Jugador j1, Jugador j2) {
        Truco2 truco = new Truco2();
        this.repositorioTruco.guardarJugador(j1);
        this.repositorioTruco.guardarJugador(j2);
        truco.setJ1(j1);
        truco.setJ2(j2);
        truco.setPuntosParaGanar(0);
        truco.setPuntosJ1(0);
        truco.setPuntosJ1(0);
        this.repositorioTruco.guardarPartida(truco);
        j1.setCartas(new ArrayList<>());
        j2.setCartas(new ArrayList<>());
        j1.setPartida(truco);
        j2.setPartida(truco);
        this.asignarCartasJugadores(j1, j2);
        return truco;
    }

    @Override
    public void reset(Jugador j1, Jugador j2) {
        this.vaciarCartasDeJugadores(j1, j2);
        this.asignarCartasJugadores(j1, j2);
    }

    private void vaciarCartasDeJugadores(Jugador j1, Jugador j2) {
        j1.getCartas().clear();
        j2.getCartas().clear();
    }


    @Override // BORRAR DESPUÉS
    public Truco2 empezarTest(Jugador j1, Jugador j2) {
        Truco2 truco = new Truco2();
        truco.setJ1(j1);
        truco.setJ2(j2);
        return truco;
    }

    private void sacarCartaDeJugador(Jugador j, Carta c) {
        boolean existeLaCartaEnCartas = j.getCartas().contains(c);
        List<Carta> cartasJugador = j.getCartas();
        if (existeLaCartaEnCartas) {
            cartasJugador.remove(c);
            if (j.getNumero().equals(1)) {
                this.cartasJ1.add(c);
            } else {
                this.cartasJ2.add(c);
            }
//            cartasTiradas.add(carta);
        }
        j.setCartas(cartasJugador);
        System.out.println("Le quedan " + j.getCartas().size() + " cartas.");
//        } else return null;
    }

    @Override
    public Ronda tirarCarta(Mano2 mano, Jugador jugador, Carta carta, String nroJugador) {
        if (!mano.getEstaTerminada()) {
            List<Carta> cartasJugador = jugador.getCartas();
            if (cartasJugador.contains(carta)) {
                sacarCartaDeJugador(jugador, carta);
                sumarMovimientoMano(mano);
                Ronda r = new Ronda();
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
    public void determinarGanadorRonda(Truco2 truco, Jugador jugador1, Jugador jugador2) {
        Jugador ganador = obtenerGanadorDeRonda(jugador1, jugador2);
        if (ganador.getNombre().equals(jugador1.getNombre())) {
            puntosJ1++;
            jugador1.setPuntosPartida(jugador1.getPuntosPartida() + 1);
            truco.setPuntosJ1(jugador1.getPuntosPartida());
        } else {
            puntosJ2++;
            jugador2.setPuntosPartida(jugador2.getPuntosPartida() + 1);
            truco.setPuntosJ2(jugador2.getPuntosPartida());
        }
        this.repositorioTruco.guardarPartida(truco);
    }

    private Integer obtenerSumaDeLasMasAltas(List<Carta> cartas) {
        int envidoMax = 0;

        // Agrupa las cartas por palo
        for (int i = 0; i < cartas.size(); i++) {
            for (int j = i + 1; j < cartas.size(); j++) {
                if (cartas.get(i).getPalo().equals(cartas.get(j).getPalo())) {
                    // Si dos cartas tienen el mismo palo, suma sus valores y añade 20
                    int envido = cartas.get(i).getValorEnvido() + cartas.get(j).getValorEnvido() + 20;
                    envidoMax = Math.max(envidoMax, envido);
                }
            }
        }

        // Si no hay dos cartas del mismo palo, el envido es el valor más alto de la mano
        if (envidoMax == 0) {
            for (Carta carta : cartas) {
                envidoMax = Math.max(envidoMax, carta.getValorEnvido());
            }
        }

        return envidoMax;
    }

    private List<Carta> tieneDosDelMismoPalo(List<Carta> cartas) {
        int contador = 0;
        List<Carta> delMismoPalo = new ArrayList<>();
        for (Carta carta : cartas) {
            for (Carta carta2 : cartas) {
                if (carta.getPalo().equals(carta2.getPalo())) {
                    delMismoPalo.add(carta2);
                }
            }
            if (delMismoPalo.size() >= 2) {
                contador = 1;
                break;
            }
        }
        if (contador == 1) {
            return delMismoPalo;
        }
        return new ArrayList<>();
    }

    private Boolean esEnvido(String accion) {
        return accion.equals("ENVIDO") || accion.equals("REAL ENVIDO");
    }

    private Boolean esTruco(String accion) {
        return accion.equals("TRUCO") || accion.equals("RE TRUCO") || accion.equals("VALE 4");
    }

    private Jugador obtenerGanadorDeRonda(Jugador jugador1, Jugador jugador2) {
        // Si ya tiraron los 2
        if (this.cartasTiradasJ1.size() == this.cartasTiradasJ2.size()) {
            // Conseguimos las últimas tiradas.
            Carta cartaJ1 = this.cartasTiradasJ1.get(this.cartasTiradasJ2.size() - 1);
            Carta cartaJ2 = this.cartasTiradasJ2.get(this.cartasTiradasJ1.size() - 1);
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

    private void asignarCartasJugadores(Jugador j1, Jugador j2) {
        List<Carta> cartas = repositorioCarta.obtenerCartas();
        List<Carta> seisCartasRandom = obtenerSeisCartasRandom(cartas);
        asignarCartasJugador(j1, seisCartasRandom);
        asignarCartasJugador(j2, seisCartasRandom);
        System.out.println("guardo: " + j1);
        System.out.println("tenia: " + j1.getCartas().size() + " cartas");
    }

    private void asignarCartasJugador(Jugador j, List<Carta> seisCartasRandom) {
        if (j.getNumero().equals(1)) {
            Iterator<Carta> iterator = seisCartasRandom.iterator();
            while (iterator.hasNext() && this.cartasJ1.size() < 3) {
                Carta carta = iterator.next();
                this.cartasJ1.add(carta);
                iterator.remove();
                carta.setJugador(j);
            }
            j.setCartas(this.cartasJ1);
        } else {
            Iterator<Carta> iterator = seisCartasRandom.iterator();
            while (iterator.hasNext() && this.cartasJ2.size() < 3) {
                Carta carta = iterator.next();
                this.cartasJ2.add(carta);
                iterator.remove(); // Elimina de forma segura del iterador
            }
            j.setCartas(this.cartasJ2);
        }
        System.out.println("Guardo jugador: " + j);
        repositorioCarta.guardarVariasCartas(j.getCartas());
        repositorioTruco.guardarJugador(j);
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

    private void sumarMovimientoMano(Mano2 mano) {
        mano.setMovimientos(mano.getMovimientos() + 1);
        repositorioMano.guardar(mano);
    }


}
