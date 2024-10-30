package com.tallerwebi.dominio;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Truco {

    private Integer nroRonda;
    private Integer testigo;
    private Integer contadorAcciones = 0;

    private Boolean isLaManoTerminada;

    private Mano manoActual;
    private Mazo mazo;
    // List<Carta> cartas

    private Jugador ultimoGanador;

    private List<Mano> manosDePartida;
    private List<Jugador> jugadores; // 2-4
    private List<Accion> acciones;

    /*
    servicio repartir cartas
    */

    public Truco() {
        this.nroRonda = 0;
        this.testigo = 0;
        this.isLaManoTerminada = false;
        this.mazo = new Mazo();
        this.jugadores = new ArrayList<>();
        this.manosDePartida = new ArrayList<>();
        this.acciones = new ArrayList<>();
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

    public Integer calcularEnvido (List<Carta> cartas) {
        List<Carta> tieneDosDelMismoPalo = this.tieneDosDelMismoPalo(cartas);
        if (tieneDosDelMismoPalo.isEmpty()) {
            return 0;
        } else {
            return this.obtenerSumaDeLasMasAltas(tieneDosDelMismoPalo);
        }
    }

    public void guardarAccion(Jugador jugadorQueEjecuta, String accion, Boolean respuesta) {
        this.acciones.add(new Accion(this.contadorAcciones, jugadorQueEjecuta, accion, false));
        this.contadorAcciones++;
    }

    public Integer obtenerSumaDeLasMasAltas (List<Carta> cartas) {
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

    public List<Carta> tieneDosDelMismoPalo (List<Carta> cartas) {
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

    public List<Accion> getAcciones() {
        return acciones;
    }

    public void setAcciones(List<Accion> acciones) {
        this.acciones = acciones;
    }
}