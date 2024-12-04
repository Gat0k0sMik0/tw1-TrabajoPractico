package com.tallerwebi.dominio;

public class Accion {
    private int nro;
    private String texto;

    public Accion(int nro, String texto) {
        this.nro = nro;
        this.texto = texto;
    }

    public int getNro() {
        return nro;
    }

    public void setNro(int nro) {
        this.nro = nro;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }
}
