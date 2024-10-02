package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.IndiceFueraDeRangoException;
import com.tallerwebi.infraestructura.ServicioTrucoImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

public class ServicioTrucoTest {

    ServicioTruco servicioTruco = new ServicioTrucoImpl(new Truco());
    Jugador j1 = new Jugador("gonza");
    Jugador j2 = new Jugador("leo");

    @Test
    public void queSeCreeElTrucoYSeCarguenLasCartas() {
        Truco truco = new Truco();
        truco.cargarCartasAlMazo();
        assertThat("40", equalToIgnoringCase(truco.getMazo().getCantidadDeCartas().toString()));
    }

    @Test
    public void siUnaCartaSeRepiteFalla() {
        List<Carta> cartitas = new ArrayList<>();
        Truco truco = new Truco();
        truco.cargarCartasAlMazo();
        Carta c1 = truco.getMazo().getCartaRandom();
        truco.getMazo().saberSiLaCartaSeRepiteEnMazo(cartitas, c1.getNumero(), c1.getPalo());
        cartitas.add(c1);
        Boolean saber = truco.getMazo().saberSiLaCartaSeRepiteEnMazo(cartitas, c1.getNumero(), c1.getPalo());
        assertTrue(saber);
    }

    @Test
    public void siUnaCartaNoSeRepiteNoFalla() throws IndiceFueraDeRangoException {
        List<Carta> cartitas = new ArrayList<>();
        Truco truco = new Truco();
        truco.cargarCartasAlMazo();
        Carta c1 = truco.getMazo().getCartaPorIndice(0);
        Carta c2 = truco.getMazo().getCartaPorIndice(1);
        cartitas.add(c1);
        Boolean saber = truco.getMazo().saberSiLaCartaSeRepiteEnMazo(cartitas, c2.getNumero(), c2.getPalo());
        assertFalse(saber);
    }


    @Test
    public void queLasCartasObtenidasSeanLasEsperadas() {
        Truco truco = new Truco();
        List<Carta> seis = truco.getMazo().getSeisCartasAleatoriasSinRepetir();
        int recibido = seis.size();
        int esperado = 6;
        Boolean sonIguales = esperado == recibido;
        assertTrue(sonIguales);
    }

    @Test
    public void queSeAsignenTresCartasACadaJugador() {
        Truco truco = new Truco();
        List<Carta> seis = truco.getMazo().getSeisCartasAleatoriasSinRepetir();
        truco.getMazo().asignarCartasAJugadores(j1, j2, seis);
        Boolean esperadoEstaOk = j1.getCartas().size() == j2.getCartas().size();
        assertTrue(esperadoEstaOk);
        assertEquals(3, j1.getCartas().size());
        assertEquals(3, j2.getCartas().size());
    }

    @Test
    public void queLaCartaATirarNoSeaNula() throws IndiceFueraDeRangoException {
        Truco truco = new Truco();
        List<Carta> seis = givenSeCarganSeisCartas(truco);
        truco.getMazo().asignarCartasAJugadores(j1, j2, seis);
        Carta tiradaDelJugador = j1.tirarCarta(truco.buscarCartaPorNumeroYPalo(7, "Espadas"));
        assertThat(tiradaDelJugador, notNullValue());
    }

    @Test
    public void queLaCartaATirarSeaNula() throws IndiceFueraDeRangoException {
        Truco truco = new Truco();
        List<Carta> seis = givenSeCarganSeisCartas(truco);
        truco.getMazo().asignarCartasAJugadores(j1, j2, seis);
        Carta tiradaDelJugador = j1.tirarCarta(truco.buscarCartaPorNumeroYPalo(2, "Espadas"));
        assertThat(tiradaDelJugador, nullValue());
    }

    @Test
    public void queUnJugadorPuedaTirarUnaCarta() throws IndiceFueraDeRangoException {
        Truco truco = new Truco();
        List<Carta> seis = givenSeCarganSeisCartas(truco);
        truco.getMazo().asignarCartasAJugadores(j1, j2, seis);
        List<Carta> tiradas = new ArrayList<>();
        Carta tiradaDelJugador = j1.tirarCarta(truco.buscarCartaPorNumeroYPalo(7, "Espadas"));
        tiradas.add(tiradaDelJugador);
        assertEquals(2, j1.getCartas().size());
        assertEquals(1, tiradas.size());
    }


    @Test
    public void queSeRegistreElMovimiento() throws IndiceFueraDeRangoException {
        // given
        Truco truco = new Truco();
        List<Carta> seis = givenSeCarganSeisCartas(truco);
        truco.getMazo().asignarCartasAJugadores(j1, j2, seis);
        // when
        Carta cartaTirada = j1.tirarCarta(truco.buscarCartaPorNumeroYPalo(12, "Espadas"));
        truco.registrarMovimiento(j1, cartaTirada);
        // then
        assertEquals(1, truco.getRondasJugadas().size());
    }

    @Test
    public void queLaCartaQueTiroElJugadorUnoSeaLaEsperada() throws IndiceFueraDeRangoException {
        // given
        Truco truco = new Truco();
        List<Carta> seis = givenSeCarganSeisCartas(truco);
        truco.getMazo().asignarCartasAJugadores(j1, j2, seis);
        // when
        Carta cartaTirada = j1.tirarCarta(truco.buscarCartaPorNumeroYPalo(12, "Espadas"));
        truco.registrarMovimiento(j1, cartaTirada);
        // then
        assertEquals(truco.getRondasJugadas().get(0).getNroCarta(), cartaTirada.getNumero());
    }

    public List<Carta> givenSeCarganSeisCartas(Truco truco) throws IndiceFueraDeRangoException {
        List<Carta> seis = new ArrayList<>();
        seis.add(truco.getMazo().getCartaPorIndice(6));
        seis.add(truco.getMazo().getCartaPorIndice(7));
        seis.add(truco.getMazo().getCartaPorIndice(9));
        seis.add(truco.getMazo().getCartaPorIndice(30));
        seis.add(truco.getMazo().getCartaPorIndice(31));
        seis.add(truco.getMazo().getCartaPorIndice(32));
        return seis;
    }

    @Test
    public void queSeHayanHechoDosRondas() throws IndiceFueraDeRangoException {
        // given
        Truco truco = new Truco();
        List<Carta> seis = givenSeCarganSeisCartas(truco);
        truco.getMazo().asignarCartasAJugadores(j1, j2, seis);
        // when
        Carta c1 = j1.tirarCarta(truco.buscarCartaPorNumeroYPalo(7, "Espadas"));
        Carta c2 = j2.tirarCarta(truco.buscarCartaPorNumeroYPalo(1, "Copas"));
        truco.registrarMovimiento(j1, c1);
        truco.registrarMovimiento(j2, c2);
        // then
        assertEquals(2, truco.getRondasJugadas().size());
        assertEquals(2, truco.getNroMovimiento());
    }

    @Test
    public void saberQuienGanoLaPrimeraRonda() throws IndiceFueraDeRangoException {
        // given
        Truco truco = new Truco();
        List<Carta> seis = givenSeCarganSeisCartas(truco);
        truco.getMazo().asignarCartasAJugadores(j1, j2, seis);
        // when
        Carta c1 = j1.tirarCarta(truco.buscarCartaPorNumeroYPalo(7, "Espadas"));
        Carta c2 = j2.tirarCarta(truco.buscarCartaPorNumeroYPalo(1, "Copas"));

        truco.registrarMovimiento(j1, c1);
        truco.registrarMovimiento(j2, c2);
        Jugador ganadorRonda = truco.saberQuienGanoNumeroDeRonda(0);
        // then
        assertEquals(j1.getNombre(), ganadorRonda.getNombre());
    }

    @Test
    public void queLaPrimeraRondaLaGaneElPrimeroYLaSegundaElSegundo() throws IndiceFueraDeRangoException {
        // given
        Truco truco = new Truco();
        List<Carta> seis = givenSeCarganSeisCartas(truco);
        truco.getMazo().asignarCartasAJugadores(j1, j2, seis);
        // when
        Carta c1 = j1.tirarCarta(truco.buscarCartaPorNumeroYPalo(7, "Espadas"));
        Carta c2 = j2.tirarCarta(truco.buscarCartaPorNumeroYPalo(1, "Copas"));
        Carta c3 = j1.tirarCarta(truco.buscarCartaPorNumeroYPalo(12, "Espadas"));
        Carta c4 = j2.tirarCarta(truco.buscarCartaPorNumeroYPalo(2, "Copas"));
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
    public void queElJugadorDosGaneDosDeTresRondas() throws IndiceFueraDeRangoException {
        // given
        Truco truco = new Truco();
        List<Carta> seis = givenAsignoCartasALosJugadores();
        truco.getMazo().asignarCartasAJugadores(j1, j2, seis);
        // when
        whenJuegoUnaMano(truco);
        Jugador ganadorRonda1 = truco.saberQuienGanoNumeroDeRonda(0);
        Jugador ganadorRonda2 = truco.saberQuienGanoNumeroDeRonda(1);
        Jugador ganadorRonda3 = truco.saberQuienGanoNumeroDeRonda(2);
        // then
        assertEquals(j1.getNombre(), ganadorRonda1.getNombre());
        assertEquals(j2.getNombre(), ganadorRonda2.getNombre());
        assertEquals(j2.getNombre(), ganadorRonda3.getNombre());
    }

    @Test
    public void saberQuienGanoLaPrimeraMano() throws IndiceFueraDeRangoException {
        // given
        Truco truco = new Truco();
        List<Carta> seis = givenAsignoCartasALosJugadores();
        truco.getMazo().asignarCartasAJugadores(j1, j2, seis);
        // when
        whenJuegoUnaMano(truco);
        Jugador ganadorMano = truco.saberQuienGanoLaPrimeraMano(j1, j2);
        // then
        assertEquals(j2.getNombre(), ganadorMano.getNombre());
    }

    @Test
    public void queSeRegistreQuienGanoLaPrimeraMano() throws IndiceFueraDeRangoException {
        // given
        Truco truco = new Truco();
        List<Carta> seis = givenAsignoCartasALosJugadores();
        truco.getMazo().asignarCartasAJugadores(j1, j2, seis);
        // when
        whenJuegoUnaMano(truco);
        Jugador ganadorMano = truco.saberQuienGanoLaPrimeraMano(j1, j2);
        truco.guardarGanadorDeMano(ganadorMano, 1);
        // then
        assertEquals(1, truco.getManosDePartida().size());

    }

    @Test
    public void queSeJueguenTresManosYQueElGanadorSeaElEsperado() throws IndiceFueraDeRangoException {
        // given
        Truco truco = new Truco();
        List<Carta> seis = givenAsignoCartasALosJugadores();
        truco.getMazo().asignarCartasAJugadores(j1, j2, seis);
        // when
        whenJuegoUnaMano(truco);
        truco.getMazo().asignarCartasAJugadores(j1, j2, seis);
        whenJuegoUnaMano(truco);
        truco.getMazo().asignarCartasAJugadores(j1, j2, seis);
        whenJuegoUnaMano(truco);
        // then
        assertEquals(j2.getNombre(), truco.saberQuienSumoMasPuntosEnLasManos(j1, j2).getNombre());

    }

    private List<Carta> givenAsignoCartasALosJugadores() throws IndiceFueraDeRangoException {
        List<Carta> seis = new ArrayList<>();
        Truco truco = new Truco();
        seis.add(truco.getMazo().getCartaPorIndice(3)); // 4 espada xxx
        seis.add(truco.getMazo().getCartaPorIndice(6)); // 7 espada x
        seis.add(truco.getMazo().getCartaPorIndice(9)); // 12 espada xx
        seis.add(truco.getMazo().getCartaPorIndice(30)); // 1 copa x
        seis.add(truco.getMazo().getCartaPorIndice(31)); // 2 copa xx
        seis.add(truco.getMazo().getCartaPorIndice(32)); // 3 copa xxx
        return seis;
    }

    private void whenJuegoUnaMano(Truco truco) {
        Carta ct1 = j1.tirarCarta(truco.buscarCartaPorNumeroYPalo(7, "Espadas"));
        Carta ct2 = j2.tirarCarta(truco.buscarCartaPorNumeroYPalo(1, "Copas"));

        Carta ct3 = j1.tirarCarta(truco.buscarCartaPorNumeroYPalo(12, "Espadas"));
        Carta ct4 = j2.tirarCarta(truco.buscarCartaPorNumeroYPalo(2, "Copas"));

        Carta ct5 = j1.tirarCarta(truco.buscarCartaPorNumeroYPalo(4, "Espadas"));
        Carta ct6 = j2.tirarCarta(truco.buscarCartaPorNumeroYPalo(3, "Copas"));

        truco.registrarMovimiento(j1, ct1);
        truco.registrarMovimiento(j2, ct2);
        truco.registrarMovimiento(j1, ct3);
        truco.registrarMovimiento(j2, ct4);
        truco.registrarMovimiento(j1, ct5);
        truco.registrarMovimiento(j2, ct6);
    }


}
