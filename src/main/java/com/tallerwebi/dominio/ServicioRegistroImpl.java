package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service("ServicioRegistro")
@Transactional
public class ServicioRegistroImpl implements ServicioRegistro {
    @Override
    public void validarDatos(Usuario usuario) throws CamposVaciosException, NombreInvalidoException, EmailInvalidoException, ContraseniasDiferentesException, ContraseniaInvalidaException {
        if (usuario != null){
            if (usuario.getNombreUsuario() == null || usuario.getEmail() == null || usuario.getPassword() == null){
                throw new CamposVaciosException("Completa todos los campos");
            }
            if (usuario.getNombreUsuario().length() > 15 || usuario.getNombreUsuario().length() < 5) {
                throw new NombreInvalidoException("El nombre de usuario debe ser mayor a 5 caracteres y menor a 16");
            }
            if (!usuario.getEmail().contains("@")) {
                throw new EmailInvalidoException("El correo debe contener un \"@\"");
            }
            if (usuario.getPassword().length() < 7 || usuario.getPassword().length() > 17) {
                throw new ContraseniaInvalidaException("La contraseña debe ser mayor a 7 caracteres y menor a 17");
            }
            if(!usuario.getPassword().equals(usuario.getConfirmPassword())){
                throw new ContraseniasDiferentesException("Las contraseñas deben ser iguales");
            }
        }
    }
}
