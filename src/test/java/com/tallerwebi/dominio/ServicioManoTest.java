package com.tallerwebi.dominio;

import com.tallerwebi.infraestructura.RepositorioManoImpl;
import com.tallerwebi.infraestructura.ServicioManoImpl2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.mock;

public class ServicioManoTest {

    RepositorioMano repositorioMano = mock(RepositorioManoImpl.class);
    ServicioMano2 servicioMano = new ServicioManoImpl2(repositorioMano);

    Jugador j1 = new Jugador();
    Jugador j2 = new Jugador();

    @BeforeEach
    void preparo() {
        j1.setNombre("gonza");
        j2.setNombre("leo");
    }


    @Test
    void queSeGuardeLaMano () {
        Truco2 t = new Truco2();
        Mano2 mano = servicioMano.empezar(t);
        assertThat(mano, notNullValue());
    }



    @Test
    public void queSeGuardeLaRonda() {
//        // given
//        Jugador j = new Jugador("J");
//        Carta c = new Carta();
//        List<Ronda> rondaTest = new ArrayList<>();
//        rondaTest.add(new Ronda(0, j, c));
//        // when
//        servicioMano.guardarRonda(j, c);
//        when(servicioRonda.getRondas()).thenReturn(rondaTest);
//        // then
//        assertThat(servicioMano.getRondas().size(), equalTo(1));
    }

    @Test
    public void queSeGuardeUnaAccion() {
//        // given
//        Jugador j1 = new Jugador("J1");
//        // when
//        Integer a = servicioMano.guardarAccion(j1, "ENVIDO", false, 0);
//        // then
//        assertThat(servicioMano.getAcciones().size(), equalTo(1));
//        assertThat(a, equalTo(0));
    }

    @Test
    public void queSeLeGuardeUnPuntoPorHaberGanadoLaMano () {
//        servicioMano.setGanadorDeMano(j1);
//        assertThat(servicioMano.getGanadorDeManoActual().getPuntosPartida(), equalTo(1));
    }



}
