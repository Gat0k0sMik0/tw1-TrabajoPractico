package com.tallerwebi.dominio;

import com.tallerwebi.infraestructura.RepositorioManoImpl;
import com.tallerwebi.infraestructura.RepositorioRondaImpl;
import com.tallerwebi.infraestructura.ServicioRondaImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ServicioRondaTest {


    RepositorioRonda2 repositorioRonda2 = mock(RepositorioRondaImpl.class);
    RepositorioMano repositorioMano = mock(RepositorioManoImpl.class);

    ServicioRonda2 servicioRonda = new ServicioRondaImpl(repositorioRonda2, repositorioMano);

    Jugador j1 = new Jugador();
    Jugador j2 = new Jugador();

    @BeforeEach
    void preparo() {
        j1.setNombre("gonza");
        j2.setNombre("leo");
    }

    @Test
    public void queSeCreeLaRonda() {
        Mano2 m = new Mano2();
        Ronda r = servicioRonda.empezar(m);
        assertThat(r, notNullValue());
    }

    @Test
    public void queSeRegistreUnaRonda() {
        // given
        Mano2 m = new Mano2();
        Jugador jugador = new Jugador();
        Carta carta = new Carta();
        List<Ronda> rondas = new ArrayList<>();
        Integer contadorNroRonda = -1;
        Integer contadorMovimientos = 0;
        Ronda rondaCreadaCuandoTiras = new Ronda();

        givenTiroUnaCarta(m, jugador, carta, rondaCreadaCuandoTiras);

        // ronda (id, nro, jug, valor_carta, nro_carta, palo_carta)
        when(repositorioMano.obtenerManoPorId(0L)).thenReturn(m);
        Mano2 manoParaAgregarleRonda = repositorioMano.obtenerManoPorId(m.getId());
        // when
        if (manoParaAgregarleRonda != null) {
            rondaCreadaCuandoTiras.setMano(manoParaAgregarleRonda);
            if (contadorMovimientos++ % 2 == 0) {
                contadorNroRonda++;
            }
            rondaCreadaCuandoTiras.setNroRonda(contadorNroRonda);
            rondas.add(rondaCreadaCuandoTiras);
        }
        // then
        assertThat(rondas.size(), equalTo(1));
        assertThat(contadorMovimientos, equalTo(1));
        assertThat(contadorNroRonda, equalTo(0));
    }

    private void givenTiroUnaCarta(Mano2 m, Jugador jugador, Carta carta, Ronda rondaCreadaCuandoTiras) {
        m.setId(0L);
        jugador.setNombre("gonza");
        carta.setValor(0);
        carta.setPalo("Espadas");
        carta.setNumero(4);
        rondaCreadaCuandoTiras.setNombreJugador(jugador.getNombre());
        rondaCreadaCuandoTiras.setNroCarta(carta.getNumero());
        rondaCreadaCuandoTiras.setValorCarta(carta.getValor());
        rondaCreadaCuandoTiras.setPaloCarta(carta.getPalo());
    }


    private void algo() {
//        Truco truco = new Truco();
//        List<Carta> seis = new ArrayList<>();
//
//        seis.add(truco.getMazo().getCartaPorIndice(3)); // 4 espada xxx
//        seis.add(truco.getMazo().getCartaPorIndice(6)); // 7 espada x
//        seis.add(truco.getMazo().getCartaPorIndice(9)); // 12 espada xx
//
//        seis.add(truco.getMazo().getCartaPorIndice(30)); // 1 copa x
//        seis.add(truco.getMazo().getCartaPorIndice(31)); // 2 copa xx
//        seis.add(truco.getMazo().getCartaPorIndice(32)); // 3 copa xxx
    }
}
