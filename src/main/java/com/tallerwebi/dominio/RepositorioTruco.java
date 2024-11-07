package com.tallerwebi.dominio;

public interface RepositorioTruco {
    void guardarPartida(Truco truco);
    Truco buscarPartidaPorId (Long id);
}
