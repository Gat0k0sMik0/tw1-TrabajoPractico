package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Carta;
import com.tallerwebi.dominio.Jugador;
import com.tallerwebi.dominio.ServicioTruco;
import com.tallerwebi.dominio.Truco;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicioTrucoImpl implements ServicioTruco {

    private Truco truco;

    @Autowired
    public ServicioTrucoImpl(Truco truco) {
        this.truco = truco;
    }

    @Override
    public void empezar(Jugador j1, Jugador j2) {
        truco.cargarCartasAlMazo();
        List<Carta> seisAleatorias = truco.getMazo().getSeisCartasAleatoriasSinRepetir();
        truco.getMazo().asignarCartasAJugadores(j1,j2,seisAleatorias);
    }
}
