package com.tallerwebi.dominio;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;

public interface ServicioFotoDePerfil {
    Resource load(String filename) throws MalformedURLException;

    String copy(MultipartFile file) throws IOException;

    boolean delete(String filename);

    Path getPath(String filename);

    void guardarFotoDePerfil(FotoPerfil file);

    FotoPerfil buscarFotoPorId(Long id);
}
