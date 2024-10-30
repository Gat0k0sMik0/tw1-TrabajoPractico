package com.tallerwebi.dominio;

public class Accion {
    private Jugador jugadorQueEjecuta;
    private String accion;
    private Boolean respuesta;
    private Integer nroAccion;

    public Accion(Integer nroAccion, Jugador jugadorQueEjecuta, String accion, Boolean respuesta) {
        this.nroAccion = nroAccion;
        this.jugadorQueEjecuta = jugadorQueEjecuta;
        this.accion = accion;
        this.respuesta = respuesta;
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
}
