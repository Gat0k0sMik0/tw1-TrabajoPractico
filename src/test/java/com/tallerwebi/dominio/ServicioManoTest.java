package com.tallerwebi.dominio;

import com.tallerwebi.infraestructura.ServicioManoImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ServicioManoTest {

    ServicioRonda servicioRonda = mock(ServicioRonda.class);
    ServicioMano servicioMano = new ServicioManoImpl(servicioRonda);

    Jugador j1 = new Jugador("gonza");
    Jugador j2 = new Jugador("leo");
    List<Jugador> jugadores = new ArrayList<>();

    @BeforeEach
    public void iniciarTest() {
        jugadores.add(j1);
        jugadores.add(j2);
    }

    @Test
    public void queSeGuardeLaRonda() {
        // given
        Jugador j = new Jugador("J");
        Carta c = new Carta();
        List<Ronda> rondaTest = new ArrayList<>();
        rondaTest.add(new Ronda(0, j, c));
        // when
        servicioMano.guardarRonda(j, c);
        when(servicioRonda.getRondas()).thenReturn(rondaTest);
        // then
        assertThat(servicioMano.getRondas().size(), equalTo(1));
    }

    @Test
    public void queSeGuardeUnaAccion() {
        // given
        Jugador j1 = new Jugador("J1");
        // when
        Integer a = servicioMano.guardarAccion(j1, "ENVIDO", false, 0);
        // then
        assertThat(servicioMano.getAcciones().size(), equalTo(1));
        assertThat(a, equalTo(0));
    }

    @Test
    public void queSeLeGuardeUnPuntoPorHaberGanadoLaMano () {
        servicioMano.setGanadorDeMano(j1);
        assertThat(servicioMano.getGanadorDeManoActual().getPuntosPartida(), equalTo(1));
    }



}
