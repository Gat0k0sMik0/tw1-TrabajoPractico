package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;


@Service
@Transactional
public class ServicioInicioImpl implements ServicioInicio {

    private RepositorioUsuario repositorioUsuario;

    @Autowired
    public ServicioInicioImpl(RepositorioUsuario repositorioUsuario) {
        this.repositorioUsuario = repositorioUsuario;
    }

    @Override
    public Usuario consultarUsuario(String nombre, String password){
        return repositorioUsuario.buscarUsuario(nombre, password);
    }
}
