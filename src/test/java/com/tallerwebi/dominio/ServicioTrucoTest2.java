package com.tallerwebi.dominio;

import com.tallerwebi.infraestructura.RepositorioCartaImpl;
import com.tallerwebi.infraestructura.RepositorioTrucoImpl;
import com.tallerwebi.infraestructura.ServicioPartida2Impl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class ServicioTrucoTest2 {

    RepositorioTruco repositorioTruco = mock(RepositorioTrucoImpl.class);
    RepositorioCarta repositorioCarta = mock(RepositorioCartaImpl.class);

    ServicioPartida2 servicioTruco = new ServicioPartida2Impl(repositorioTruco, repositorioCarta);

    Jugador j1 = new Jugador();
    Jugador j2 = new Jugador();
    CopyOnWriteArrayList<Carta> esperadas = new CopyOnWriteArrayList<>();

    @BeforeEach
    void preparo() {
        esperadas.add(new Carta(0, 4, "Espadas"));
        esperadas.add(new Carta(0, 7, "Espadas"));
        esperadas.add(new Carta(0, 12, "Espadas"));
        esperadas.add(new Carta(0, 1, "Copas"));
        esperadas.add(new Carta(0, 2, "Copas"));
        esperadas.add(new Carta(0, 3, "Copas"));
        j1.setNombre("gonza");
        j2.setNombre("leo");
    }

    @Test
    @Transactional
    public void queSeEmpiezeLaPartida() {

    }

    @Test
    public void queSeLeAsignenLasCartas () {
        // given
        when(repositorioCarta.obtenerCartas()).thenReturn(esperadas); // solo 6
        List<Carta> cartasRepo = repositorioCarta.obtenerCartas();
        // when
        whenAsignoCartasAlJugador(j1, cartasRepo);
        whenAsignoCartasAlJugador(j2, cartasRepo);
        // then
        verify(repositorioCarta, times(1)).obtenerCartas();
        assertThat(j1.getCartas().size(), equalTo(3));
    }

    private void whenAsignoCartasAlJugador(Jugador jugador, List<Carta> cartas) {
        for (Carta carta : cartas) {
            if (jugador.getCartas().size() < 3) {
                jugador.agregarCarta(carta);
                cartas.remove(carta);
            } else break;
        }
    }


}
