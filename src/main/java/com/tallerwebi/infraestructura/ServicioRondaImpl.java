package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Carta;
import com.tallerwebi.dominio.Jugador;
import com.tallerwebi.dominio.Ronda;
import com.tallerwebi.dominio.ServicioRonda;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ServicioRondaImpl implements ServicioRonda {

    private List<Ronda> rondas;
    private List<Jugador> ganadoresDeRonda;

    public ServicioRondaImpl() {
        this.rondas = new ArrayList<>();
        this.ganadoresDeRonda = new ArrayList<>();
    }

    @Override
    public void empezar() {

    }

    @Override
    public void crearRonda(Jugador jugador, Carta carta) {
        Ronda nueva = new Ronda(0, jugador, carta);
        rondas.add(nueva);
    }

    @Override
    public String getGanadorDeRondaPorNumero(int i) {
        List<Ronda> rondasBuscadas = getRondasPorNumero(i);
        Integer valorMasAlto = 0;
        Integer testigo = 0;
        Jugador jugadorGanador = null;
        for (Ronda ronda : rondasBuscadas) {
            if (testigo == 0) {
                valorMasAlto = ronda.getValorCarta();
                jugadorGanador = ronda.getJugador();
                testigo = 1;
            } else {
                if (ronda.getValorCarta() > valorMasAlto) {
                    jugadorGanador = ronda.getJugador();
                }
            }
        }
        return jugadorGanador.getNombre();
    }

    @Override
    public List<Ronda> getRondas() {
        return this.rondas;
    }


    private List<Ronda> getRondasPorNumero(Integer numero) {
        List<Ronda> r = new ArrayList<>();
        for (Ronda ronda : this.rondas) {
            if (ronda.getNroRonda().equals(numero)) {
                r.add(ronda);
            }
        }
        return r;
    }

}
