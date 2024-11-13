package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.IndiceFueraDeRangoException;
import com.tallerwebi.infraestructura.RepositorioCartaImpl;
import com.tallerwebi.infraestructura.RepositorioTrucoImpl;
import com.tallerwebi.infraestructura.ServicioManoImpl;
import com.tallerwebi.infraestructura.ServicioTrucoImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ServicioTrucoTest {

    RepositorioCartaImpl repositorioCarta = mock(RepositorioCartaImpl.class);
    RepositorioTrucoImpl repositorioTruco = mock(RepositorioTrucoImpl.class);
    ServicioMano servicioMano = mock(ServicioManoImpl.class);

    ServicioTruco servicioTruco = new ServicioTrucoImpl(
            repositorioCarta, repositorioTruco, servicioMano
    );

    Jugador j1 = new Jugador("gonza");
    Jugador j2 = new Jugador("leo");
    List<Jugador> jugadores = new ArrayList<>();

    @BeforeEach
    public void iniciarTest() {
        jugadores.add(j1);
        jugadores.add(j2);
    }


    @Test
    public void queSeEmpieceLaPartida() {
        List<Carta> lasQueYoQuiero = givenSeCarganSeisCartas();
        servicioTruco.empezar(j1, j2, lasQueYoQuiero);
        assertThat(servicioTruco.getCantidadJugadores(), equalTo(2));
    }

    @Test
    public void queLosJugadoresTenganAsignadasSusCartas() {
        List<Carta> lasQueYoQuiero = givenSeCarganSeisCartas();
        servicioTruco.empezar(j1, j2, lasQueYoQuiero);
        assertThat(servicioTruco.getJugadores().get(0).getCartas().size(), equalTo(3));
        assertThat(servicioTruco.getJugadores().get(1).getCartas().size(), equalTo(3));
    }

//    @Test
//    public void siUnaCartaSeRepiteFalla() {
//        List<Carta> cartitas = new ArrayList<>();
//        Truco truco = new Truco();
//        truco.cargarCartasAlMazo();
//        Carta c1 = truco.getMazo().getCartaRandom();
//        truco.getMazo().saberSiLaCartaSeRepiteEnMazo(cartitas, c1.getNumero(), c1.getPalo());
//        cartitas.add(c1);
//        Boolean saber = truco.getMazo().saberSiLaCartaSeRepiteEnMazo(cartitas, c1.getNumero(), c1.getPalo());
//        assertTrue(saber);
//    }
//
//    @Test
//    public void siUnaCartaNoSeRepiteNoFalla() throws IndiceFueraDeRangoException {
//        List<Carta> cartitas = new ArrayList<>();
//        Truco truco = new Truco();
//        truco.cargarCartasAlMazo();
//        Carta c1 = truco.getMazo().getCartaPorIndice(0);
//        Carta c2 = truco.getMazo().getCartaPorIndice(1);
//        cartitas.add(c1);
//        Boolean saber = truco.getMazo().saberSiLaCartaSeRepiteEnMazo(cartitas, c2.getNumero(), c2.getPalo());
//        assertFalse(saber);
//    }
//
//    @Test
//    public void queLasCartasObtenidasSeanLasEsperadas() {
//        Truco truco = new Truco();
//        List<Carta> seis = truco.getMazo().getSeisCartasAleatoriasSinRepetir();
//        int recibido = seis.size();
//        int esperado = 6;
//        Boolean sonIguales = esperado == recibido;
//        assertTrue(sonIguales);
//    }
//
//    @Test
//    public void queSeAsignenTresCartasACadaJugador() {
//        Truco truco = new Truco();
//        List<Carta> seis = truco.getMazo().getSeisCartasAleatoriasSinRepetir();
//        truco.getMazo().asignarCartasAJugadores(jugadores, seis);
//        Boolean esperadoEstaOk = j1.getCartas().size() == j2.getCartas().size();
//        assertTrue(esperadoEstaOk);
//        assertEquals(3, j1.getCartas().size());
//        assertEquals(3, j2.getCartas().size());
//    }

    @Test
    public void queUnJugadorPuedaTirarSuCarta() {
        Truco truco = new Truco(); // SOLO TEST
        List<Carta> lasQueYoQuiero = givenSeCarganSeisCartas();
        servicioTruco.empezar(j1, j2, lasQueYoQuiero);
        servicioTruco.tirarCarta(j1, truco.buscarCartaPorNumeroYPalo(7, "Espadas"));
        assertThat(servicioTruco.getJugadores().get(0).getCartas().size(), equalTo(2));
    }


    @Test
    public void queSeRegistreElMovimiento() {
        Truco truco = new Truco(); // SOLO TEST
        List<Carta> lasQueYoQuiero = givenSeCarganSeisCartas();
        servicioTruco.empezar(j1, j2, lasQueYoQuiero);
        servicioTruco.tirarCarta(j1, truco.buscarCartaPorNumeroYPalo(7, "Espadas"));
        assertThat(servicioTruco.getCartasTiradas(), equalTo(1));
    }


    @Test
    public void queSeHayanHechoDosRondas() throws IndiceFueraDeRangoException {
        // given
        Truco truco = new Truco(); // SOLO TEST
        List<Carta> lasQueYoQuiero = givenSeCarganSeisCartas();
        servicioTruco.empezar(j1, j2, lasQueYoQuiero);
        List<Ronda> rondasTest = new ArrayList<>();
        rondasTest.add(new Ronda(0, j1, new Carta()));
        rondasTest.add(new Ronda(0, j1, new Carta()));
        // when
        servicioTruco.tirarCarta(j1, truco.buscarCartaPorNumeroYPalo(7, "Espadas"));
        servicioTruco.tirarCarta(j2, truco.buscarCartaPorNumeroYPalo(1, "Copas"));
        // then
        verify(servicioMano, times(1))
                .guardarRonda(j1, truco.buscarCartaPorNumeroYPalo(7, "Espadas"));
        verify(servicioMano, times(1))
                .guardarRonda(j2, truco.buscarCartaPorNumeroYPalo(1, "Copas"));
        when(servicioMano.getRondas()).thenReturn(rondasTest);
        assertThat(servicioTruco.getRondas().size(), equalTo(2));
    }


    @Test
    public void queSePuedaJugarUnaMano() {
        Truco truco = new Truco(); // SOLO TEST
        List<Carta> lasQueYoQuiero = givenSeCarganSeisCartas();
        servicioTruco.empezar(j1, j2, lasQueYoQuiero);
        whenSeJuegaUnaMano(truco);
        assertThat(servicioTruco.getCartasTiradas(), equalTo(6));
    }

    @Test
    public void queSeObtengaGanadorDeUnaMano() {
        // given
        Truco truco = new Truco(); // SOLO TEST
        List<Carta> lasQueYoQuiero = givenSeCarganSeisCartas();
        servicioTruco.empezar(j1, j2, lasQueYoQuiero);
        // when
        whenSeJuegaUnaMano(truco);
        when(servicioMano.getGanadorDeManoActual()).thenReturn(new Jugador("XD"));
        // then
        assertThat(servicioTruco.getUltimoGanadorDeMano(), notNullValue());
    }


    @Test
    public void saberQuienGanoLaPrimeraRonda() {
        // given
        Truco truco = new Truco(); // SOLO TEST
        List<Carta> lasQueYoQuiero = givenSeCarganSeisCartas();
        servicioTruco.empezar(j1, j2, lasQueYoQuiero);
        // when
        whenSeJuegaUnaMano(truco);
        // then
        when(servicioMano.getGanadorDeRondaPorNumero(0)).thenReturn("gonza");
        assertEquals(servicioTruco.getGanadorDeRondaPorNumero(0), j1.getNombre());
    }

    @Test
    public void queLaPrimeraRondaLaGaneElPrimeroYLaSegundaElSegundo() {
        // given
        Truco truco = new Truco(); // SOLO TEST
        List<Carta> lasQueYoQuiero = givenSeCarganSeisCartas();
        servicioTruco.empezar(j1, j2, lasQueYoQuiero);
        // when
        whenSeJuegaUnaMano(truco);
        // then
        when(servicioMano.getGanadorDeRondaPorNumero(0)).thenReturn("gonza");
        when(servicioMano.getGanadorDeRondaPorNumero(1)).thenReturn("leo");
        assertEquals(servicioTruco.getGanadorDeRondaPorNumero(0), j1.getNombre());
        assertEquals(servicioTruco.getGanadorDeRondaPorNumero(1), j2.getNombre());
    }

    @Test
    public void queLaManoLaGaneElJugadorEsperado() {
        // given
        Truco truco = new Truco(); // SOLO TEST
        List<Carta> lasQueYoQuiero = givenSeCarganSeisCartas();
        servicioTruco.empezar(j1, j2, lasQueYoQuiero);
        // when
        whenSeJuegaUnaMano(truco);
        when(servicioMano.getGanadorDeManoActual()).thenReturn(j2);
        // then
        assertThat(servicioTruco.getUltimoGanadorDeMano(), notNullValue());
        assertEquals(servicioTruco.getUltimoGanadorDeMano().getNombre(), j2.getNombre());
    }

    @Test
    public void queElJugadorQueGanoLaManoTengaUnPuntoEsperado() {
        // given
        Truco truco = new Truco(); // SOLO TEST
        List<Carta> lasQueYoQuiero = givenSeCarganSeisCartas();
        servicioTruco.empezar(j1, j2, lasQueYoQuiero);
        // when
        whenSeJuegaUnaMano(truco);
        verify(servicioMano, times(1)).sumarPuntoDeRonda(j1);
        verify(servicioMano, times(2)).sumarPuntoDeRonda(j2);
        verify(servicioMano, times(1)).getGanadorDeManoActual();
        when(servicioMano.getGanadorDeManoActual()).thenReturn(j2);
        assertThat(servicioTruco.getUltimoGanadorDeMano(), notNullValue());
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
//        assertThat(truco.getManosDePartida().get(0).getJugador(), notNullValue());
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
//        Jugador ganadorMano = truco.getGanadorDeManoPorNumero(0);
        // then
//        assertThat(ganadorMano, notNullValue());
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
//        assertEquals(j2.getNombre(), truco.saberQuienSumoMasPuntosEnLasManos(j1, j2).getNombre());
    }

    @Test
    public void calcularSiTieneParaEnvidoDosCartasAlMenos() throws IndiceFueraDeRangoException {
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
    public void obtenerValoresDeCartaParaEnvido() throws IndiceFueraDeRangoException {
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

    @Test
    void queSeGenereUnaAccion() throws IndiceFueraDeRangoException {
        List<Carta> seis = givenAsignoCartasALosJugadores();
        servicioTruco.empezar(jugadores, seis);
        Truco truco = servicioTruco.getTruco();
        truco.empezarMano(jugadores);
        Integer nroAccion = servicioTruco.preguntar("2", j1, j2);
        assertEquals(0, nroAccion);
    }

    @Test
    void queElCuandoSeLeRespondaQUIEROAlEnvidoSalgaBien() throws IndiceFueraDeRangoException {
        List<Carta> seis = givenAsignoCartasALosJugadores();
        servicioTruco.empezar(jugadores, seis);
        Truco truco = servicioTruco.getTruco();
        truco.empezarMano(jugadores);
        Integer nroAccion = servicioTruco.preguntar("2", j1, j2); // envido
        Jugador leTocaResponder = servicioTruco.responder("1", j2, j1, nroAccion); // quiero
        assertThat(leTocaResponder, notNullValue());
    }

    @Test
    void queSeCantenDosEnvidos() throws IndiceFueraDeRangoException {
        List<Carta> seis = givenAsignoCartasALosJugadores();
        servicioTruco.empezar(jugadores, seis);
        Truco truco = servicioTruco.getTruco();
        truco.empezarMano(jugadores);
        Integer nroAccion = servicioTruco.preguntar("2", j1, j2);
        Jugador leTocaResponder = servicioTruco.responder("2", j2, j1, nroAccion);
        assertThat(leTocaResponder, notNullValue());
    }

    @Test
    void queLosPuntosEnJuegoDelEnvidoSeanLosEsperados() throws IndiceFueraDeRangoException {
        List<Carta> seis = givenAsignoCartasALosJugadores();
        servicioTruco.empezar(jugadores, seis);
        Truco truco = servicioTruco.getTruco();
        truco.empezarMano(jugadores);
        Integer nroAccion = servicioTruco.preguntar("2", j1, j2);
        Jugador leTocaResponder = servicioTruco.responder("2", j2, j1, nroAccion);
        Accion a = servicioTruco.getAccionPorNro(nroAccion);
        assertThat(a, notNullValue());
        assertThat(a.getPuntosEnJuego(), equalTo(4));
        assertThat(leTocaResponder, notNullValue());
    }

    @Test
    void queSeAceptenDosEnvidosYSalgaBien() throws IndiceFueraDeRangoException {
        List<Carta> seis = givenAsignoCartasALosJugadores();
        servicioTruco.empezar(jugadores, seis);
        Truco truco = servicioTruco.getTruco();
        truco.empezarMano(jugadores);
        Integer nroAccion = servicioTruco.preguntar("2", j1, j2);
        servicioTruco.responder("2", j2, j1, nroAccion);
        Jugador respondeAhora = servicioTruco.responder("1", j1, j2, nroAccion);
        assertThat(respondeAhora, notNullValue());
        assertThat(j1.getPuntosPartida(), equalTo(4));
    }

    @Test
    void queSeJuegeEnvidoYRealEnvidoYQueSalgaBien() throws IndiceFueraDeRangoException {
        // given
        List<Carta> seis = givenAsignoCartasALosJugadores();
        servicioTruco.empezar(jugadores, seis);
        Truco truco = servicioTruco.getTruco();
        truco.empezarMano(jugadores);
        // when
        Integer nroAccion = servicioTruco.preguntar("2", j1, j2); // envido -> j1
        servicioTruco.responder("3", j2, j1, nroAccion); // real envido -> j2
        Jugador respondeAhora = servicioTruco.responder("1", j1, j2, nroAccion); // quiero -> j1
        // then
        assertThat(respondeAhora, notNullValue());
        assertThat(j1.getPuntosPartida(), equalTo(5));
    }

    @Test
    void queSeJuegeSoloRealEnvido() throws IndiceFueraDeRangoException {
        // given
        List<Carta> seis = givenAsignoCartasALosJugadores();
        servicioTruco.empezar(jugadores, seis);
        Truco truco = servicioTruco.getTruco();
        truco.empezarMano(jugadores);
        // when
        Integer nroAccion = servicioTruco.preguntar("3", j1, j2); // real envido -> j1
        servicioTruco.responder("1", j2, j1, nroAccion); // quiero -> j2
        // then
        assertThat(j1.getPuntosPartida(), equalTo(3));
    }

    @Test
    void queSeJuegueEnvidoEnvidoRealEnvido() throws IndiceFueraDeRangoException {
        // given
        List<Carta> seis = givenAsignoCartasALosJugadores();
        servicioTruco.empezar(jugadores, seis);
        Truco truco = servicioTruco.getTruco();
        truco.empezarMano(jugadores);
        // when
        Integer nroAccion = servicioTruco.preguntar("2", j1, j2); // envido -> j1
        servicioTruco.responder("2", j2, j1, nroAccion); // envido -> j2
        servicioTruco.responder("3", j1, j2, nroAccion); // real envido -> j1
        Jugador respondeAhora = servicioTruco.responder("1", j2, j1, nroAccion); // quiero -> j1
        // then
        assertThat(respondeAhora, notNullValue());
        assertThat(j1.getPuntosPartida(), equalTo(7));
    }

    @Test
    void queSeCanteUnTruco() throws IndiceFueraDeRangoException {
        // given
        List<Carta> seis = givenAsignoCartasALosJugadores();
        servicioTruco.empezar(jugadores, seis);
        Truco truco = servicioTruco.getTruco();
        truco.empezarMano(jugadores);
        // when
        Integer nroAccion = servicioTruco.preguntar("5", j1, j2); // truco
        Jugador ahoraLeToca = servicioTruco.responder("1", j2, j1, nroAccion); // quiero
        // then
        assertThat(ahoraLeToca, notNullValue());
        assertEquals(j1.getNombre(), ahoraLeToca.getNombre());
        assertEquals(2, truco.getPuntosEnJuegoDeLaManoActual());
    }


    @Test
    void queSeCanteTrucoYSeLeSumenLosPuntosEsperadosAlGanador() throws IndiceFueraDeRangoException {
        // given
        List<Carta> seis = givenAsignoCartasALosJugadores();
        servicioTruco.empezar(jugadores, seis);
        Truco truco = servicioTruco.getTruco();
        truco.empezarMano(jugadores);
        // when
        Integer nroAccion = servicioTruco.preguntar("5", j1, j2); // truco
        servicioTruco.responder("1", j2, j1, nroAccion); // quiero
        whenJuegoUnaMano(truco, seis);
        // then
        assertThat(j2.getPuntosPartida(), equalTo(2));
    }

    @Test
    void queSeCanteTrucoYReTrucoYSeLeSumenLosPuntosEsperadosAlGanador() throws IndiceFueraDeRangoException {
        // given
        List<Carta> seis = givenAsignoCartasALosJugadores();
        servicioTruco.empezar(jugadores, seis);
        Truco truco = servicioTruco.getTruco();
        truco.empezarMano(jugadores);
        // when
        Integer nroAccion = servicioTruco.preguntar("5", j1, j2); // truco
        servicioTruco.responder("6", j2, j1, nroAccion); // re truco
        servicioTruco.responder("1", j1, j2, nroAccion); // quiero
        whenJuegoUnaMano(truco, seis);
        // then
        assertThat(j2.getPuntosPartida(), equalTo(3));
    }

    @Test
    void queSeCanteTodosLosTrucosYSeanLosPuntosEsperados() throws IndiceFueraDeRangoException {
        // given
        List<Carta> seis = givenAsignoCartasALosJugadores();
        servicioTruco.empezar(jugadores, seis);
        Truco truco = servicioTruco.getTruco();
        truco.empezarMano(jugadores);
        // when
        Integer nroAccion = servicioTruco.preguntar("5", j1, j2); // truco
        servicioTruco.responder("6", j2, j1, nroAccion); // re truco
        servicioTruco.responder("7", j1, j2, nroAccion); // vale 4
        servicioTruco.responder("1", j2, j1, nroAccion); // quiero
        whenJuegoUnaMano(truco, seis);
        // then
        assertThat(j2.getPuntosPartida(), equalTo(4));
    }

    @Test
    void queSeJuegueConUnEnvidoYUnTrucoYQueElPuntajeSeaElEsperado() throws IndiceFueraDeRangoException {
        // given
        List<Carta> seis = givenAsignoCartasALosJugadores();
        servicioTruco.empezar(jugadores, seis);
        Truco truco = servicioTruco.getTruco();
        truco.empezarMano(jugadores);
        // when
        Integer nroAccion1 = servicioTruco.preguntar("2", j1, j2); // envido
        servicioTruco.responder("1", j2, j1, nroAccion1); // quiero

        Integer nroAccion2 = servicioTruco.preguntar("5", j1, j2); // truco
        servicioTruco.responder("1", j2, j1, nroAccion2); // quiero
        whenJuegoUnaMano(truco, seis);
        // then
        assertThat(j2.getPuntosPartida(), equalTo(2)); // gana truco
        assertThat(j1.getPuntosPartida(), equalTo(2)); // gana envido
    }

    @Test
    void queSeJuegueConUnEnvidoYUnDosTrucosYQueElPuntajeSeaElEsperado() throws IndiceFueraDeRangoException {
        // given
        List<Carta> seis = givenAsignoCartasALosJugadores();
        servicioTruco.empezar(jugadores, seis);
        Truco truco = servicioTruco.getTruco();
        truco.empezarMano(jugadores);
        // when
        Integer nroAccion1 = servicioTruco.preguntar("2", j1, j2); // envido
        servicioTruco.responder("1", j2, j1, nroAccion1); // quiero

        Integer nroAccion2 = servicioTruco.preguntar("5", j1, j2); // truco
        servicioTruco.responder("6", j2, j1, nroAccion2); // re truco
        servicioTruco.responder("1", j1, j2, nroAccion2); // quiero
        whenJuegoUnaMano(truco, seis);
        // then
        assertThat(j2.getPuntosPartida(), equalTo(3)); // gana truco
        assertThat(j1.getPuntosPartida(), equalTo(2)); // gana envido
    }

    @Test
    void queSeJuegueConUnEnvidoYUnTresTrucosYQueElPuntajeSeaElEsperado() throws IndiceFueraDeRangoException {
        // given
        List<Carta> seis = givenAsignoCartasALosJugadores();
        servicioTruco.empezar(jugadores, seis);
        Truco truco = servicioTruco.getTruco();
        truco.empezarMano(jugadores);
        // when
        Integer nroAccion1 = servicioTruco.preguntar("2", j1, j2); // envido
        servicioTruco.responder("1", j2, j1, nroAccion1); // quiero

        Integer nroAccion2 = servicioTruco.preguntar("5", j1, j2); // truco
        servicioTruco.responder("6", j2, j1, nroAccion2); // re truco
        servicioTruco.responder("7", j1, j2, nroAccion2); // vale 4
        servicioTruco.responder("1", j2, j1, nroAccion2); // quiero
        whenJuegoUnaMano(truco, seis);
        // then
        assertThat(j2.getPuntosPartida(), equalTo(4)); // gana truco
        assertThat(j1.getPuntosPartida(), equalTo(2)); // gana envido
    }

    @Test
    void queSeJuegueConDosEnvidoYUnTresTrucosYQueElPuntajeSeaElEsperado() throws IndiceFueraDeRangoException {
        // given
        List<Carta> seis = givenAsignoCartasALosJugadores();
        servicioTruco.empezar(jugadores, seis);
        Truco truco = servicioTruco.getTruco();
        truco.empezarMano(jugadores);
        // when
        Integer nroAccion1 = servicioTruco.preguntar("2", j1, j2); // envido
        servicioTruco.responder("2", j2, j1, nroAccion1); // envido
        servicioTruco.responder("1", j1, j2, nroAccion1); // quiero

        Integer nroAccion2 = servicioTruco.preguntar("5", j1, j2); // truco
        servicioTruco.responder("6", j2, j1, nroAccion2); // re truco
        servicioTruco.responder("7", j1, j2, nroAccion2); // vale 4
        servicioTruco.responder("1", j2, j1, nroAccion2); // quiero
        whenJuegoUnaMano(truco, seis);
        // then
        assertThat(j2.getPuntosPartida(), equalTo(4)); // gana truco
        assertThat(j1.getPuntosPartida(), equalTo(4)); // gana envido
    }

    @Test
    void queSeJuegueConTresEnvidoYUnTresTrucosYQueElPuntajeSeaElEsperado() throws IndiceFueraDeRangoException {
        // given
        List<Carta> seis = givenAsignoCartasALosJugadores();
        servicioTruco.empezar(jugadores, seis);
        Truco truco = servicioTruco.getTruco();
        truco.empezarMano(jugadores);
        // when
        Integer nroAccion1 = servicioTruco.preguntar("2", j1, j2); // envido
        servicioTruco.responder("2", j2, j1, nroAccion1); // envido
        servicioTruco.responder("3", j1, j2, nroAccion1); // real envido
        servicioTruco.responder("1", j2, j1, nroAccion1); // quiero

        Integer nroAccion2 = servicioTruco.preguntar("5", j1, j2); // truco
        servicioTruco.responder("6", j2, j1, nroAccion2); // re truco
        servicioTruco.responder("7", j1, j2, nroAccion2); // vale 4
        servicioTruco.responder("1", j2, j1, nroAccion2); // quiero
        whenJuegoUnaMano(truco, seis);
        // then
        assertThat(j2.getPuntosPartida(), equalTo(4)); // gana truco
        assertThat(j1.getPuntosPartida(), equalTo(7)); // gana envido
    }


    private List<Carta> givenAsignoCartasALosJugadores() throws IndiceFueraDeRangoException {
        List<Carta> seis = new ArrayList<>();
        Truco truco = new Truco();
        seis.add(truco.getMazo().getCartaPorIndice(3)); // 4 espada xxx
        seis.add(truco.getMazo().getCartaPorIndice(6)); // 7 espada x
        seis.add(truco.getMazo().getCartaPorIndice(9)); // 12 espada xx
        // aca hay 31 de envido

        seis.add(truco.getMazo().getCartaPorIndice(30)); // 1 copa x
        seis.add(truco.getMazo().getCartaPorIndice(31)); // 2 copa xx
        seis.add(truco.getMazo().getCartaPorIndice(32)); // 3 copa xxx
        // aca hay 25 de envido
        return seis;
    }

    private void whenJuegoUnaMano(Truco truco, List<Carta> seis) {
        /*
        RESUMEN PARTIDA DEFAULT -> MANO J1
        J1 | 4e 7e 12e
        J2 | 1c 2c 3c

        R1 -> j2
        R2 -> j1
        R3 -> j2
        M -> j2

        */

        j1.tirarCarta(seis.get(0));
        truco.registrarJugada(j1, seis.get(0));
        servicioTruco.determinarGanadorRonda(j1, j2);
        j2.tirarCarta(seis.get(3));
        truco.registrarJugada(j1, seis.get(3));
        servicioTruco.determinarGanadorRonda(j1, j2);

        j2.tirarCarta(seis.get(4));
        truco.registrarJugada(j1, seis.get(4));
        servicioTruco.determinarGanadorRonda(j1, j2);
        j1.tirarCarta(seis.get(1));
        truco.registrarJugada(j1, seis.get(1));
        servicioTruco.determinarGanadorRonda(j1, j2);

        j1.tirarCarta(seis.get(2));
        truco.registrarJugada(j1, seis.get(2));
        servicioTruco.determinarGanadorRonda(j1, j2);
        j2.tirarCarta(seis.get(5));
        truco.registrarJugada(j1, seis.get(5));
        servicioTruco.determinarGanadorRonda(j1, j2);
    }

    // seis cartas predeterminadas
    private List<Carta> givenSeCarganSeisCartas() {
        Truco truco = new Truco(); // SOLO TEST
        List<Carta> seis = new ArrayList<>();

        seis.add(truco.getMazo().getCartaPorIndice(3)); // 4 espada xxx
        seis.add(truco.getMazo().getCartaPorIndice(6)); // 7 espada x
        seis.add(truco.getMazo().getCartaPorIndice(9)); // 12 espada xx

        seis.add(truco.getMazo().getCartaPorIndice(30)); // 1 copa x
        seis.add(truco.getMazo().getCartaPorIndice(31)); // 2 copa xx
        seis.add(truco.getMazo().getCartaPorIndice(32)); // 3 copa xxx
        return seis;
    }

    // jugar una mano
    private void whenSeJuegaUnaMano(Truco truco) {
        servicioTruco.tirarCarta(j1, truco.buscarCartaPorNumeroYPalo(7, "Espadas"));
        servicioTruco.determinarGanadorRonda(j1, j2);
        servicioTruco.tirarCarta(j2, truco.buscarCartaPorNumeroYPalo(1, "Copas"));
        servicioTruco.determinarGanadorRonda(j1, j2);

        servicioTruco.tirarCarta(j1, truco.buscarCartaPorNumeroYPalo(12, "Espadas"));
        servicioTruco.determinarGanadorRonda(j1, j2);
        servicioTruco.tirarCarta(j2, truco.buscarCartaPorNumeroYPalo(2, "Copas"));
        servicioTruco.determinarGanadorRonda(j1, j2);

        servicioTruco.tirarCarta(j2, truco.buscarCartaPorNumeroYPalo(3, "Copas"));
        servicioTruco.determinarGanadorRonda(j1, j2);
        servicioTruco.tirarCarta(j1, truco.buscarCartaPorNumeroYPalo(4, "Espadas"));
        servicioTruco.determinarGanadorRonda(j1, j2);
    }

    private void whenJuegoDosRondas(Truco truco, List<Carta> seis) {
        j1.tirarCarta(seis.get(0));
        truco.registrarJugada(j1, seis.get(0));
        servicioTruco.determinarGanadorRonda(j1, j2);
        j2.tirarCarta(seis.get(3));
        truco.registrarJugada(j1, seis.get(3));
        servicioTruco.determinarGanadorRonda(j1, j2);
        j2.tirarCarta(seis.get(4));
        truco.registrarJugada(j1, seis.get(4));
        servicioTruco.determinarGanadorRonda(j1, j2);
        j1.tirarCarta(seis.get(1));
        truco.registrarJugada(j1, seis.get(1));
        servicioTruco.determinarGanadorRonda(j1, j2);
    }

}
