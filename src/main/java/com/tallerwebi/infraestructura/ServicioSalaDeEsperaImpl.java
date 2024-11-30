package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicioSalaDeEsperaImpl implements ServicioSalaDeEspera {
    private final RepositorioSalaDeEspera repositorioSalaDeEspera;
    private final RepositorioJugador repositorioJugador;

    @Autowired
    public ServicioSalaDeEsperaImpl(RepositorioSalaDeEspera repositorioSalaDeEspera, RepositorioJugador repositorioJugador) {
        this.repositorioSalaDeEspera = repositorioSalaDeEspera;
        this.repositorioJugador = repositorioJugador;
    }

    @Override
    public List<SalaDeEspera> obtenerSalas() {
        return repositorioSalaDeEspera.obtenerSalas();
    }

    @Override
    public void guardarSala(SalaDeEspera salaDeEspera) {
        repositorioSalaDeEspera.guardar(salaDeEspera);
    }

    @Override
    public SalaDeEspera obtenerSalaPorId(Long idSala) {
        return repositorioSalaDeEspera.obtenerSalaPorId(idSala);
    }

    @Override
    public void actualizarSala(SalaDeEspera sala) {
        repositorioSalaDeEspera.actualizar(sala);
    }

    @Override
    public Jugador obtenerJugadorPorId(Long id) {
        return repositorioJugador.buscarPorId(id);
    }
}
