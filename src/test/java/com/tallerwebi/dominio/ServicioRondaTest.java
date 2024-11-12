package com.tallerwebi.dominio;

import com.tallerwebi.infraestructura.ServicioRondaImpl;
import com.tallerwebi.infraestructura.ServicioTrucoImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ServicioRondaTest {


    ServicioRonda servicioRonda = new ServicioRondaImpl();

    Jugador j1 = new Jugador("gonza");
    Jugador j2 = new Jugador("leo");
    List<Jugador> jugadores = new ArrayList<>();

    @BeforeEach
    public void iniciarTest() {
        jugadores.add(j1);
        jugadores.add(j2);
    }

    @Test
    public void queSeCreeLaRonda() {
        Jugador j = new Jugador("J");
        Carta c = new Carta();
        servicioRonda.empezar();
        servicioRonda.crearRonda(j, c);
        assertThat(servicioRonda.getRondas().size(), equalTo(1));
    }

    @Test
    public void queElNombreDelQueGanoLaRondaSeaElEsperado() {
        Truco truco = new Truco();
        servicioRonda.crearRonda(j1, truco.getMazo().getCartaPorIndice(6));
        servicioRonda.crearRonda(j2, truco.getMazo().getCartaPorIndice(30));
        assertEquals(servicioRonda.getGanadorDeRondaPorNumero(0), j1.getNombre());
    }

    private void algo() {
        Truco truco = new Truco();
        List<Carta> seis = new ArrayList<>();

        seis.add(truco.getMazo().getCartaPorIndice(3)); // 4 espada xxx
        seis.add(truco.getMazo().getCartaPorIndice(6)); // 7 espada x
        seis.add(truco.getMazo().getCartaPorIndice(9)); // 12 espada xx

        seis.add(truco.getMazo().getCartaPorIndice(30)); // 1 copa x
        seis.add(truco.getMazo().getCartaPorIndice(31)); // 2 copa xx
        seis.add(truco.getMazo().getCartaPorIndice(32)); // 3 copa xxx
    }
}
