package com.tallerwebi.dominio;

import java.util.Objects;

public class Mensaje {
    private final String nombre;
    private final String contenido;

    public Mensaje(String nombre, String contenido) {
        this.nombre = nombre;
        this.contenido = contenido;
    }

    public String getNombre() {
        return nombre;
    }

    public String getContenido() {
        return contenido;
    }

    @Override
    public String toString() {
        return "Mensaje{" +
                "nombre='" + nombre + '\'' +
                ", contenido='" + contenido + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mensaje mensaje = (Mensaje) o;
        return Objects.equals(nombre, mensaje.nombre) &&
                Objects.equals(contenido, mensaje.contenido);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre, contenido);
    }
}