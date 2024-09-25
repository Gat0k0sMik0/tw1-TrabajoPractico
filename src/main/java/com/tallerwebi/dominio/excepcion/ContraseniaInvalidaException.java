package com.tallerwebi.dominio.excepcion;

public class ContraseniaInvalidaException extends Exception {
    public ContraseniaInvalidaException(String mensaje) {
        super(mensaje);
    }
}
