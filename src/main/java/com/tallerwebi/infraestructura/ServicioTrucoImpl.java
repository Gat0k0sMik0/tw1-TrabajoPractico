package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ServicioTrucoImpl implements ServicioTruco {

    private Truco truco;
    private Jugador jugador;
    private List<Carta> mazo;
    private List<Carta> cartasJugadas;
    private RepositorioCarta repositorioCarta;
    private Jugador turno;
    /*private Integer puntosJugador1;
    private Integer puntosJugador2;*/

    @Autowired
    public ServicioTrucoImpl(RepositorioCarta repositorioCarta) {
        this.repositorioCarta = repositorioCarta;
        this.cartasJugadas = new ArrayList<>();
    }

    @Override
    public void empezar(Jugador j1, Jugador j2) {
        this.truco = new Truco();
        this.truco.getMazo().setCartas(repositorioCarta.obtenerCartas());
        List<Carta> seisAleatorias = this.truco.getMazo().getSeisCartasAleatoriasSinRepetir();
        this.truco.getMazo().asignarCartasAJugadores(j1, j2, seisAleatorias);
    }

    // Método para que el jugador tire una carta
    public void tirarCarta(Jugador jugador, Carta cartaSeleccionada) {
        List<Carta> cartasJugador = jugador.getCartas();
        if (cartasJugador.contains(cartaSeleccionada)) {
            jugador.tirarCarta(cartaSeleccionada);
            truco.registrarMovimiento(jugador, cartaSeleccionada);
            cartasJugadas.add(cartaSeleccionada); // Agrega la carta a las cartas jugadas
        } else {
            throw new IllegalArgumentException("La carta seleccionada no está en la mano del jugador.");
        }
    }

    @Override
    public void cambiarTurno(Jugador jugador) {
        this.turno = jugador;
    }

    @Override
    public Boolean esTurnoJugador(String nombreJugador) {
        return (this.turno.getNombre().equals(nombreJugador));
    }

    @Override
    public Jugador getTurnoJugador() {
        return this.turno;
    }

    @Override
    public void determinarGanadorRonda(Jugador jugador1, Jugador jugador2) {
        Carta cartaJ1 = jugador1.getCartasTiradas().get(jugador1.getCartasTiradas().size() - 1);
        Carta cartaJ2 = jugador2.getCartasTiradas().get(jugador2.getCartasTiradas().size() - 1);

        if (cartaJ1.getValor() > cartaJ2.getValor()) {
            cambiarTurno(jugador1);
        } else if (cartaJ2.getValor() > cartaJ1.getValor()) {
            cambiarTurno(jugador2);
        }
    }

    public List<Carta> getCartasJugadas(Jugador j) {
        return j.getCartasTiradas();
    }

    @Override
    public List<Ronda> getRondasJugadas() {
        return truco.getRondasJugadas();

    }

    /* --ENVIDO--
    public void verificarEnvido(Jugador jugador1, Jugador jugador2) {
        Integer tantosJ1 = verLosTantos(jugador1);
        Integer tantosJ2 = verLosTantos(jugador2);

        if (tantosJ1 > tantosJ2) {
            this.puntosJugador1 = puntosJugador1 + 2;
        }

        if (tantosJ2 > tantosJ1) {
            this.puntosJugador2 = puntosJugador2 + 2;
        }


    }

    private Integer verLosTantos(Jugador jugador) {
        List<Carta> cartasJugador = jugador.getCartas();
        Integer tantos = 0;
        boolean mismoPalo = false;

        for (int i = 0; i < cartasJugador.size(); i++) {
            for (int j = i + 1; j < cartasJugador.size(); j++) {
                if (cartasJugador.get(i).getPalo().equals(cartasJugador.get(j).getPalo())) {
                    mismoPalo = true;

                    Integer valor1 = cartasJugador.get(i).getNumero();
                    Integer valor2 = cartasJugador.get(j).getNumero();
                    tantos = sumarTantos(valor1, valor2);
                    break;
                }
            }
        }

        return tantos;
    }

    private Integer sumarTantos(Integer valor1, Integer valor2) {
        Integer tantosIniciales = 20;
        Integer tantos = 0;

        if (!valor1.equals(10) || !valor1.equals(11) || !valor1.equals(12) || !valor2.equals(10) || !valor2.equals(11) || !valor2.equals(12)){
            tantos = tantosIniciales + valor1 + valor2;
        }

        if (valor1.equals(10) || valor1.equals(11) || valor1.equals(12) && valor2.equals(10) || valor2.equals(11) || valor2.equals(12)){
            tantos = tantosIniciales;
        }

        if (valor1.equals(10) || valor1.equals(11) || valor1.equals(12)) {
            tantos = tantosIniciales + valor2;
        }

        if (valor2.equals(10) || valor2.equals(11) || valor2.equals(12)) {
            tantos = tantosIniciales + valor1;
        }

        return tantos;
    }*/
}



