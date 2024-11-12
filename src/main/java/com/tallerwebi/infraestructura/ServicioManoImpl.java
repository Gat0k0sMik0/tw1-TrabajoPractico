package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ServicioManoImpl implements ServicioMano {

    private ServicioRonda servicioRonda;

    private List<Mano> manos;
    private List<Accion> acciones;

    private Boolean estaTerminada = true;
    private Integer nroAccion = 0;
    private Integer puntosEnJuego = 0;

    private Jugador ganadorDeManoActual = null;

    public ServicioManoImpl(ServicioRonda servicioRonda) {
        this.servicioRonda = servicioRonda;
        this.manos = new ArrayList<>();
        this.acciones = new ArrayList<>();
    }

    @Override
    public void empezar() {
        servicioRonda.empezar();
        this.estaTerminada = false;
    }

    @Override
    public Boolean estaLaManoTerminada() {
        return this.estaTerminada;
    }

    @Override
    public void guardarRonda(Jugador jugador, Carta cartaSeleccionada) {
        servicioRonda.crearRonda(jugador, cartaSeleccionada);
    }


    @Override
    public void sumarPuntoDeRonda(Jugador jugador1) {
        jugador1.agregarPuntoRonda();
    }

    @Override
    public Integer guardarAccion(Jugador cantador, String accion, boolean b, Integer puntosEnJuego) {
        Accion a = new Accion(this.nroAccion++, cantador, accion, b, puntosEnJuego);
        this.acciones.add(a);
        return a.getNroAccion();
    }

    @Override
    public void agregarPuntosEnJuegoManoActual(Integer puntosViejos) {
        this.puntosEnJuego += puntosViejos;
    }

    @Override
    public Jugador getGanadorDeManoActual() {
        return this.ganadorDeManoActual;
    }

    @Override
    public void setGanadorDeRonda(Jugador j) {
        this.ganadorDeManoActual = j;
        j.acumularPuntosDePartida(1);
    }

    @Override
    public String getGanadorDeRondaPorNumero(int i) {
        return servicioRonda.getGanadorDeRondaPorNumero(i);
    }
    @Override
    public List<Mano> getManos() {
        return this.manos;
    }

    @Override
    public List<Accion> getAcciones() {
        return acciones;
    }

    @Override
    public List<Ronda> getRondas() {
        return servicioRonda.getRondas();
    }

    //   GETTERS Y SETTERS

    public ServicioRonda getServicioRonda() {
        return servicioRonda;
    }

    public void setServicioRonda(ServicioRonda servicioRonda) {
        this.servicioRonda = servicioRonda;
    }

    public void setManos(List<Mano> manos) {
        this.manos = manos;
    }

    public void setAcciones(List<Accion> acciones) {
        this.acciones = acciones;
    }

    public Boolean getEstaTerminada() {
        return estaTerminada;
    }

    public void setEstaTerminada(Boolean estaTerminada) {
        this.estaTerminada = estaTerminada;
    }

    public Integer getNroAccion() {
        return nroAccion;
    }

    public void setNroAccion(Integer nroAccion) {
        this.nroAccion = nroAccion;
    }

    public void setGanadorDeManoActual(Jugador ganadorDeManoActual) {
        this.ganadorDeManoActual = ganadorDeManoActual;
    }
}
