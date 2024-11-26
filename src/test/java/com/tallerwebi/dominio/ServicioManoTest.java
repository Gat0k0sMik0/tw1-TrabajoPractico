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
import static org.mockito.Mockito.times;

public class ServicioManoTest {

    RepositorioMano repositorioMano = mock(RepositorioManoImpl.class);
    RepositorioTruco repositorioTruco = mock(RepositorioTrucoImpl.class);
    RepositorioRonda2 repositorioRonda = mock(RepositorioRondaImpl.class);
    RepositorioCarta repositorioCarta = mock(RepositorioCartaImpl.class);

    ServicioMano2 servicioMano = new ServicioManoImpl2(
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
        carta1.setPalo("Espadas");
        carta2.setId(1L);
        carta2.setNumero(7);
        carta2.setPalo("Espadas");
        carta3.setId(2L);
        carta3.setNumero(12);
        carta3.setPalo("Espadas");
        carta4.setId(3L);
        carta4.setNumero(1);
        carta4.setPalo("Copas");
        carta5.setId(4L);
        carta5.setNumero(2);
        carta5.setPalo("Copas");
        carta6.setId(5L);
        carta6.setNumero(3);
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
        Truco2 t = new Truco2();
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
        Truco2 t = new Truco2();
        Mano m = new Mano();
        servicioMano.preguntar(m, "2", j1, j2);
        assertThat(servicioMano.obtenerPuntosEnJuegoDelEnvido(), equalTo(2));
    }

    @Test
    public void queLosPuntosEnJuegoDelEnvidoSean4() {
        Truco2 t = new Truco2();
        Mano m = new Mano();
        servicioMano.preguntar(m, "2", j1, j2);
        Jugador leToca = servicioMano.responder(t, "2", "2", j1, j2);
        assertThat(servicioMano.obtenerPuntosEnJuegoDelEnvido(), equalTo(4));
        assertThat(leToca, notNullValue());
    }

    @Test
    public void queLosPuntosEnJuegoDelEnvidoSean5() {
        Truco2 t = new Truco2();
        Mano m = new Mano();
        servicioMano.preguntar(m, "2", j1, j2);
        Jugador leToca = servicioMano.responder(t, "2", "3", j2, j1);
        assertThat(servicioMano.obtenerPuntosEnJuegoDelEnvido(), equalTo(5));
        assertThat(leToca, notNullValue());
    }

    @Test
    public void queLosPuntosEnJuegoDelEnvidoSean7() {
        Truco2 t = new Truco2();
        Mano m = new Mano();
        servicioMano.preguntar(m, "2", j1, j2);
        servicioMano.responder(t, "2", "2", j2, j1);
        Jugador leToca = servicioMano.responder(t, "2", "3", j1, j2);
        assertThat(servicioMano.obtenerPuntosEnJuegoDelEnvido(), equalTo(7));
        assertThat(leToca, notNullValue());
    }

    @Test
    public void queElFaltaEnvidoLoGaneElJugadorUno() {
        // given
        when(repositorioCarta.obtenerCartas()).thenReturn(esperadas);
        Truco2 t = new Truco2();
        Mano m = new Mano();
        t.setPuntosParaGanar(30);
        t.setPuntosJ1(0);
        t.setPuntosJ2(0);
        m.setCartasJ1(new ArrayList<>());
        m.setCartasJ2(new ArrayList<>());
        servicioMano.empezar(t, j1, j2);
        whenAsignoTresCartasAlJugador(j1, esperadas, m);
        whenAsignoTresCartasAlJugador(j2, esperadas, m);
        System.out.println(m.getCartasJ1().size() + " tiene el j1");
        System.out.println(m.getCartasJ2().size() + " tiene el j2");
        // When
        servicioMano.preguntar(m, "4", j1, j2);
        servicioMano.responder(t, "4", "1", j2, j1);
        // Then
        assertThat(t.getPuntosJ1(), equalTo(30));
    }

    @Test
    public void queCanteTrucoYQueSeGuardenLosPuntosQueRecibiraSiGanaLaManoYSean2() {
        // given
        Truco2 t = new Truco2();
        Mano m = new Mano();
        t.setPuntosParaGanar(30);
        t.setPuntosJ1(0);
        t.setPuntosJ2(0);
        m.setCartasJ1(new ArrayList<>());
        m.setCartasJ2(new ArrayList<>());
        whenAsignoTresCartasAlJugador(j1, esperadas, m);
        whenAsignoTresCartasAlJugador(j2, esperadas, m);
        // When
        servicioMano.preguntar(m, "5", j1, j2);
        servicioMano.responder(t, "5", "1", j2, j1);
        // Then
        assertThat(servicioMano.obtenerPuntosEnJuegoPorTruco(), equalTo(2));
    }

    @Test
    public void queCanteTrucoYQueSeGuardenLosPuntosQueRecibiraSiGanaLaManoYSean3() {
        // given
        Truco2 t = new Truco2();
        Mano m = new Mano();
        t.setPuntosParaGanar(30);
        t.setPuntosJ1(0);
        t.setPuntosJ2(0);
        m.setCartasJ1(new ArrayList<>());
        m.setCartasJ2(new ArrayList<>());
        whenAsignoTresCartasAlJugador(j1, esperadas, m);
        whenAsignoTresCartasAlJugador(j2, esperadas, m);
        // When
        servicioMano.preguntar(m, "5", j1, j2);
        servicioMano.responder(t, "5", "6", j2, j1);
        servicioMano.responder(t, "6", "1", j1, j2);
        // Then
        assertThat(servicioMano.obtenerPuntosEnJuegoPorTruco(), equalTo(3));
    }

    @Test
    public void queCanteTrucoYQueSeGuardenLosPuntosQueRecibiraSiGanaLaManoYSean5() {
        // given
        Truco2 t = new Truco2();
        Mano m = new Mano();
        t.setPuntosParaGanar(30);
        t.setPuntosJ1(0);
        t.setPuntosJ2(0);
        m.setCartasJ1(new ArrayList<>());
        m.setCartasJ2(new ArrayList<>());
        whenAsignoTresCartasAlJugador(j1, esperadas, m);
        whenAsignoTresCartasAlJugador(j2, esperadas, m);
        // When
        Jugador respondeAhora = servicioMano.preguntar(m, "5", j1, j2);
        assertThat(respondeAhora.getNombre(), equalToIgnoringCase(j2.getNombre()));
        respondeAhora = servicioMano.responder(t, "5", "6", j2, j1);
        assertThat(respondeAhora.getNombre(), equalToIgnoringCase(j1.getNombre()));
        respondeAhora = servicioMano.responder(t, "6", "7", j1, j2);
        assertThat(respondeAhora.getNombre(), equalToIgnoringCase(j2.getNombre()));
        respondeAhora = servicioMano.responder(t, "7", "1", j2, j1);
        assertThat(respondeAhora, nullValue());
        // Then
        assertThat(servicioMano.obtenerPuntosEnJuegoPorTruco(), equalTo(4));
    }

    @Test
    public void queElQueLeTocaResponderSeaElEsperadoTrasUnaSecuenciaDeAcciones() {
        // given
        Truco2 t = new Truco2();
        Mano m = new Mano();
        t.setPuntosParaGanar(30);
        t.setPuntosJ1(0);
        t.setPuntosJ2(0);
        m.setCartasJ1(new ArrayList<>());
        m.setCartasJ2(new ArrayList<>());
        whenAsignoTresCartasAlJugador(j1, esperadas, m);
        whenAsignoTresCartasAlJugador(j2, esperadas, m);
        // When/then
        Jugador respondeAhora = servicioMano.preguntar(m, "5", j1, j2);
        assertThat(respondeAhora.getNombre(), equalToIgnoringCase(j2.getNombre()));
        respondeAhora = servicioMano.responder(t, "5", "6", j2, j1);
        assertThat(respondeAhora.getNombre(), equalToIgnoringCase(j1.getNombre()));
        respondeAhora = servicioMano.responder(t, "6", "7", j1, j2);
        assertThat(respondeAhora.getNombre(), equalToIgnoringCase(j2.getNombre()));
        respondeAhora = servicioMano.responder(t, "7", "1", j2, j1);
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
        Truco2 t = new Truco2();
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
        Truco2 t = new Truco2();
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
        Mano m = new Mano();
        m.setId(0L);
        Truco2 t = new Truco2();
        t.setPuntosJ1(0);
        t.setPuntosJ2(0);
        m.setPartida(t);
        // when
        Jugador respondeAhora = servicioMano.preguntar(m, "9", j1, j2);
        // then
        assertThat(respondeAhora, nullValue());
    }

    @Test
    public void queSeLeAsignenLasCartas() {
        // given
        when(repositorioCarta.obtenerCartas()).thenReturn(esperadas);
        Truco2 t = new Truco2();
        Mano m = servicioMano.empezar(t, j1, j2);
        // then
        assertThat(m.getCartasJ1().size(), equalTo(3));
    }

    @Test
    public void queTireUnaCarta() {
        // given
        when(repositorioCarta.obtenerCartas()).thenReturn(esperadas);
        when(repositorioCarta.buscarCartaPorId(0L)).thenReturn(esperadas.get(0));
        Truco2 t = new Truco2();
        Mano m = servicioMano.empezar(t, j1, j2);
        // when
        whenAsignoTresCartasAlJugador(j1, esperadas, m);
        Ronda r = servicioMano.tirarCarta(m, j1, 0L, "1");
        // then
        assertNotNull(r);
    }


    private void whenAsignoTresCartasAlJugador(Jugador j, List<Carta> esperadas, Mano mano) {


        int contador = 0;
        Iterator<Carta> iterator = esperadas.iterator();
        while (iterator.hasNext() && contador < 3) {
            Carta carta = iterator.next();
            if (j.getNumero().equals(1)) {
                mano.getCartasJ1().add(carta);
            } else {
                mano.getCartasJ2().add(carta);
            }
            iterator.remove(); // Elimina de la lista principal.
            contador++;
            System.out.println("Asigno a " + j.getNombre()
                    + " la carta " + carta.getNumero() + " " + carta.getPalo() + ".");
        }
    }


}
