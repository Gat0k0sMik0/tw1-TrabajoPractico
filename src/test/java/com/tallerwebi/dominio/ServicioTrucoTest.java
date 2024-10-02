package com.tallerwebi.dominio;

import com.tallerwebi.infraestructura.ServicioTrucoImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.junit.jupiter.api.Assertions.*;

public class ServicioTrucoTest {

    ServicioTruco servicioTruco = new ServicioTrucoImpl(new Truco());
    Mazo mazo = new Mazo();


    Carta c1 = new Carta(13, 1, "Espadas");
    Carta c2 = new Carta(8, 2, "Espadas");
    Carta c3 = new Carta(9, 3, "Espadas");
    Carta c4 = new Carta(0, 4, "Espadas");
    Carta c5 = new Carta(1, 5, "Espadas");
    Carta c6 = new Carta(2, 6, "Espadas");
    Carta c7 = new Carta(11, 7, "Espadas");
    Carta c8 = new Carta(4, 10, "Espadas");
    Carta c9 = new Carta(5, 11, "Espadas");
    Carta c10 = new Carta(6, 12, "Espadas");

    Carta c11 = new Carta(7, 1, "Oros");
    Carta c12 = new Carta(8, 2, "Oros");
    Carta c13 = new Carta(9, 3, "Oros");
    Carta c14 = new Carta(0, 4, "Oros");
    Carta c15 = new Carta(1, 5, "Oros");
    Carta c16 = new Carta(2, 6, "Oros");
    Carta c17 = new Carta(10, 7, "Oros");
    Carta c18 = new Carta(4, 10, "Oros");
    Carta c19 = new Carta(5, 11, "Oros");
    Carta c20 = new Carta(6, 12, "Oros");

    Carta c21 = new Carta(12, 1, "Bastos");
    Carta c22 = new Carta(8, 2, "Bastos");
    Carta c23 = new Carta(9, 3, "Bastos");
    Carta c24 = new Carta(0, 4, "Bastos");
    Carta c25 = new Carta(1, 5, "Bastos");
    Carta c26 = new Carta(2, 6, "Bastos");
    Carta c27 = new Carta(3, 7, "Bastos");
    Carta c28 = new Carta(4, 10, "Bastos");
    Carta c29 = new Carta(5, 11, "Bastos");
    Carta c30 = new Carta(6, 12, "Bastos");

    Carta c31 = new Carta(7, 1, "Copas");
    Carta c32 = new Carta(8, 2, "Copas");
    Carta c33 = new Carta(9, 3, "Copas");
    Carta c34 = new Carta(0, 4, "Copas");
    Carta c35 = new Carta(1, 5, "Copas");
    Carta c36 = new Carta(2, 6, "Copas");
    Carta c37 = new Carta(3, 7, "Copas");
    Carta c38 = new Carta(4, 10, "Copas");
    Carta c39 = new Carta(5, 11, "Copas");
    Carta c40 = new Carta(6, 12, "Copas");

    Jugador j1 = new Jugador("gonza");
    Jugador j2 = new Jugador("leo");

    @Test
    public void revisarQueHayaCuarentaCartas() {
        assertThat("40", equalToIgnoringCase(mazo.getCantidadDeCartas().toString()));
    }

    @Test
    public void siUnaCartaSeRepiteFalla() {
        List<Carta> cartitas = new ArrayList<>();
        Boolean saber = true;
        saber = mazo.saberSiLaCartaSeRepiteEnMazo(cartitas, c1.getNumero(), c1.getPalo());
        cartitas.add(c1);
        saber = mazo.saberSiLaCartaSeRepiteEnMazo(cartitas, c1.getNumero(), c1.getPalo());
        assertTrue(saber);
    }

    @Test
    public void siUnaCartaNoSeRepiteNoFalla() {
        List<Carta> cartitas = new ArrayList<>();
        cartitas.add(c1);
        Boolean saber = mazo.saberSiLaCartaSeRepiteEnMazo(cartitas, c2.getNumero(), c2.getPalo());
        assertFalse(saber);
    }


    @Test
    public void obtenerSeisCartasYQueNoSeRepitan() {
        List<Carta> seis = mazo.getSeisCartasAleatoriasSinRepetir();
        int recibido = seis.size();
        int esperado = 6;
        Boolean sonIguales = esperado == recibido;
        assertTrue(sonIguales);
    }

    @Test
    public void queSeAsignenTresCartasACadaJugador() {
        List<Carta> seis = mazo.getSeisCartasAleatoriasSinRepetir();
        mazo.asignarCartasAJugadores(j1, j2, seis);
        Boolean esperadoEstaOk = j1.getCartas().size() == j2.getCartas().size();
        assertTrue(esperadoEstaOk);
        assertEquals(3, j1.getCartas().size());
        assertEquals(3, j2.getCartas().size());
    }

    @Test
    public void queUnJugadorPuedaTirarUnaCarta() {
//        List<Carta> seis = mazo.getSeisCartasAleatoriasSinRepetir();
//        mazo.asignarCartasAJugadores(j1, j2, seis);
//        List<Carta> tiradas = new ArrayList<>();
//        Carta tiradaDelJugador = j1.tirarCarta(0);
//        tiradas.add(tiradaDelJugador);
//        assertEquals(2, j1.getCartas().size());
//        assertEquals(1, tiradas.size());
    }


    @Test
    public void queSeRegistreElMovimiento() {
        // given
//        List<Carta> seis = mazo.getSeisCartasAleatoriasSinRepetir();
        List<Carta> seis = new ArrayList<>();
        seis.add(c7);
        seis.add(c8);
        seis.add(c10);
        seis.add(c31);
        seis.add(c32);
        seis.add(c33);
        mazo.asignarCartasAJugadores(j1, j2, seis);
        Truco truco = new Truco();
        // when
        Carta cartaTirada = j1.tirarCarta(c7);
        truco.registrarMovimiento(j1, cartaTirada);
        // then
        assertEquals(1, truco.getRondasJugadas().size());
    }

    @Test
    public void queLaCartaQueTiroElJugadorUnoSeaLaEsperada() {
        // given
//        List<Carta> seis = mazo.getSeisCartasAleatoriasSinRepetir();
        List<Carta> seis = new ArrayList<>();
        seis.add(c7);
        seis.add(c8);
        seis.add(c10);
        seis.add(c31);
        seis.add(c32);
        seis.add(c33);
        mazo.asignarCartasAJugadores(j1, j2, seis);
        Truco truco = new Truco();
        // when
        Carta cartaTirada = j1.tirarCarta(c7);
        truco.registrarMovimiento(j1, cartaTirada);
        // then
        assertEquals(truco.getRondasJugadas().get(0).getNroCarta(), cartaTirada.getNumero());
    }

    @Test
    public void queSeHayanHechoDosRondas() {
        // given
//        List<Carta> seis = mazo.getSeisCartasAleatoriasSinRepetir();
        List<Carta> seis = new ArrayList<>();
        seis.add(c7);
        seis.add(c8);
        seis.add(c10);

        seis.add(c31);
        seis.add(c32);
        seis.add(c33);
        mazo.asignarCartasAJugadores(j1, j2, seis);
        Truco truco = new Truco();
        // when
        Carta c1 = j1.tirarCarta(c7);
        Carta c2 = j2.tirarCarta(c31);
        truco.registrarMovimiento(j1, c1);
        truco.registrarMovimiento(j2, c2);
        // then
        assertEquals(2, truco.getRondasJugadas().size());
        assertEquals(2, truco.getNroMovimiento());
    }

    @Test
    public void saberQuienGanoLaPrimeraRonda() {
        // given
//        List<Carta> seis = mazo.getSeisCartasAleatoriasSinRepetir();
        List<Carta> seis = new ArrayList<>();
        seis.add(c7);
        seis.add(c8);
        seis.add(c10);

        seis.add(c31);
        seis.add(c32);
        seis.add(c33);
        mazo.asignarCartasAJugadores(j1, j2, seis);
        Truco truco = new Truco();
        // when
        Carta c1 = j1.tirarCarta(c7);
        Carta c2 = j2.tirarCarta(c31);

        truco.registrarMovimiento(j1, c1);
        truco.registrarMovimiento(j2, c2);
        Jugador ganadorRonda = truco.saberQuienGanoNumeroDeRonda(0);
        // then
        assertEquals(j1.getNombre(), ganadorRonda.getNombre());
    }

    @Test
    public void queLaPrimeraRondaLaGaneElPrimeroYLaSegundaElSegundo() {
        // given
//        List<Carta> seis = mazo.getSeisCartasAleatoriasSinRepetir();
        List<Carta> seis = new ArrayList<>();
        seis.add(c4); // 4 espada xxx
        seis.add(c7); // 7 espada x
        seis.add(c10); // 12 espada xx

        seis.add(c31); // 1 copa x
        seis.add(c32); // 2 copa xx
        seis.add(c33); // 3 copa xxx
        mazo.asignarCartasAJugadores(j1, j2, seis);
        Truco truco = new Truco();

        // when
        Carta c1 = j1.tirarCarta(c7);
        Carta c2 = j2.tirarCarta(c31);

        Carta c3 = j1.tirarCarta(c10);
        Carta c4 = j2.tirarCarta(c32);

        truco.registrarMovimiento(j1, c1);
        truco.registrarMovimiento(j2, c2);

        truco.registrarMovimiento(j1, c3);
        truco.registrarMovimiento(j2, c4);

        Jugador ganadorRonda1 = truco.saberQuienGanoNumeroDeRonda(0);
        Jugador ganadorRonda2 = truco.saberQuienGanoNumeroDeRonda(1);
        // then

        assertEquals(j1.getNombre(), ganadorRonda1.getNombre());
        assertEquals(j2.getNombre(), ganadorRonda2.getNombre());
    }

    @Test
    public void queElJugadorDosGaneDosDeTresRondas() {
        // given
        List<Carta> seis = givenAsignoCartasALosJugadores();
        mazo.asignarCartasAJugadores(j1, j2, seis);
        Truco truco = new Truco();

        // when
        whenJuegoUnaMano(truco);

        Jugador ganadorRonda1 = truco.saberQuienGanoNumeroDeRonda(0);
        Jugador ganadorRonda2 = truco.saberQuienGanoNumeroDeRonda(1);
        Jugador ganadorRonda3 = truco.saberQuienGanoNumeroDeRonda(2);
        // then

        assertEquals(j1.getNombre(), ganadorRonda1.getNombre());
        assertEquals(j2.getNombre(), ganadorRonda2.getNombre());
        assertEquals(j2.getNombre(), ganadorRonda2.getNombre());
    }

    @Test
    public void saberQuienGanoLaPrimeraMano() {
        // given
        List<Carta> seis = givenAsignoCartasALosJugadores();
        mazo.asignarCartasAJugadores(j1, j2, seis);
        Truco truco = new Truco();

        // when
        whenJuegoUnaMano(truco);

        Jugador ganadorMano = truco.saberQuienGanoLaPrimeraMano(j1, j2);
        // then

        assertEquals(j2.getNombre(), ganadorMano.getNombre());

    }

    @Test
    public void queSeRegistreQuienGanoLaPrimeraMano() {
        // given
        List<Carta> seis = givenAsignoCartasALosJugadores();
        mazo.asignarCartasAJugadores(j1, j2, seis);
        Truco truco = new Truco();

        // when
        whenJuegoUnaMano(truco);

        Jugador ganadorMano = truco.saberQuienGanoLaPrimeraMano(j1, j2);
        truco.guardarGanadorDeMano(ganadorMano, 1);
        // then

        assertEquals(1, truco.getManosDePartida().size());

    }

    @Test
    public void queSeJueguenTresManosYQueElGanadorSeaElEsperado() {
        // given
        List<Carta> seis = givenAsignoCartasALosJugadores();
        mazo.asignarCartasAJugadores(j1, j2, seis);
        Truco truco = new Truco();
        // when
        whenJuegoUnaMano(truco);
        whenJuegoUnaMano(truco);
        whenJuegoUnaMano(truco);

//        Jugador ganadorMano = truco.saberQuienGanoLaPrimeraMano(j1, j2);
//        truco.guardarGanadorDeMano(ganadorMano, 1);
        // then

        assertEquals(j2.getNombre(), truco.saberQuienSumoMasPuntosEnLasManos(j1, j2).getNombre());

    }

    private List<Carta> givenAsignoCartasALosJugadores () {
        List<Carta> seis = new ArrayList<>();
        seis.add(c4); // 4 espada xxx
        seis.add(c7); // 7 espada x
        seis.add(c10); // 12 espada xx
        seis.add(c31); // 1 copa x
        seis.add(c32); // 2 copa xx
        seis.add(c33); // 3 copa xxx
        return seis;
    }

    private void whenJuegoUnaMano(Truco truco) {
        Carta c1 = j1.tirarCarta(c7);
        Carta c2 = j2.tirarCarta(c31);
        Carta c3 = j1.tirarCarta(c10);
        Carta c4a = j2.tirarCarta(c32);
        Carta c5 = j1.tirarCarta(c4);
        Carta c6 = j2.tirarCarta(c33);
        truco.registrarMovimiento(j1, c1);
        truco.registrarMovimiento(j2, c2);
        truco.registrarMovimiento(j1, c3);
        truco.registrarMovimiento(j2, c4a);
        truco.registrarMovimiento(j1, c5);
        truco.registrarMovimiento(j2, c6);
    }


}
