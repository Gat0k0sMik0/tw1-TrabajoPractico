package com.tallerwebi.dominio;

import javax.persistence.*;

public class Accion {

    private Jugador jugadorQueEjecuta;
    private String accion;
    private Boolean respuesta;
    private Integer nroAccion;
    private Integer puntosEnJuego = 0;

    public Accion(Integer nroAccion, Jugador jugadorQueEjecuta, String accion, Boolean respuesta, Integer puntosEnJuego) {
        this.nroAccion = nroAccion;
        this.puntosEnJuego += puntosEnJuego;
        this.jugadorQueEjecuta = jugadorQueEjecuta;
        this.accion = accion;
        this.respuesta = respuesta;
    }

    public void acumularPuntos(Integer puntos) {
        puntosEnJuego += puntos;
    }

    public Jugador getJugadorQueEjecuta() {
        return jugadorQueEjecuta;
    }

    public void setJugadorQueEjecuta(Jugador jugadorQueEjecuta) {
        this.jugadorQueEjecuta = jugadorQueEjecuta;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public Boolean getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(Boolean respuesta) {
        this.respuesta = respuesta;
    }

    public Integer getNroAccion() {
        return nroAccion;
    }

    public void setNroAccion(Integer nroAccion) {
        this.nroAccion = nroAccion;
    }

    public Integer getPuntosEnJuego() {
        return puntosEnJuego;
    }

    public void setPuntosEnJuego(Integer puntosEnJuego) {
        this.puntosEnJuego = puntosEnJuego;
    }
}
