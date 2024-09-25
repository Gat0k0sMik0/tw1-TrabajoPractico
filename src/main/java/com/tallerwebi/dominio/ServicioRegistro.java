package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.*;
import org.springframework.stereotype.Service;

public interface ServicioRegistro {
    public void validarDatos(Usuario usuario) throws CamposVaciosException, NombreInvalidoException, EmailInvalidoException, ContraseniasDiferentesException, ContraseniaInvalidaException;
}
