package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.IndiceFueraDeRangoException;
import com.tallerwebi.infraestructura.RepositorioCartaImpl;
import com.tallerwebi.infraestructura.ServicioTrucoImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class ServicioTrucoTest {

    RepositorioCartaImpl repositorioCarta = mock(RepositorioCartaImpl.class);
    ServicioTruco servicioTruco = new ServicioTrucoImpl(repositorioCarta);

    Jugador j1 = new Jugador("gonza");
    Jugador j2 = new Jugador("leo");
    List<Jugador> jugadores = new ArrayList<>();

    @BeforeEach
    public void iniciarTest () {
        jugadores.add(j1);
        jugadores.add(j2);
    }

    
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
        truco.getMazo().asignarCartasAJugadores(jugadores, seis);
        Boolean esperadoEstaOk = j1.getCartas().size() == j2.getCartas().size();
        assertTrue(esperadoEstaOk);
        assertEquals(3, j1.getCartas().size());
        assertEquals(3, j2.getCartas().size());
    }

    @Test
    public void queLaCartaATirarNoSeaNula() throws IndiceFueraDeRangoException {
        Truco truco = new Truco();
        List<Carta> seis = givenSeCarganSeisCartas(truco);
        truco.getMazo().asignarCartasAJugadores(jugadores, seis);
        Carta tiradaDelJugador = j1.tirarCarta(truco.buscarCartaPorNumeroYPalo(7, "Espadas"));
        assertThat(tiradaDelJugador, notNullValue());
    }

    @Test
    public void queLaCartaATirarSeaNula() throws IndiceFueraDeRangoException {
        Truco truco = new Truco();
        List<Carta> seis = givenSeCarganSeisCartas(truco);
        truco.getMazo().asignarCartasAJugadores(jugadores, seis);
        Carta tiradaDelJugador = j1.tirarCarta(truco.buscarCartaPorNumeroYPalo(2, "Espadas"));
        assertThat(tiradaDelJugador, nullValue());
    }

    @Test
    public void queUnJugadorPuedaTirarUnaCarta() throws IndiceFueraDeRangoException {
        Truco truco = new Truco();
        List<Carta> seis = givenSeCarganSeisCartas(truco);
        truco.getMazo().asignarCartasAJugadores(jugadores, seis);
        List<Carta> tiradas = new ArrayList<>();
        Carta tiradaDelJugador = j1.tirarCarta(truco.buscarCartaPorNumeroYPalo(7, "Espadas"));
        tiradas.add(tiradaDelJugador);
        assertEquals(2, j1.getCartas().size());
        assertEquals(1, tiradas.size());
    }


    @Test
    public void queSeRegistreElMovimiento() throws IndiceFueraDeRangoException {
        // given
        List<Carta> seis = givenAsignoCartasALosJugadores(); // cartas que yo quiero
        servicioTruco.empezar(jugadores, seis); // truco, asigna, empieza
        Truco truco = servicioTruco.getTruco();
        // when
        whenJuegoUnaMano(truco, seis);
        // then
        assertEquals(6, truco.getRondasDeManoActual().size());
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
        List<Carta> seis = givenAsignoCartasALosJugadores(); // cartas que yo quiero
        servicioTruco.empezar(jugadores, seis); // truco, asigna, empieza
        Truco truco = servicioTruco.getTruco();
        // when
        whenJuegoDosRondas(truco, seis);
        // then
        assertEquals(2, truco.getNumeroDeRondasJugadasDeLaManoActual());
        assertEquals(4, truco.getMovimientosDeLaManoActual());
    }

    @Test
    public void queElNumeroDeRondasSeaElEsperado() throws IndiceFueraDeRangoException {
        // given
        List<Carta> seis = givenAsignoCartasALosJugadores(); // cartas que yo quiero
        servicioTruco.empezar(jugadores, seis); // truco, asigna, empieza
        Truco truco = servicioTruco.getTruco();
        // when
        whenJuegoUnaMano(truco, seis);
        // then
        assertEquals(3, truco.getNumeroDeRondasJugadasDeLaManoActual());
        assertEquals(6, truco.getMovimientosDeLaManoActual());
    }

    private void whenJuegoDosRondas(Truco truco, List<Carta> seis) {
        j1.tirarCarta(seis.get(0));
        truco.registrarJugada(j1, seis.get(0));
        servicioTruco.determinarGanadorRonda(j1,j2);
        j2.tirarCarta(seis.get(3));
        truco.registrarJugada(j1, seis.get(3));
        servicioTruco.determinarGanadorRonda(j1,j2);
        j2.tirarCarta(seis.get(4));
        truco.registrarJugada(j1, seis.get(4));
        servicioTruco.determinarGanadorRonda(j1,j2);
        j1.tirarCarta(seis.get(1));
        truco.registrarJugada(j1, seis.get(1));
        servicioTruco.determinarGanadorRonda(j1,j2);
    }

    @Test
    public void saberQuienGanoLaPrimeraRonda() throws IndiceFueraDeRangoException {
        // given
        List<Carta> seis = givenAsignoCartasALosJugadores(); // cartas que yo quiero
        servicioTruco.empezar(jugadores, seis); // truco, asigna, empieza
        Truco truco = servicioTruco.getTruco();
        // when
        whenJuegoDosRondas(truco, seis);
        // then
        assertEquals("leo", truco.getGanadorDeRondaDeManoActualPorNumero(0));
    }

    @Test
    public void queLaPrimeraRondaLaGaneElPrimeroYLaSegundaElSegundo() throws IndiceFueraDeRangoException {
        List<Carta> seis = givenAsignoCartasALosJugadores(); // cartas que yo quiero
        servicioTruco.empezar(jugadores, seis); // truco, asigna, empieza
        Truco truco = servicioTruco.getTruco();
        // when
        whenJuegoDosRondas(truco, seis);
        // then
        assertEquals("leo", truco.getGanadorDeRondaDeManoActualPorNumero(0));
        assertEquals("gonza", truco.getGanadorDeRondaDeManoActualPorNumero(1));
    }

    @Test
    public void queElUltimoGanadorDeUnaManoNoSeaNulo() throws IndiceFueraDeRangoException {
        List<Carta> seis = givenAsignoCartasALosJugadores(); // cartas que yo quiero
        servicioTruco.empezar(jugadores, seis); // truco, asigna, empieza
        Truco truco = servicioTruco.getTruco();
        // when
        whenJuegoUnaMano(truco, seis);
        // then
        assertThat(truco.getGanadorDeManoPorNumero(0), notNullValue());
        assertThat(truco.getManosDePartida().size(), is(1));
        assertThat(truco.getRondasDeManoActual().size(), is(6));
    }

    @Test
    public void queElJugadorDosGaneDosDeTresRondas() throws IndiceFueraDeRangoException {
        List<Carta> seis = givenAsignoCartasALosJugadores(); // cartas que yo quiero
        servicioTruco.empezar(jugadores, seis); // truco, asigna, empieza
        Truco truco = servicioTruco.getTruco();
        // when
        whenJuegoUnaMano(truco, seis);
        // then
        assertEquals("leo", truco.getGanadorDeRondaDeManoActualPorNumero(0));
        assertEquals("gonza", truco.getGanadorDeRondaDeManoActualPorNumero(1));
        assertEquals("leo", truco.getGanadorDeRondaDeManoActualPorNumero(2));
        assertEquals("leo", truco.getGanadorDeManoPorNumero(0).getNombre());
    }


    @Test
    public void queSeGuardeUnaManoAlJugarTresRondas() throws IndiceFueraDeRangoException {
        // given
        List<Carta> seis = givenAsignoCartasALosJugadores(); // cartas que yo quiero
        servicioTruco.empezar(jugadores, seis); // truco, asigna, empieza
        Truco truco = servicioTruco.getTruco();
        // when
        whenJuegoUnaMano(truco, seis);
        // then
        assertEquals(1, truco.getManosDePartida().size());
    }

    @Test
    public void queSeGuardenVariasManos() throws IndiceFueraDeRangoException {
        // given
        List<Carta> seis = givenAsignoCartasALosJugadores();
        servicioTruco.empezar(jugadores, seis);
        Truco truco = servicioTruco.getTruco();
        truco.empezarMano(jugadores);
        // when
        truco.asignarCartasJugadores(jugadores, seis);
        truco.empezarMano(jugadores);
        whenJuegoUnaMano(truco, seis);
        truco.asignarCartasJugadores(jugadores, seis);
        truco.empezarMano(jugadores);
        whenJuegoUnaMano(truco, seis);
        truco.asignarCartasJugadores(jugadores, seis);
        truco.empezarMano(jugadores);
        whenJuegoUnaMano(truco, seis);
        // then
        assertEquals(3, truco.getManosDePartida().size());
    }

    @Test
    public void queSeGuardeUnaManoJugada() throws IndiceFueraDeRangoException {
        // given
        List<Carta> seis = givenAsignoCartasALosJugadores();
        servicioTruco.empezar(jugadores, seis);
        Truco truco = servicioTruco.getTruco();
        truco.empezarMano(jugadores);
        // when
        whenJuegoUnaMano(truco, seis);
        // then
        assertThat(truco.getManosDePartida().get(0).getJugador(), notNullValue());
    }


    @Test
    public void queObtenerElGanadorDeLaPrimeraManoNoSeaNulo() throws IndiceFueraDeRangoException {
        // given
        List<Carta> seis = givenAsignoCartasALosJugadores();
        servicioTruco.empezar(jugadores, seis);
        Truco truco = servicioTruco.getTruco();
        truco.empezarMano(jugadores);
        // when
        whenJuegoUnaMano(truco, seis);
        Jugador ganadorMano = truco.getGanadorDeManoPorNumero(0);
        // then
        assertThat(ganadorMano, notNullValue());
    }

    @Test
    public void queSeJueguenTresManosYQueElGanadorSeaElEsperado() throws IndiceFueraDeRangoException {
        // given
        List<Carta> seis = givenAsignoCartasALosJugadores();
        servicioTruco.empezar(jugadores, seis);
        Truco truco = servicioTruco.getTruco();
        truco.empezarMano(jugadores);
        // when
        servicioTruco.empezar(jugadores, seis);
        truco.empezarMano(jugadores);
        servicioTruco.empezar(jugadores, seis);
        truco.empezarMano(jugadores);
        servicioTruco.empezar(jugadores, seis);
        truco.empezarMano(jugadores);
        // then
        assertEquals(j2.getNombre(), truco.saberQuienSumoMasPuntosEnLasManos(j1, j2).getNombre());
    }

    @Test
    public void calcularSiTieneParaEnvidoDosCartasAlMenos () throws IndiceFueraDeRangoException {
        List<Carta> seis = givenAsignoCartasALosJugadores();
        servicioTruco.empezar(jugadores, seis);
        Truco truco = servicioTruco.getTruco();
        seis.clear();
        seis.add(truco.getMazo().getCartaPorIndice(3)); // 4 espada xxx
        seis.add(truco.getMazo().getCartaPorIndice(6)); // 7 espada x
        seis.add(truco.getMazo().getCartaPorIndice(9)); // 12 espada xx
//        assertTrue(truco.calcularEnvido(seis));
    }

    @Test
    public void obtenerValoresDeCartaParaEnvido () throws IndiceFueraDeRangoException {
        List<Carta> seis = givenAsignoCartasALosJugadores();
        servicioTruco.empezar(jugadores, seis);
        Truco truco = servicioTruco.getTruco();
        seis.clear();
        seis.add(truco.getMazo().getCartaPorIndice(3)); // 4 espada xxx
        seis.add(truco.getMazo().getCartaPorIndice(6)); // 7 espada x
        seis.add(truco.getMazo().getCartaPorIndice(9)); // 12 espada xx
        for (Carta c : seis) {
            if (c.getNumero() >= 10 && c.getNumero() <= 12) {
                c.setValorEnvido(0);
            } else {
                c.setValorEnvido(c.getNumero());
            }
        }
        assertEquals(3, truco.tieneDosDelMismoPalo(seis).size());
        assertEquals(31, (int) truco.obtenerSumaDeLasMasAltas(seis));
        assertEquals(31, servicioTruco.calcularTantosDeCartasDeUnJugador(j1));
    }

    @Test void queSeLeSumePuntosAlAceptarYGanarEnvido () throws IndiceFueraDeRangoException {
//        // given
//        List<Carta> seis = givenAsignoCartasALosJugadores();
//        servicioTruco.empezar(jugadores, seis);
//        Truco truco = servicioTruco.getTruco();
//        truco.empezarMano(jugadores);
//        Jugador ganadorEnvido = null;
//        // when
//        Integer nroAccion = servicioTruco.accion("ENVIDO", j1, j2, 2);
//        servicioTruco.actualizarRespuestaDeAccion(nroAccion, true);
//        Integer tantoJ1 = servicioTruco.calcularTantosDeCartasDeUnJugador(j1);
//        Integer tantoJ2 = servicioTruco.calcularTantosDeCartasDeUnJugador(j2);
//
//        if (tantoJ2 > tantoJ1) {
//            ganadorEnvido = j1;
//        } else {
//            ganadorEnvido = j2;
//        }
//        servicioTruco.guardarPuntos(ganadorEnvido, 2);
//        // then
//        assertEquals(31, tantoJ1);
//        assertEquals(25, tantoJ2);
//        assertEquals(2, servicioTruco.getPuntosDeUnJugador(j2));
//        assertEquals(0, servicioTruco.getPuntosDeUnJugador(j1));
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

    private void whenJuegoUnaMano(Truco truco, List<Carta> seis) {
        j1.tirarCarta(seis.get(0));
        truco.registrarJugada(j1, seis.get(0));
        servicioTruco.determinarGanadorRonda(j1,j2);
        j2.tirarCarta(seis.get(3));
        truco.registrarJugada(j1, seis.get(3));
        servicioTruco.determinarGanadorRonda(j1,j2);

        j2.tirarCarta(seis.get(4));
        truco.registrarJugada(j1, seis.get(4));
        servicioTruco.determinarGanadorRonda(j1,j2);
        j1.tirarCarta(seis.get(1));
        truco.registrarJugada(j1, seis.get(1));
        servicioTruco.determinarGanadorRonda(j1,j2);

        j1.tirarCarta(seis.get(2));
        truco.registrarJugada(j1, seis.get(2));
        servicioTruco.determinarGanadorRonda(j1,j2);
        j2.tirarCarta(seis.get(5));
        truco.registrarJugada(j1, seis.get(5));
        servicioTruco.determinarGanadorRonda(j1,j2);
    }


}
