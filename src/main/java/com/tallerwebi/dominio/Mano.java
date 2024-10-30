package com.tallerwebi.dominio;

import java.util.ArrayList;
import java.util.List;

public class Mano {

    // El que la gana
    private Jugador jugador;
    // Los puntos (afecta acciones como truco, envido, etc)
    private Integer puntos = 0;


    private List<Ronda> rondas;
    private List<Carta> cartasJugadas;
    private Integer nroMovimientos;
    private Integer nroRonda;
    private boolean finalizacion = false;

    private List<Jugador> ganadoresDeRonda;
    private List<Jugador> jugadores; // 2-4

    public Mano(List<Jugador> jugadores) {
        this.rondas = new ArrayList<>();
        this.cartasJugadas = new ArrayList<>();
        this.nroMovimientos = 0;
        this.nroRonda = 0;
        this.ganadoresDeRonda = new ArrayList<>();
        this.jugadores = jugadores;
    }

    public Jugador getGanadorDeUnaRondaPorNumero (Integer nroRonda) {
        return this.ganadoresDeRonda.get(nroRonda);
    }

    private List<Ronda> getRondasPorNumero(Integer numero) {
        List<Ronda> r = new ArrayList<>();
        for (Ronda ronda : this.rondas) {
            if (ronda.getNroRonda().equals(numero)) {
                r.add(ronda);
            }
        }
        return r;
    }

    public void guardarGanadorDeRonda(Jugador jugador) {
        jugador.agregarPuntoRonda();
        this.ganadoresDeRonda.add(jugador);
    }


    public Jugador obtenerGanador() {
        Jugador ganadorActual = null;
        for (Jugador jugador : this.ganadoresDeRonda) {
            if (ganadorActual == null) {
                ganadorActual = jugador;
            } else {
                if (jugador.getPuntosRonda() > ganadorActual.getPuntosRonda()) {
                    ganadorActual = jugador;
                }
            }
        }
        return ganadorActual;
    }


    public void addRonda(Jugador j, Carta c) {
        this.rondas.add(new Ronda(nroRonda, j, c));
        this.cartasJugadas.add(c);
        if (this.nroMovimientos++ % 2 != 0) {
            this.nroRonda++;
        }
    }

    public List<Ronda> getRondas() {
        return this.rondas;
    }

    public Integer getRondasJugadas() {
        int nroRondaActual = 0;
        int counter = 0;
        for (Ronda r : rondas) {
            if (r.getNroRonda().equals(nroRondaActual)) {
                counter++;
                nroRondaActual++;
            }
        }
        return counter;
    }


    public Jugador getJugador() {
        return jugador;
    }

    public void setJugador(Jugador jugador) {
        this.jugador = jugador;
    }

    public Integer getPuntos() {
        return puntos;
    }

    public void setPuntos(Integer puntos) {
        this.puntos = puntos;
    }

    public void setRondas(List<Ronda> rondas) {
        this.rondas = rondas;
    }

    public boolean isFinalizacion() {
        return this.finalizacion;
    }

    public void setFinalizacion(boolean b) {
        this.finalizacion = b;
    }


    public List<Carta> getCartasJugadas() {
        return cartasJugadas;
    }

    public void setCartasJugadas(List<Carta> cartasJugadas) {
        this.cartasJugadas = cartasJugadas;
    }

    public Integer getNroRonda() {
        return nroRonda;
    }

    public void setNroRonda(Integer nroRonda) {
        this.nroRonda = nroRonda;
    }

    public Integer getNumeroDeMovimientosRealizados() {
        return this.nroMovimientos;
    }

    public void setNroMovimiento(Integer nroMovimiento) {
        this.nroMovimientos = nroMovimiento;
    }


}
