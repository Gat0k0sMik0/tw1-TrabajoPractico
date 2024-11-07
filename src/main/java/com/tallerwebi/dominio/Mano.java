package com.tallerwebi.dominio;

import java.util.ArrayList;
import java.util.List;

public class Mano {

    // Los puntos (afecta acciones como truco, envido, etc)

    // Atributos
    private Integer nroMovimientos; // contador
    private Integer nroRonda; // contador
    private Integer puntos = 0; // en juego por truco
    private Boolean estaTerminada = false;

    // Listas 1 - N ?
    private List<Ronda> rondas;

    // Solo usado para testear
    private List<Jugador> ganadoresDeRonda;

    // Sin uso actualmente

    // Uso escaso/dudoso
    private List<Jugador> jugadores; // 2-4


    public Mano(List<Jugador> jugadores) {
        this.rondas = new ArrayList<>();
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

    public Integer getPuntos() {
        return puntos;
    }

    public void setPuntos(Integer puntos) {
        this.puntos = puntos;
    }

    public void acumularPuntosEnJuego(Integer puntos) {
        this.puntos += puntos;
    }

    public void setRondas(List<Ronda> rondas) {
        this.rondas = rondas;
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

    public Boolean getEstaTerminada() {
        return estaTerminada;
    }

    public void setEstaTerminada(Boolean estaTerminada) {
        this.estaTerminada = estaTerminada;
    }

}
