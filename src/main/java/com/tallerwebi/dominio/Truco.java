package com.tallerwebi.dominio;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Truco {

    private Integer nroRonda;
    private Integer testigo;

    private Boolean isLaManoTerminada;

    private Mano manoActual;
    private Mazo mazo;
    private Jugador ultimoGanador;

    private List<Mano> manosDePartida;
    private List<Jugador> jugadores; // 2-4


    public Truco() {
        this.nroRonda = 0;
        this.testigo = 0;
        this.isLaManoTerminada = false;
        this.mazo = new Mazo();
        this.jugadores = new ArrayList<>();
        this.manosDePartida = new ArrayList<>();
    }

    // Crear mano y asignar jugadores
    public void empezarMano(List<Jugador> jugadores) {
        this.manoActual = new Mano(jugadores);
        this.jugadores = jugadores;
    }

    // Asignar cartas
    public void asignarCartasJugadores(List<Jugador> jugadores) {
        List<Carta> seisCartasRandom = this.mazo.getSeisCartasAleatoriasSinRepetir();
        this.mazo.asignarCartasAJugadores(jugadores, seisCartasRandom);
        this.jugadores = jugadores;
    }

    // Método de test
    public void asignarCartasJugadores(List<Jugador> jugadores, List<Carta> cartas) {
        this.mazo.asignarCartasAJugadores(jugadores, cartas);
    }

    public void registrarJugada(Jugador j, Carta c) {
        this.manoActual.addRonda(j, c);
    }

    public List<Ronda> getRondasDeManoActual () {
        return this.manoActual.getRondas();
    }

    public Integer getNumeroDeRondasJugadasDeLaManoActual() {
        List<Ronda> rondasDeLaManoActual = getRondasDeManoActual();
        int nroRondaAnalizada = 0;
        if (rondasDeLaManoActual.isEmpty()) {
            return 0;
        } else {
            for (Ronda r : rondasDeLaManoActual) {
                if (r.getNroRonda().equals(nroRondaAnalizada)) {
                    nroRondaAnalizada++;
                }
            }
        }


        /*
        0 j c
        0 j c
        1 j c
        1 j c
        2 j c
        2 j c
           r    c
        1) 0 == 0 -> nro++ => 1
        2) 0 == 1 -> chau
        3) 1 == 1 -> nro++ => 2
        4) 1 == 2 -> chau
        5) 2 == 2 -> nro++ => 3
        6) 2 == 3 -> chau


        */
        return nroRondaAnalizada;
    }

    // Suma punto para saber que jugador ganó la ronda
    // Sí Franco gano 2 rondas, tendrá 2 puntos.
    // Si Gonzalo ganó 1 ronda, tendrá 1 punto
    // Entonces el que se llevá un punto por ganar la mano es Franco
    // Ese punto se ve afectado si se canta truco, envido, flor, etc.
    public void sumarPuntoDeRonda (Jugador jugador) {
        this.manoActual.guardarGanadorDeRonda(jugador);
        jugador.agregarPuntoRonda();
        // Si ya se jugaronn todas las cartas
        if (this.manoActual.getRondas().size() == 6) {
            Jugador ganador = this.manoActual.obtenerGanador();
            this.ultimoGanador = ganador;
            this.manoActual.setJugador(ganador);
            this.manoActual.setPuntos(1);
            this.manosDePartida.add(this.manoActual);
//            this.manoActual = null;
        }
    }

    public Jugador getGanadorDeManoPorNumero(Integer nroMano) {
        return this.manosDePartida.get(nroMano).getJugador();
    }

    public Carta buscarCartaPorNumeroYPalo(Integer numero, String palo) {
        for (Carta c : this.mazo.getCartas()) {
            if (c.getPalo().equals(palo) && c.getNumero().equals(numero)) {
                return c;
            }
        }
        return null;
    }


    public Jugador saberQuienSumoMasPuntosEnLasManos(Jugador j1, Jugador j2) {
        int puntosJ1 = 0;
        int puntosJ2 = 0;
        for (Mano m : this.manosDePartida) {
            if (m.getJugador().equals(j1)) {
                puntosJ1++;
            } else {
                puntosJ2++;
            }
        }
        if (puntosJ1 > puntosJ2) {
            return j1;
        } else {
            return j2;
        }
    }
    public String getGanadorDeRondaDeManoActualPorNumero(Integer nroRonda) {
        return this.manoActual.getGanadorDeUnaRondaPorNumero(nroRonda).getNombre();
    }


    public Integer getNroRonda() {
        return nroRonda;
    }

    public void setNroRonda(Integer nroRonda) {
        this.nroRonda = nroRonda;
    }

    public List<Mano> getManosDePartida() {
        return this.manosDePartida;
    }

    public void setManosDePartida(List<Mano> manosDePartida) {
        this.manosDePartida = manosDePartida;
    }

    public void cargarCartasAlMazo() {
        this.mazo = new Mazo();
    }

    public Mazo getMazo() {
        return mazo;
    }

    public void setMazo(Mazo mazo) {
        this.mazo = mazo;
    }

    public Integer getTestigo() {
        return testigo;
    }

    public void setTestigo(Integer testigo) {
        this.testigo = testigo;
    }

    public boolean saberSiEraLaUltimaCartaDeLaMano() {
        return true;
    }



    public Integer getMovimientosDeLaManoActual() {
        return this.manoActual.getNumeroDeMovimientosRealizados();
    }

    public boolean yaNoTieneCartas() {
        return this.manoActual.getNumeroDeMovimientosRealizados().equals(6) && this.manoActual.getRondasJugadas().equals(3);
    }

    public void terminarManoActual() {
        this.isLaManoTerminada = true;
    }

    public Boolean getLaManoTerminada() {
        return isLaManoTerminada;
    }

    public void setLaManoTerminada(Boolean laManoTerminada) {
        isLaManoTerminada = laManoTerminada;
    }



}