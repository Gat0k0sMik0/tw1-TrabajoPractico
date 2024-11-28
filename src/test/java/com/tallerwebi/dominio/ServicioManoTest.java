package com.tallerwebi.dominio;

import com.tallerwebi.infraestructura.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class ServicioManoTest {

    RepositorioMano repositorioMano = mock(RepositorioManoImpl.class);
    RepositorioTruco repositorioTruco = mock(RepositorioTrucoImpl.class);
    RepositorioRonda2 repositorioRonda = mock(RepositorioRondaImpl.class);
    RepositorioCarta repositorioCarta = mock(RepositorioCartaImpl.class);

    ServicioMano servicioMano = new ServicioManoImpl(
            repositorioMano, repositorioRonda, repositorioTruco, repositorioCarta);

    Jugador j1 = new Jugador();
    Jugador j2 = new Jugador();
    Mano mano = new Mano();
    List<Carta> esperadas = new ArrayList<>();

    @BeforeEach
    void preparo() {
        j1.setNombre("gonza");
        j2.setNombre("leo");
        j1.setNumero(1);
        j2.setNumero(2);
        mano.setCartasJ1(new ArrayList<>());
        mano.setCartasJ2(new ArrayList<>());
        Carta carta1 = new Carta();
        Carta carta2 = new Carta();
        Carta carta3 = new Carta();
        Carta carta4 = new Carta();
        Carta carta5 = new Carta();
        Carta carta6 = new Carta();
        carta1.setId(0L);
        carta1.setNumero(4);
        carta1.setValor(0);
        carta1.setPalo("Espadas");
        carta2.setId(1L);
        carta2.setNumero(7);
        carta2.setValor(9);
        carta2.setPalo("Espadas");
        carta3.setId(2L);
        carta3.setNumero(12);
        carta3.setValor(6);
        carta3.setPalo("Espadas");
        carta4.setId(3L);
        carta4.setNumero(1);
        carta4.setValor(7);
        carta4.setPalo("Copas");
        carta5.setId(4L);
        carta5.setNumero(2);
        carta5.setValor(8);
        carta5.setPalo("Copas");
        carta6.setId(5L);
        carta6.setNumero(3);
        carta6.setValor(9);
        carta6.setPalo("Copas");
        esperadas.add(carta1);
        esperadas.add(carta2);
        esperadas.add(carta3);
        esperadas.add(carta4);
        esperadas.add(carta5);
        esperadas.add(carta6);
    }

    @Test
    void queSeGuardeLaMano() {
        when(repositorioCarta.obtenerCartas()).thenReturn(esperadas);
        Partida t = new Partida();
        Mano mano = servicioMano.empezar(t, j1, j2);
        assertThat(mano, notNullValue());
    }

    @Test
    void queSeGuardeQuienCantoEnvido() {
        // given
        Jugador ejecutor = new Jugador();
        Jugador receptor = new Jugador();
        ejecutor.setNumero(1);
        String ejecutorNroRecibido = "1";
        Jugador diceEnvidoJ1 = null;
        Jugador diceEnvidoJ2 = null;
        // when
        if (ejecutor.getNumero().toString().equals(ejecutorNroRecibido)) {
            diceEnvidoJ1 = ejecutor;
        } else {
            diceEnvidoJ2 = ejecutor;
        }
        // then
        assertThat(diceEnvidoJ1, notNullValue());
    }

    @Test
    public void queLosPuntosEnJuegoDelEnvidoSean2() {
        Partida t = new Partida();
        Mano m = new Mano();
        t.setId(0L);
        t.setPuntosParaGanar(30);
        t.setPuntosJ1(0);
        t.setPuntosJ2(0);
        t.setJ1(j1);
        t.setJ2(j2);
        m.setId(0L);
        m.setCartasJ1(new ArrayList<>());
        m.setCartasJ2(new ArrayList<>());
        m.setCartasTiradasJ1(new ArrayList<>());
        m.setCartasTiradasJ2(new ArrayList<>());
        m.setConfirmacionTerminada(false);
        m.setEstaTerminada(false);
        m.setPartida(t);
        servicioMano.preguntar(m, "2", 1);
        assertThat(servicioMano.obtenerPuntosEnJuegoDelEnvido(), equalTo(2));
    }

    @Test
    public void queLosPuntosEnJuegoDelEnvidoSean4() {
        Partida t = new Partida();
        Mano m = new Mano();
        t.setId(0L);
        t.setPuntosParaGanar(30);
        t.setPuntosJ1(0);
        t.setPuntosJ2(0);
        t.setJ1(j1);
        t.setJ2(j2);
        m.setId(0L);
        m.setCartasJ1(new ArrayList<>());
        m.setCartasJ2(new ArrayList<>());
        m.setCartasTiradasJ1(new ArrayList<>());
        m.setCartasTiradasJ2(new ArrayList<>());
        m.setConfirmacionTerminada(false);
        m.setEstaTerminada(false);
        m.setPartida(t);
        servicioMano.preguntar(m, "2", 1);
        Jugador leToca = servicioMano.responder(m, "2", "2", 2);
        assertThat(servicioMano.obtenerPuntosEnJuegoDelEnvido(), equalTo(4));
        assertThat(leToca, notNullValue());
    }

    @Test
    public void queLosPuntosEnJuegoDelEnvidoSean5() {
        Partida t = new Partida();
        Mano m = new Mano();
        t.setId(0L);
        t.setPuntosParaGanar(30);
        t.setPuntosJ1(0);
        t.setPuntosJ2(0);
        t.setJ1(j1);
        t.setJ2(j2);
        m.setId(0L);
        m.setCartasJ1(new ArrayList<>());
        m.setCartasJ2(new ArrayList<>());
        m.setCartasTiradasJ1(new ArrayList<>());
        m.setCartasTiradasJ2(new ArrayList<>());
        m.setConfirmacionTerminada(false);
        m.setEstaTerminada(false);
        m.setPartida(t);
        servicioMano.preguntar(m, "2", 1);
        Jugador leToca = servicioMano.responder(m, "2", "3", 2);
        assertThat(servicioMano.obtenerPuntosEnJuegoDelEnvido(), equalTo(5));
        assertThat(leToca, notNullValue());
    }

    @Test
    public void queLosPuntosEnJuegoDelEnvidoSean7() {
        Partida t = new Partida();
        Mano m = new Mano();
        t.setId(0L);
        t.setPuntosParaGanar(30);
        t.setPuntosJ1(0);
        t.setPuntosJ2(0);
        t.setJ1(j1);
        t.setJ2(j2);
        m.setId(0L);
        m.setCartasJ1(new ArrayList<>());
        m.setCartasJ2(new ArrayList<>());
        m.setCartasTiradasJ1(new ArrayList<>());
        m.setCartasTiradasJ2(new ArrayList<>());
        m.setConfirmacionTerminada(false);
        m.setEstaTerminada(false);
        m.setPartida(t);
        servicioMano.preguntar(m, "2", 1);
        servicioMano.responder(m, "2", "2", 2);
        Jugador leToca = servicioMano.responder(m, "2", "3", 1);
        assertThat(servicioMano.obtenerPuntosEnJuegoDelEnvido(), equalTo(7));
        assertThat(leToca, notNullValue());
    }

    @Test
    public void queSeCalculeBienLosTantosDeUnJugador () {
        // given
        when(repositorioCarta.obtenerCartas()).thenReturn(esperadas);
        Partida t = new Partida();
        Mano m = new Mano();
        t.setPuntosParaGanar(30);
        t.setPuntosJ1(0);
        t.setPuntosJ2(0);
        t.setJ1(j1);
        t.setJ2(j2);
        m.setCartasJ1(new ArrayList<>());
        m.setCartasJ2(new ArrayList<>());
        servicioMano.empezar(t, j1, j2);
        whenAsignoTresCartasAlJugador(j1, esperadas, m);
        whenAsignoTresCartasAlJugador(j2, esperadas, m);
    }

    @Test
    public void queElFaltaEnvidoLoGaneElJugadorUno() {
        // given
        when(repositorioCarta.obtenerCartas()).thenReturn(esperadas);
        when(repositorioMano.obtenerManoPorId(0L)).thenReturn(mano);
        Partida t = new Partida();
        Mano m = new Mano();
        t.setId(0L);
        t.setPuntosParaGanar(30);
        t.setPuntosJ1(0);
        t.setPuntosJ2(0);
        t.setJ1(j1);
        t.setJ2(j2);
        m.setId(0L);
        m.setCartasJ1(new ArrayList<>());
        m.setCartasJ2(new ArrayList<>());
        m.setCartasTiradasJ1(new ArrayList<>());
        m.setCartasTiradasJ2(new ArrayList<>());
        m.setConfirmacionTerminada(false);
        m.setEstaTerminada(false);
        m.setPartida(t);
        whenAsignoTresCartasAlJugador(j1, esperadas, m);
        whenAsignoTresCartasAlJugador(j2, esperadas, m);
        System.out.println("J1 tiene: " + m.getCartasJ1().size());
        System.out.println("J2 tiene: " + m.getCartasJ2().size());
        // When
        servicioMano.preguntar(m, "4", 1);
        servicioMano.responder(m, "4", "1", 2);
        // Then
        assertThat(t.getPuntosJ1(), equalTo(30));
    }

    @Test
    public void queCanteTrucoYQueSeGuardenLosPuntosQueRecibiraSiGanaLaManoYSean2() {
        // given
        Partida t = new Partida();
        Mano m = new Mano();
        t.setPuntosParaGanar(30);
        t.setPuntosJ1(0);
        t.setPuntosJ2(0);
        t.setJ1(j1);
        t.setJ2(j2);
        m.setCartasJ1(new ArrayList<>());
        m.setCartasJ2(new ArrayList<>());
        m.setPartida(t);
        whenAsignoTresCartasAlJugador(j1, esperadas, m);
        whenAsignoTresCartasAlJugador(j2, esperadas, m);
        // When
        servicioMano.preguntar(m, "5", 1);
        servicioMano.responder(m, "5", "1", 2);
        // Then
        assertThat(servicioMano.obtenerPuntosEnJuegoPorTruco(), equalTo(2));
    }

    @Test
    public void queCanteTrucoYQueSeGuardenLosPuntosQueRecibiraSiGanaLaManoYSean3() {
        // given
        Partida t = new Partida();
        Mano m = new Mano();
        t.setPuntosParaGanar(30);
        t.setPuntosJ1(0);
        t.setPuntosJ2(0);
        t.setJ1(j1);
        t.setJ2(j2);
        m.setCartasJ1(new ArrayList<>());
        m.setCartasJ2(new ArrayList<>());
        m.setPartida(t);
        whenAsignoTresCartasAlJugador(j1, esperadas, m);
        whenAsignoTresCartasAlJugador(j2, esperadas, m);
        // When
        servicioMano.preguntar(m, "5", 1);
        servicioMano.responder(m, "5", "6", 2);
        servicioMano.responder(m, "6", "1", 1);
        // Then
        assertThat(servicioMano.obtenerPuntosEnJuegoPorTruco(), equalTo(3));
    }



    @Test
    public void queCanteTrucoYQueSeGuardenLosPuntosQueRecibiraSiGanaLaManoYSean5() {
        // given
        Partida t = new Partida();
        Mano m = new Mano();
        t.setPuntosParaGanar(30);
        t.setPuntosJ1(0);
        t.setPuntosJ2(0);
        t.setJ1(j1);
        t.setJ2(j2);
        m.setCartasJ1(new ArrayList<>());
        m.setCartasJ2(new ArrayList<>());
        m.setPartida(t);
        whenAsignoTresCartasAlJugador(j1, esperadas, m);
        whenAsignoTresCartasAlJugador(j2, esperadas, m);
        // When
        Jugador respondeAhora = servicioMano.preguntar(m, "5", 1);
        assertThat(respondeAhora.getNombre(), equalToIgnoringCase(j2.getNombre()));

        respondeAhora = servicioMano.responder(m, "5", "6", 2);
        assertThat(respondeAhora.getNombre(), equalToIgnoringCase(j1.getNombre()));

        respondeAhora = servicioMano.responder(m, "6", "7", 1);
        assertThat(respondeAhora.getNombre(), equalToIgnoringCase(j2.getNombre()));

        respondeAhora = servicioMano.responder(m, "7", "1", 2);
        assertThat(respondeAhora, nullValue());
        // Then
        assertThat(servicioMano.obtenerPuntosEnJuegoPorTruco(), equalTo(4));
    }

    @Test
    public void queElQueLeTocaResponderSeaElEsperadoTrasUnaSecuenciaDeAcciones() {
        // given
        Partida t = new Partida();
        Mano m = new Mano();
        t.setPuntosParaGanar(30);
        t.setPuntosJ1(0);
        t.setPuntosJ2(0);
        t.setJ1(j1);
        t.setJ2(j2);
        m.setCartasJ1(new ArrayList<>());
        m.setCartasJ2(new ArrayList<>());
        m.setPartida(t);
        whenAsignoTresCartasAlJugador(j1, esperadas, m);
        whenAsignoTresCartasAlJugador(j2, esperadas, m);
        // When/then
        Jugador respondeAhora = servicioMano.preguntar(m, "5", 1);
        assertThat(respondeAhora.getNombre(), equalToIgnoringCase(j2.getNombre()));
        respondeAhora = servicioMano.responder(m, "5", "6", 2);
        assertThat(respondeAhora.getNombre(), equalToIgnoringCase(j1.getNombre()));
        respondeAhora = servicioMano.responder(m, "6", "7", 1);
        assertThat(respondeAhora.getNombre(), equalToIgnoringCase(j2.getNombre()));
        respondeAhora = servicioMano.responder(m, "7", "1", 2);
        assertThat(respondeAhora, nullValue());
    }

    @Test
    public void queSeLeAsigneUnPuntoAlRivalPorIrseAlMazo() {
        // given
        Ronda r1 = new Ronda();
        Ronda r2 = new Ronda();
        List<Ronda> rs = new ArrayList<>();
        rs.add(r1);
        rs.add(r2);
        Partida t = new Partida();
        Mano m = new Mano();
        m.setId(0L);
        Jugador j1 = new Jugador();
        j1.setNumero(1);
        t.setPuntosParaGanar(30);
        t.setPuntosJ1(0);
        t.setPuntosJ2(0);
        when(repositorioRonda.obtenerRondasDeUnaMano(0L)).thenReturn(new ArrayList<>());
        // when
        List<Ronda> rondasMano = this.repositorioRonda.obtenerRondasDeUnaMano(m.getId());
        Integer puntosEnJuego = 1;
        if (rondasMano.isEmpty()) {
            puntosEnJuego = 2;
        }
        if (j1.getNumero().equals(1)) {
            t.setPuntosJ2(t.getPuntosJ2() + puntosEnJuego);
        } else {
            t.setPuntosJ1(t.getPuntosJ1() + puntosEnJuego);
        }
        // then
        assertThat(t.getPuntosJ2(), equalTo(2));
    }

    @Test
    public void queSeLeAsigneDosPuntosAlRivalPorIrseAlMazo() {
        // given
        Ronda r1 = new Ronda();
        Ronda r2 = new Ronda();
        List<Ronda> rs = new ArrayList<>();
        rs.add(r1);
        rs.add(r2);
        Partida t = new Partida();
        Mano m = new Mano();
        m.setId(0L);
        Jugador j1 = new Jugador();
        j1.setNumero(1);
        t.setPuntosParaGanar(30);
        t.setPuntosJ1(0);
        t.setPuntosJ2(0);
        when(repositorioRonda.obtenerRondasDeUnaMano(0L)).thenReturn(rs);
        // when
        List<Ronda> rondasMano = this.repositorioRonda.obtenerRondasDeUnaMano(m.getId());
        Integer puntosEnJuego = 1;
        if (rondasMano.isEmpty()) {
            puntosEnJuego = 2;
        }
        if (j1.getNumero().equals(1)) {
            t.setPuntosJ2(t.getPuntosJ2() + puntosEnJuego);
        } else {
            t.setPuntosJ1(t.getPuntosJ1() + puntosEnJuego);
        }
        // then
        assertThat(t.getPuntosJ2(), equalTo(1));
    }

    @Test
    public void siSeVaAlMazoQueSeaNullElQueLeToqueResponder() {
        // given
        Partida t = new Partida();
        Mano m = new Mano();
        t.setPuntosParaGanar(30);
        t.setPuntosJ1(0);
        t.setPuntosJ2(0);
        t.setJ1(j1);
        t.setJ2(j2);
        m.setCartasJ1(new ArrayList<>());
        m.setCartasJ2(new ArrayList<>());
        m.setPartida(t);
        // when
        Jugador respondeAhora = servicioMano.preguntar(m, "9", 1);
        // then
        assertThat(respondeAhora, nullValue());
    }

    @Test
    public void queSeLeAsignenLasCartas() {
        // given
        when(repositorioCarta.obtenerCartas()).thenReturn(esperadas);
        Partida t = new Partida();
        Mano m = servicioMano.empezar(t, j1, j2);
        // then
        assertThat(m.getCartasJ1().size(), equalTo(3));
    }

    @Test
    public void queTireUnaCarta() {
        // given
        when(repositorioCarta.obtenerCartas()).thenReturn(esperadas);
        when(repositorioCarta.buscarCartaPorId(0L)).thenReturn(esperadas.get(0));
        Partida t = new Partida();
        t.setJ1(j1);
        t.setJ2(j2);
        Mano m = servicioMano.empezar(t, j1, j2);
        // when
        whenAsignoTresCartasAlJugador(j1, esperadas, m);
        Ronda r = servicioMano.tirarCarta(t, m, 0L, j1.getNumero().toString());
        // then
        assertNotNull(r);
    }

    @Test
    public void queSeLeAsigneUnPuntoPorGanarLasRondasSinTruco() {
        // given
        when(repositorioCarta.buscarCartaPorId(0L)).thenReturn(esperadas.get(0));
        when(repositorioCarta.buscarCartaPorId(1L)).thenReturn(esperadas.get(1));
        when(repositorioCarta.buscarCartaPorId(2L)).thenReturn(esperadas.get(2));
        when(repositorioCarta.buscarCartaPorId(3L)).thenReturn(esperadas.get(3));
        when(repositorioCarta.buscarCartaPorId(4L)).thenReturn(esperadas.get(4));
        when(repositorioCarta.buscarCartaPorId(5L)).thenReturn(esperadas.get(5));
        Partida t = new Partida();
        Mano m = new Mano();
        t.setId(0L);
        t.setPuntosParaGanar(30);
        t.setPuntosJ1(0);
        t.setPuntosJ2(0);
        t.setJ1(j1);
        t.setJ2(j2);
        m.setId(0L);
        m.setCartasJ1(new ArrayList<>());
        m.setCartasJ2(new ArrayList<>());
        m.setCartasTiradasJ1(new ArrayList<>());
        m.setCartasTiradasJ2(new ArrayList<>());
        m.setConfirmacionTerminada(false);
        m.setEstaTerminada(false);
        m.setPartida(t);
        whenAsignoTresCartasAlJugador(j1, esperadas, m);
        whenAsignoTresCartasAlJugador(j2, esperadas, m);
        // When
        servicioMano.tirarCarta(t, m, 1L, j1.getNumero().toString());
        servicioMano.determinarGanadorRonda(t, m);
        servicioMano.tirarCarta(t, m, 3L, j2.getNumero().toString());
        servicioMano.determinarGanadorRonda(t, m);

        servicioMano.tirarCarta(t, m, 4L, j2.getNumero().toString());
        servicioMano.determinarGanadorRonda(t, m);
        servicioMano.tirarCarta(t, m, 0L, j1.getNumero().toString());
        servicioMano.determinarGanadorRonda(t, m);

        servicioMano.tirarCarta(t, m, 2L, j1.getNumero().toString());
        servicioMano.determinarGanadorRonda(t, m);
        servicioMano.tirarCarta(t, m, 5L, j2.getNumero().toString());
        servicioMano.determinarGanadorRonda(t, m);

        //  r  1  2  3
        // j1 7e 4e 12e
        // j2 1c 2c 3c

        // Then
        assertThat(m.getPuntosRondaJ1(), equalTo(1));
        assertThat(m.getPuntosRondaJ2(), equalTo(2));
        assertThat(t.getPuntosJ1(), equalTo(0));
        assertThat(t.getPuntosJ2(), equalTo(1));
    }

    private void whenAsignoTresCartasAlJugador(Jugador j, List<Carta> esperadas, Mano mano) {
        int contador = 0;
        Iterator<Carta> iterator = esperadas.iterator();
        while (iterator.hasNext() && contador < 3) {
            Carta carta = iterator.next();
            if (j.getNumero().equals(1)) {
                mano.getCartasJ1().add(carta);
                System.out.println("Asigno a: " + j.getNombre() + " " + carta.getPalo() + " " + carta.getNumero());
            } else {
                mano.getCartasJ2().add(carta);
                System.out.println("Asigno a: " + j.getNombre() + " " + carta.getPalo() + " " + carta.getNumero());
            }
            iterator.remove(); // Elimina de la lista principal.
            contador++;
        }
    }


}
