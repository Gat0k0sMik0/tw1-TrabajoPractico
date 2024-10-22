package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Carta;
import com.tallerwebi.dominio.Jugador;
import com.tallerwebi.dominio.ServicioTruco;
import com.tallerwebi.dominio.ServicioUsuario;
import com.tallerwebi.infraestructura.ServicioTrucoImpl;
import com.tallerwebi.infraestructura.ServicioUsuarioImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ControladorTrucoTest {

    ServicioTruco servicioTruco = mock(ServicioTrucoImpl.class);
    ControladorTruco controladorTruco = new ControladorTruco(servicioTruco);

    @Test
    public void queSePrepareLaPartida() {
        // Jug 1, Jug 2
        // Mazo
        // 3 a cada uno
        Jugador j1 = new Jugador("gonzalo");
        Jugador j2 = new Jugador("leonel");
        j1.setCartas(List.of(new Carta(), new Carta(), new Carta()));
        j2.setCartas(List.of(new Carta(), new Carta(), new Carta()));
        servicioTruco.empezar(j1, j2);
        verify(servicioTruco).empezar(j1, j2);
        assertEquals(3, j1.getCartas().size());
        assertEquals(j1.getCartas().size(), j2.getCartas().size());
    }

    @Test
    public void queUnJugadorTire() {
        Long idCartaParam = 0L;
        Jugador j1 = new Jugador("gonzalo");
        Jugador j2 = new Jugador("leonel");
        Carta c1 = new Carta(0,4,"Basto");
        Carta c2 = new Carta(1,5,"Basto");
        Carta c3 = new Carta(2,6,"Basto");
        List <Carta> cartas = new ArrayList<>(List.of(c1, c2, c3));
        j1.setCartas(cartas);
        j2.setCartas(cartas);
        servicioTruco.empezar(j1, j2);
        if (j1.saberSiExiste(new Carta(0,4,"Basto"))) {
            j1.tirarCarta(new Carta(0,4,"Basto"));
            verify(servicioTruco).empezar(j1, j2);
            assertEquals(2, j1.getCartas().size());
            assertEquals(1, j1.getCartasTiradas().size());
        } else fail();
    }

    @Test
    public void queSeTireUnaCartaDeUnJugador() {
        Jugador j1 = new Jugador("gonzalo");
        Carta c1 = new Carta(0,4,"Basto");
        Carta c2 = new Carta(1,5,"Basto");
        Carta c3 = new Carta(2,6,"Basto");
        List <Carta> cartas = new ArrayList<>(List.of(c1, c2, c3));
        j1.setCartas(cartas);
        j1.tirarCarta(new Carta(0,4,"Basto"));
        assertEquals(2, j1.getCartas().size());
    }

}
