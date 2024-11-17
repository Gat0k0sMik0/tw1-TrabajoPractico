package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.*;
import com.tallerwebi.dominio.excepcion.TrucoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ServicioRondaImpl implements ServicioRonda2 {

    private List<Ronda2> rondas;
    private Integer contadorNroRonda;
    private Integer contadorMovimientos;
//    private List<Jugador> ganadoresDeRonda;

    @Autowired
    private RepositorioRonda2 repositorioRonda;
    @Autowired
    private RepositorioMano repositorioMano;

    public ServicioRondaImpl(RepositorioRonda2 repositorioRonda, RepositorioMano repositorioMano) {
//        this.ganadoresDeRonda = new ArrayList<>();
        this.repositorioRonda = repositorioRonda;
        this.repositorioMano = repositorioMano;
        this.rondas = new ArrayList<>();
        this.contadorNroRonda = -1;
        this.contadorMovimientos = 0;
    }

    @Override
    public Ronda2 empezar(Mano2 mano) {
        Ronda2 r = new Ronda2();
        repositorioRonda.guardar(r);
        return r;
    }

    @Override
    public void registrarRonda(Mano2 mano, Ronda2 ronda) {
        if (ronda == null) {
            throw new TrucoException("La ronda que me llegó es nula");
        }
        // ronda (nro, jug, valor_carta, nro_carta, palo_carta)
        Mano2 manoParaAgregarleRonda = this.repositorioMano.obtenerManoPorId(mano.getId());
        if (manoParaAgregarleRonda != null) {
            ronda.setNroRonda(this.contadorNroRonda);
            ronda.setMano(manoParaAgregarleRonda);
            if (contadorMovimientos++ % 2 == 0) {
                contadorNroRonda++;
            }
            ronda.setNroRonda(contadorNroRonda);
            rondas.add(ronda);
        }
    }

//    @Override
//    public void crearRonda(Jugador jugador, Carta carta) {
//        Ronda nueva = new Ronda(0, jugador, carta);
//        rondas.add(nueva);
//    }
//
//    @Override
//    public String getGanadorDeRondaPorNumero(int i) {
//        List<Ronda> rondasBuscadas = getRondasPorNumero(i);
//        Integer valorMasAlto = 0;
//        Integer testigo = 0;
//        Jugador jugadorGanador = null;
//        for (Ronda ronda : rondasBuscadas) {
//            if (testigo == 0) {
//                valorMasAlto = ronda.getValorCarta();
//                jugadorGanador = ronda.getJugador();
//                testigo = 1;
//            } else {
//                if (ronda.getValorCarta() > valorMasAlto) {
//                    jugadorGanador = ronda.getJugador();
//                }
//            }
//        }
//        return jugadorGanador.getNombre();
//    }
//
//    @Override
//    public List<Ronda> getRondas() {
//        return this.rondas;
//    }
//
//
//    private List<Ronda> getRondasPorNumero(Integer numero) {
//        List<Ronda> r = new ArrayList<>();
//        for (Ronda ronda : this.rondas) {
//            if (ronda.getNroRonda().equals(numero)) {
//                r.add(ronda);
//            }
//        }
//        return r;
//    }

}
