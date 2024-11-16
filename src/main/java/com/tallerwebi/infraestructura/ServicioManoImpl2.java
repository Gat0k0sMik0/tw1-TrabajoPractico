package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Mano2;
import com.tallerwebi.dominio.RepositorioMano;
import com.tallerwebi.dominio.ServicioMano2;
import com.tallerwebi.dominio.Truco2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServicioManoImpl2 implements ServicioMano2 {

    @Autowired
    RepositorioMano repositorioMano;

    public ServicioManoImpl2(RepositorioMano repositorioMano) {
        this.repositorioMano = repositorioMano;
    }

    @Override
    public Mano2 empezar(Truco2 t) {
        Mano2 m = new Mano2();
        repositorioMano.guardar(m);
        return m;
    }

    @Override
    public Mano2 obtenerManoPorId(Long id) {
        Mano2 buscada = repositorioMano.obtenerManoPorId(id);
        if (buscada != null) {
            return buscada;
        }
        return null;
    }
}
