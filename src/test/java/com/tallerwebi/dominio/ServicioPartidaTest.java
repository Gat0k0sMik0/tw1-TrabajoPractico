package com.tallerwebi.dominio;

import com.tallerwebi.infraestructura.RepositorioCartaImpl;
import com.tallerwebi.infraestructura.RepositorioManoImpl;
import com.tallerwebi.infraestructura.RepositorioTrucoImpl;
import com.tallerwebi.infraestructura.ServicioPartidaImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ServicioPartidaTest {

    RepositorioTruco repositorioTruco = mock(RepositorioTrucoImpl.class);
    RepositorioCarta repositorioCarta = mock(RepositorioCartaImpl.class);
    RepositorioMano repositorioMano = mock(RepositorioManoImpl.class);

    ServicioPartida servicioTruco = new ServicioPartidaImpl(repositorioTruco, repositorioCarta, repositorioMano);

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
        carta1.setValor(0);
        carta1.setNumero(4);
        carta1.setPalo("Espadas");
        carta2.setValor(0);
        carta2.setNumero(7);
        carta2.setPalo("Espadas");
        carta3.setValor(0);
        carta3.setNumero(12);
        carta3.setPalo("Espadas");
        carta4.setValor(0);
        carta4.setNumero(1);
        carta4.setPalo("Copas");
        carta5.setValor(0);
        carta5.setNumero(2);
        carta5.setPalo("Copas");
        carta6.setValor(0);
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
    @Transactional
    public void queSeEmpiezeLaPartida() {

    }

    @Test
    @Transactional
    public void queMeTraigaUnaListaDePartidasCreadas() {
        Partida partida1 = new Partida();
        partida1.setId(0L);
        partida1.setJ1(j1);
        partida1.setJ2(null);
        partida1.setPuntosParaGanar(30);
        Partida partida2 = new Partida();
        partida2.setId(1L);
        partida2.setJ1(j2);
        partida2.setJ2(null);
        partida2.setPuntosParaGanar(30);
        when(servicioTruco.getTodasLasPartidas()).thenReturn(List.of(partida1, partida2));
        List<Partida> partidas = servicioTruco.getTodasLasPartidas();
        assertFalse(partidas.isEmpty());
    }

}
