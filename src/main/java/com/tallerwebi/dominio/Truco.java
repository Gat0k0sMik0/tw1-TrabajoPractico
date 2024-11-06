package com.tallerwebi.dominio;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Truco {

    private Integer nroRonda;
    private Integer testigo;
    private Integer contadorAcciones = 0;
    private Integer puntosJ1 = 0;
    private Integer puntosJ2 = 0;
    private Integer puntosEnJuegoDeLaMano = 0;

    private Boolean isLaManoTerminada;

    private Mano manoActual;
    private Mazo mazo;
    // List<Carta> cartas

    private Jugador ultimoGanador;

    private List<Mano> manosDePartida;
    private List<Jugador> jugadores; // 2-4
    private List<Accion> acciones;
    private Boolean trucoCantado;

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

    public Integer calcularEnvido(List<Carta> cartas) {
        List<Carta> tieneDosDelMismoPalo = this.tieneDosDelMismoPalo(cartas);
        if (tieneDosDelMismoPalo.isEmpty()) {
            return 0;
        } else {
            return this.obtenerSumaDeLasMasAltas(tieneDosDelMismoPalo);
        }
    }

    public Integer guardarAccion(Jugador jugadorQueEjecuta, String accion, Boolean respuesta, Integer puntosEnJuego) {
        this.acciones.add(new Accion(this.contadorAcciones, jugadorQueEjecuta, accion, false, puntosEnJuego));
        return this.contadorAcciones++;
    }

    public Integer obtenerSumaDeLasMasAltas(List<Carta> cartas) {
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

    public List<Carta> tieneDosDelMismoPalo(List<Carta> cartas) {
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

    public List<Ronda> getRondasDeManoActual() {
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
    public void sumarPuntoDeRonda(Jugador jugador) {
        this.manoActual.guardarGanadorDeRonda(jugador);
        jugador.agregarPuntoRonda();
        // Si ya se jugaronn todas las cartas
        if (this.manoActual.getRondas().size() == 6) {
            Jugador ganador = this.manoActual.obtenerGanador();

            if (!this.saberSiHuboAlgunTruco()) {
                this.manoActual.acumularPuntosEnJuego(1); // por ganar 2/3 rondas sin truco
            }

            this.manoActual.acumularPuntosEnJuego(puntosEnJuegoDeLaMano); // por ganar algún truco

            ganador.acumularPuntosDePartida(this.manoActual.getPuntos());

            this.ultimoGanador = ganador; // el ultimo que ganó
            this.manoActual.setJugador(ganador); // el que ganó más rondas

            this.manosDePartida.add(this.manoActual); // guardar historico

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

    // -- HANDLERS DE ACCIONES --

    // Saber si hubo algún truco
    private Boolean saberSiHuboAlgunTruco () {
        for (Accion a : this.acciones) {
            if (a.getAccion().equals("TRUCO")) {
                if (a.getRespuesta()) {
                    return true;
                }
            }
        }
        return false;
    }

    public Accion getAccionPorNro(Integer nro) {
        for (Accion a : this.acciones) {
            if (a.getNroAccion().equals(nro)) {
                return a;
            }
        }
        return null;
    }

    // -- HANDLERS DE LA MANO ACTUAL --

    // Obtener número de movimientos
    public Integer getMovimientosDeLaManoActual() {
        return this.manoActual.getNumeroDeMovimientosRealizados();
    }

    // Saber si hubo 6 movimientos y 3 rondas.
    public boolean yaNoTieneCartas() {
        return this.manoActual.getNumeroDeMovimientosRealizados().equals(6) && this.manoActual.getRondasJugadas().equals(3);
    }

    // Terminar la mano actual
    public void terminarManoActual() {
        this.isLaManoTerminada = true;
    }

    // Saber si la mano esta terminada
    public Boolean isLaManoTerminada() {
        return this.isLaManoTerminada;
    }

    // Retornar jugador que ganó la mano
    public Jugador getGanadorDeLaMano() {
        return this.manoActual.getJugador();
    }

    // Retornar puntos del ganador de la mano (afectado por truco, re truco)
    public Integer getPuntosDelGanadorDeLaMano() {
        return this.puntosEnJuegoDeLaMano;
    }

    // -- FIN HANDLERS DE MANO ACTUAL --


    public List<Accion> getAcciones() {
        return acciones;
    }

    public void setAcciones(List<Accion> acciones) {
        this.acciones = acciones;
    }

    public Boolean getTrucoCantado() {
        return trucoCantado;
    }

    public void setTrucoCantado(Boolean trucoCantado) {
        this.trucoCantado = trucoCantado;
    }



    public void guardarPuntosDePartida(Jugador j, Integer puntos) {
        Jugador buscado = this.getJugador(j);
        if (buscado != null) {
            buscado.setPuntosPartida(puntos);
        }
    }


    public Integer getPuntosDeUnJugador(Jugador jugador) {
        Jugador buscado = this.getJugador(jugador);
        if (buscado != null) {
            return buscado.getPuntosPartida();
        }
        return -1;
    }

    private Jugador getJugador(Jugador jugador) {
        for (Jugador j : this.jugadores) {
            if (j.equals(jugador)) {
                return j;
            }
        }
        return null;
    }

    public void agregarPuntosEnJuegoManoActual(Integer puntos) {
        this.manoActual.acumularPuntosEnJuego(puntos);
    }

    public int getPuntosEnJuegoDeLaManoActual() {
        return this.manoActual.getPuntos();
    }

    public void guardarPuntosParaElGanadorDelTruco(Integer puntosEnJuego) {
        this.puntosEnJuegoDeLaMano += puntosEnJuego;
    }


    public Integer getPuntosJ1() {
        return this.puntosJ1;
    }

    public Integer getPuntosJ2() {
        return this.puntosJ2;
    }
}