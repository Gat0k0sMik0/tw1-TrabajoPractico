package com.tallerwebi.dominio.excepcion;

public class EmailInvalidoException extends Exception {
    public EmailInvalidoException(String mensaje) {
        super(mensaje);
    }
}
