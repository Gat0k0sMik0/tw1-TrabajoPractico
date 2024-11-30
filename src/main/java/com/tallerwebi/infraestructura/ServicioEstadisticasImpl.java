package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Estadistica;
import com.tallerwebi.dominio.ServicioEstadisticas;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service("servicioEstadisticas")
@Transactional
public class ServicioEstadisticasImpl implements ServicioEstadisticas {

    private List<Estadistica> estadisticas = new ArrayList<>();
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

    public void agregarVictoria(String jugador) {
        // Buscar la estadística del jugador
        Estadistica estadistica = null;
        for (Estadistica e : estadisticas) {
            if (e.getJugador().equals(jugador)) {
                estadistica = e;
                break;
            }
        }

        // Si el jugador tiene estadísticas, sumamos una victoria
        if (estadistica != null) {
            estadistica.setNivel(estadistica.getNivel() + 1); // Aumentamos el nivel de victorias
            // Actualizamos el nivel del jugador según el número de victorias
            if (estadistica.getNivel() > 30) {
                estadistica.setPuntaje(3); // Oro
            } else if (estadistica.getNivel() > 20) {
                estadistica.setPuntaje(2); // Plata
            } else {
                estadistica.setPuntaje(1); // Bronce
            }
        } else {
            // Si el jugador no tiene estadísticas, creamos una nueva con 1 victoria (bronce)
            Estadistica nuevaEstadistica = new Estadistica(jugador, 1, 1);
            estadisticas.add(nuevaEstadistica);
        }
    }
}
