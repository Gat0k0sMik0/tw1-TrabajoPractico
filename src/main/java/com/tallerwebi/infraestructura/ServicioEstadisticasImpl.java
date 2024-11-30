package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Estadistica;
import com.tallerwebi.dominio.Jugador;
import com.tallerwebi.dominio.ServicioEstadisticas;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service("servicioEstadisticas")
@Transactional
public class ServicioEstadisticasImpl implements ServicioEstadisticas {

    private List<Estadistica> estadisticas = new ArrayList<>();
    private List<Jugador> jugadores = new ArrayList<>();
    @Override
    public List<Estadistica> obtenerTodasLasEstadisticas() {
            Estadistica estadistica1 = new Estadistica("Pepe12", 97, 150);
            Estadistica estadistica2 = new Estadistica("Tomas9", 86, 143);
            Estadistica estadistica3 = new Estadistica("Holamundo", 67, 123);
            Estadistica estadistica4 = new Estadistica("mzcc", 15, 119);

            estadisticas.add(estadistica1);
            estadisticas.add(estadistica2);
            estadisticas.add(estadistica3);
            estadisticas.add(estadistica4);

            return estadisticas;
        }

}
