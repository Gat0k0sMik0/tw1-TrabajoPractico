//package com.tallerwebi.dominio;
//
//import com.tallerwebi.infraestructura.RepositorioManoImpl;
//import com.tallerwebi.infraestructura.RepositorioRondaImpl;
//import com.tallerwebi.infraestructura.RepositorioTrucoImpl;
//import com.tallerwebi.infraestructura.ServicioManoImpl2;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
//
//import static org.hamcrest.MatcherAssert.assertThat;
//import static org.hamcrest.Matchers.*;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
//public class ServicioManoTest {
//
//    RepositorioMano repositorioMano = mock(RepositorioManoImpl.class);
//    RepositorioTruco repositorioTruco = mock(RepositorioTrucoImpl.class);
//    RepositorioRonda2 repositorioRonda = mock(RepositorioRondaImpl.class);
//
//    ServicioMano2 servicioMano = new ServicioManoImpl2(repositorioMano, repositorioRonda, repositorioTruco);
//
//    Jugador j1 = new Jugador();
//    Jugador j2 = new Jugador();
//    List<Carta> esperadas = new ArrayList<>();
//
//    @BeforeEach
//    void preparo() {
//        j1.setNombre("gonza");
//        j2.setNombre("leo");
//        j1.setNumero(1);
//        j2.setNumero(2);
//        j1.setCartas(new ArrayList<>());
//        j2.setCartas(new ArrayList<>());
//
//        esperadas.add(new Carta(0, 4, "Espadas"));
//        esperadas.add(new Carta(0, 7, "Espadas"));
//        esperadas.add(new Carta(0, 12, "Espadas"));
//
//        esperadas.add(new Carta(0, 1, "Copas"));
//        esperadas.add(new Carta(0, 2, "Copas"));
//        esperadas.add(new Carta(0, 3, "Copas"));
//
//    }
//
//
//    @Test
//    void queSeGuardeLaMano () {
//        Truco2 t = new Truco2();
//        Mano2 mano = servicioMano.empezar(t);
//        assertThat(mano, notNullValue());
//    }
//
//    @Test
//    void queSeGuardeQuienCantoEnvido() {
//        // given
//        Jugador ejecutor = new Jugador();
//        Jugador receptor = new Jugador();
//        ejecutor.setNumero(1);
//        String ejecutorNroRecibido = "1";
//        Jugador diceEnvidoJ1 = null;
//        Jugador diceEnvidoJ2 = null;
//        // when
//        if (ejecutor.getNumero().toString().equals(ejecutorNroRecibido)) {
//            diceEnvidoJ1 = ejecutor;
//        } else {
//            diceEnvidoJ2 = ejecutor;
//        }
//        // then
//        assertThat(diceEnvidoJ1, notNullValue());
//    }
//
//    @Test
//    public void queLosPuntosEnJuegoDelEnvidoSean2() {
//        Truco2 t = new Truco2();
//        Mano2 m = new Mano2();
//        servicioMano.preguntar(t, m,"2", j1, j2);
//        assertThat(servicioMano.obtenerPuntosEnJuegoDelEnvido(), equalTo(2));
//    }
//
//    @Test
//    public void queLosPuntosEnJuegoDelEnvidoSean4() {
//        Truco2 t = new Truco2();
//        Mano2 m = new Mano2();
//        servicioMano.preguntar(t, m, "2", j1, j2);
//        Jugador leToca = servicioMano.responder(t,"2", "2", j1, j2);
//        assertThat(servicioMano.obtenerPuntosEnJuegoDelEnvido(), equalTo(4));
//        assertThat(leToca, notNullValue());
//    }
//
//    @Test
//    public void queLosPuntosEnJuegoDelEnvidoSean5() {
//        Truco2 t = new Truco2();
//        Mano2 m = new Mano2();
//        servicioMano.preguntar(t,m, "2", j1, j2);
//        Jugador leToca = servicioMano.responder(t,"2","3", j2, j1);
//        assertThat(servicioMano.obtenerPuntosEnJuegoDelEnvido(), equalTo(5));
//        assertThat(leToca, notNullValue());
//    }
//
//    @Test
//    public void queLosPuntosEnJuegoDelEnvidoSean7() {
//        Truco2 t = new Truco2();
//        Mano2 m = new Mano2();
//        servicioMano.preguntar(t, m,"2", j1, j2);
//        servicioMano.responder(t,"2","2", j2, j1);
//        Jugador leToca = servicioMano.responder(t,"2","3", j1, j2);
//        assertThat(servicioMano.obtenerPuntosEnJuegoDelEnvido(), equalTo(7));
//        assertThat(leToca, notNullValue());
//    }
//
//    @Test
//    public void queElFaltaEnvidoLoGaneElJugadorUno() {
//        // given
//        Truco2 t = new Truco2();
//        Mano2 m = new Mano2();
//        t.setPuntosParaGanar(30);
//        t.setPuntosJ1(0);
//        t.setPuntosJ2(0);
//        whenAsignoTresCartasAlJugador(j1, esperadas);
//        whenAsignoTresCartasAlJugador(j2, esperadas);
//        // When
//        servicioMano.preguntar(t,m,"4", j1, j2);
//        servicioMano.responder(t,"4","1", j2, j1);
//        // Then
//        assertThat(j1.getPuntosPartida(), equalTo(30));
//    }
//
//    @Test
//    public void queCanteTrucoYQueSeGuardenLosPuntosQueRecibiraSiGanaLaManoYSean2 () {
//        // given
//        Truco2 t = new Truco2();
//        Mano2 m = new Mano2();
//        t.setPuntosParaGanar(30);
//        t.setPuntosJ1(0);
//        t.setPuntosJ2(0);
//        whenAsignoTresCartasAlJugador(j1, esperadas);
//        whenAsignoTresCartasAlJugador(j2, esperadas);
//        // When
//        servicioMano.preguntar(t,m, "5", j1, j2);
//        servicioMano.responder(t,"5","1", j2, j1);
//        // Then
//        assertThat(servicioMano.obtenerPuntosEnJuegoPorTruco(), equalTo(2));
//    }
//
//    @Test
//    public void queCanteTrucoYQueSeGuardenLosPuntosQueRecibiraSiGanaLaManoYSean3 () {
//        // given
//        Truco2 t = new Truco2();
//        Mano2 m = new Mano2();
//        t.setPuntosParaGanar(30);
//        t.setPuntosJ1(0);
//        t.setPuntosJ2(0);
//        whenAsignoTresCartasAlJugador(j1, esperadas);
//        whenAsignoTresCartasAlJugador(j2, esperadas);
//        // When
//        servicioMano.preguntar(t, m,"5", j1, j2);
//        servicioMano.responder(t,"5","6", j2, j1);
//        servicioMano.responder(t,"6","1", j1, j2);
//        // Then
//        assertThat(servicioMano.obtenerPuntosEnJuegoPorTruco(), equalTo(3));
//    }
//
//    @Test
//    public void queCanteTrucoYQueSeGuardenLosPuntosQueRecibiraSiGanaLaManoYSean5 () {
//        // given
//        Truco2 t = new Truco2();
//        Mano2 m = new Mano2();
//        t.setPuntosParaGanar(30);
//        t.setPuntosJ1(0);
//        t.setPuntosJ2(0);
//        whenAsignoTresCartasAlJugador(j1, esperadas);
//        whenAsignoTresCartasAlJugador(j2, esperadas);
//        // When
//        Jugador respondeAhora = servicioMano.preguntar(t,m, "5", j1, j2);
//        assertThat(respondeAhora.getNombre(), equalToIgnoringCase(j2.getNombre()));
//        respondeAhora = servicioMano.responder(t,"5","6", j2, j1);
//        assertThat(respondeAhora.getNombre(), equalToIgnoringCase(j1.getNombre()));
//        respondeAhora = servicioMano.responder(t,"6","7", j1, j2);
//        assertThat(respondeAhora.getNombre(), equalToIgnoringCase(j2.getNombre()));
//        respondeAhora = servicioMano.responder(t,"7","1", j2, j1);
//        assertThat(respondeAhora, nullValue());
//        // Then
//        assertThat(servicioMano.obtenerPuntosEnJuegoPorTruco(), equalTo(4));
//    }
//
//    @Test
//    public void queElQueLeTocaResponderSeaElEsperadoTrasUnaSecuenciaDeAcciones () {
//        // given
//        Truco2 t = new Truco2();
//        Mano2 m = new Mano2();
//        t.setPuntosParaGanar(30);
//        t.setPuntosJ1(0);
//        t.setPuntosJ2(0);
//        whenAsignoTresCartasAlJugador(j1, esperadas);
//        whenAsignoTresCartasAlJugador(j2, esperadas);
//        // When/then
//        Jugador respondeAhora = servicioMano.preguntar(t, m,"5", j1, j2);
//        assertThat(respondeAhora.getNombre(), equalToIgnoringCase(j2.getNombre()));
//        respondeAhora = servicioMano.responder(t,"5","6", j2, j1);
//        assertThat(respondeAhora.getNombre(), equalToIgnoringCase(j1.getNombre()));
//        respondeAhora = servicioMano.responder(t,"6","7", j1, j2);
//        assertThat(respondeAhora.getNombre(), equalToIgnoringCase(j2.getNombre()));
//        respondeAhora = servicioMano.responder(t,"7","1", j2, j1);
//        assertThat(respondeAhora, nullValue());
//    }
//
//    @Test
//    public void queSeLeAsigneUnPuntoAlRivalPorIrseAlMazo() {
//        // given
//        Ronda r1 = new Ronda();
//        Ronda r2 = new Ronda();
//        List<Ronda> rs = new ArrayList<>();
//        rs.add(r1);
//        rs.add(r2);
//        Truco2 t = new Truco2();
//        Mano2 m = new Mano2();
//        m.setId(0L);
//        Jugador j1 = new Jugador();
//        j1.setNumero(1);
//        t.setPuntosParaGanar(30);
//        t.setPuntosJ1(0);
//        t.setPuntosJ2(0);
//        when(repositorioRonda.obtenerRondasDeUnaMano(0L)).thenReturn(new ArrayList<>());
//        // when
//        List<Ronda> rondasMano = this.repositorioRonda.obtenerRondasDeUnaMano(m.getId());
//        Integer puntosEnJuego = 1;
//        if (rondasMano.isEmpty()) {
//            puntosEnJuego = 2;
//        }
//        if (j1.getNumero().equals(1)) {
//            t.setPuntosJ2(t.getPuntosJ2() + puntosEnJuego);
//        } else {
//            t.setPuntosJ1(t.getPuntosJ1() + puntosEnJuego);
//        }
//        // then
//        assertThat(t.getPuntosJ2(), equalTo(2));
//    }
//
//    @Test
//    public void queSeLeAsigneDosPuntosAlRivalPorIrseAlMazo() {
//        // given
//        Ronda r1 = new Ronda();
//        Ronda r2 = new Ronda();
//        List<Ronda> rs = new ArrayList<>();
//        rs.add(r1);
//        rs.add(r2);
//        Truco2 t = new Truco2();
//        Mano2 m = new Mano2();
//        m.setId(0L);
//        Jugador j1 = new Jugador();
//        j1.setNumero(1);
//        t.setPuntosParaGanar(30);
//        t.setPuntosJ1(0);
//        t.setPuntosJ2(0);
//        when(repositorioRonda.obtenerRondasDeUnaMano(0L)).thenReturn(rs);
//        // when
//        List<Ronda> rondasMano = this.repositorioRonda.obtenerRondasDeUnaMano(m.getId());
//        Integer puntosEnJuego = 1;
//        if (rondasMano.isEmpty()) {
//            puntosEnJuego = 2;
//        }
//        if (j1.getNumero().equals(1)) {
//            t.setPuntosJ2(t.getPuntosJ2() + puntosEnJuego);
//        } else {
//            t.setPuntosJ1(t.getPuntosJ1() + puntosEnJuego);
//        }
//        // then
//        assertThat(t.getPuntosJ2(), equalTo(1));
//    }
//
//    @Test
//    public void siSeVaAlMazoQueSeaNullElQueLeToqueResponder() {
//        // given
//        Mano2 m = new Mano2();
//        m.setId(0L);
//        Truco2 t = new Truco2();
//        t.setPuntosJ1(0);
//        t.setPuntosJ2(0);
//        // when
//        Jugador respondeAhora = servicioMano.preguntar(t, m, "9", j1, j2);
//        // then
//        assertThat(respondeAhora, nullValue());
//
//    }
//
//
//    private void whenAsignoTresCartasAlJugador(Jugador j1, List<Carta> esperadas) {
//        int contador = 0;
//        Iterator<Carta> iterator = esperadas.iterator();
//        while (iterator.hasNext() && contador < 3) {
//            Carta carta = iterator.next();
//            j1.agregarCarta(carta);
//            iterator.remove(); // Elimina de la lista principal.
//            contador++;
//            System.out.println("Asigno a " + j1.getNombre()
//                    + " la carta " + carta.getNumero() + " " + carta.getPalo() + ".");
//        }
//    }
//
//
//
//
//}
