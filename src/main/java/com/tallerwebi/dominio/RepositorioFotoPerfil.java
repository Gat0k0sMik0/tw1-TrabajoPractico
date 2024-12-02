package com.tallerwebi.dominio;

import org.springframework.stereotype.Repository;

import java.util.List;

public interface RepositorioFotoPerfil {
    List<FotoPerfil> obtenerFotos();

    FotoPerfil buscarFotoPorId(Long id);

    void guardarFotoPerfil(FotoPerfil foto);
}
