package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.RepositorioSalaDeEspera;
import com.tallerwebi.dominio.SalaDeEspera;
import com.tallerwebi.dominio.ServicioSalaDeEspera;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class ServicioSalaDeEsperaImpl implements ServicioSalaDeEspera {
    private final RepositorioSalaDeEspera repositorioSalaDeEspera;

    @Autowired
    public ServicioSalaDeEsperaImpl(RepositorioSalaDeEspera repositorioSalaDeEspera) {
        this.repositorioSalaDeEspera = repositorioSalaDeEspera;
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
}
