package com.tallerwebi.dominio;

public interface RepositorioTruco {
    void guardarPartida(Truco2 truco);
    Truco2 buscarPartidaPorId (Long id);
}
