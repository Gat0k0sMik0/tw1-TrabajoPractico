package com.tallerwebi.dominio;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Truco {

    private List<Ronda> rondasJugadas;
    private Integer nroMovimiento;
    private Integer nroRonda;
    private Integer testigo;
    private List<Mano> manosDePartida;
    private Mazo mazo = null;

    public Truco() {
        this.rondasJugadas = new ArrayList<>();
        this.nroMovimiento = 0;
        this.nroRonda = 0;
        this.testigo = 0;
        manosDePartida = new ArrayList<>();
        this.mazo = new Mazo();
    }



    public void registrarMovimiento(Jugador j1, Carta cartaTirada) {
        Ronda r = new Ronda(nroRonda,
                j1,
                cartaTirada.getValor(),
                cartaTirada.getNumero(),
                cartaTirada.getPalo());
        rondasJugadas.add(r);
        if (nroMovimiento % 2 != 0) {
            nroRonda++;
        }
        nroMovimiento++;
    }

    public Jugador saberQuienGanoNumeroDeRonda(Integer nroRonda) {
        List<Ronda> jugadas = new ArrayList<>();
        for (Ronda r : this.rondasJugadas) {
            if (r != null) {
                if (r.getNroRonda().equals(nroRonda)) {
                    jugadas.add(r);
                }
            }

        }
        Integer ultimoValor = 0;
        Jugador ultimoJugador = null;
        Jugador ganadorRonda = null;

        for (Ronda round : jugadas) {
            if (ultimoJugador != null) {
                // despues de primera busqueda
                if (round.getValorCarta() > ultimoValor) {
                    // la segunda es mejor que la primera
                    ganadorRonda = round.getJugador();
                } else {
                    ganadorRonda = ultimoJugador;
                    // la primera es mejor que la segunda
                }
            } else {
                // primera ves que busca
                ultimoValor = round.getValorCarta();
                ultimoJugador = round.getJugador();
                // ganador provisional
                ganadorRonda = round.getJugador();
            }
        }
        return ganadorRonda;
    }

    private List<Ronda> getRondasPorNumero(Integer nroRonda) {
        List<Ronda> rondas = new ArrayList<>();
        for (Ronda r : this.rondasJugadas) {
            if (r.getNroRonda().equals(nroRonda)) {
                rondas.add(r);
            }
        }
        return rondas;
    }

    public Jugador saberQuienGanoLaPrimeraMano(Jugador j1, Jugador j2) {
        Integer puntosJ1 = 0;
        Integer puntosJ2 = 0;
        Integer rondaAAnalizar = 0;
        while (rondaAAnalizar < 3) {
            List<Ronda> rondaBuscada = getRondasPorNumero(rondaAAnalizar);
            if (rondaBuscada.get(0).getValorCarta() > rondaBuscada.get(1).getValorCarta()) {
                if (rondaBuscada.get(0).getJugador().equals(j1)) {
                    puntosJ1++;
                } else {
                    puntosJ2++;
                }
            } else {
                if (rondaBuscada.get(1).getJugador().equals(j1)) {
                    puntosJ1++;
                } else {
                    puntosJ2++;
                }
            }
            rondaAAnalizar++;
        }
        if (puntosJ1 > puntosJ2) {
            return j1;
        } else {
            return j2;
        }
    }

    public void guardarGanadorDeMano(Jugador ganadorMano, Integer puntos) {
        Mano mano = new Mano(ganadorMano, puntos);
        manosDePartida.add(mano);
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

    public Integer getNroRonda() {
        return nroRonda;
    }

    public void setNroRonda(Integer nroRonda) {
        this.nroRonda = nroRonda;
    }

    public List<Mano> getManosDePartida() {
        return manosDePartida;
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

    public List<Ronda> getRondasJugadas() {
        return rondasJugadas;
    }

    public void setRondasJugadas(List<Ronda> rondasJugadas) {
        this.rondasJugadas = rondasJugadas;
    }

    public Integer getTestigo() {
        return testigo;
    }

    public void setTestigo(Integer testigo) {
        this.testigo = testigo;
    }

    public Integer getNroMovimiento() {
        return nroMovimiento;
    }

    public void setNroMovimiento(Integer nroMovimiento) {
        this.nroMovimiento = nroMovimiento;
    }
}