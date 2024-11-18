//package com.tallerwebi.presentacion;
//
//import com.tallerwebi.dominio.*;
//import com.tallerwebi.dominio.ServicioTruco;
//import com.tallerwebi.infraestructura.*;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.web.servlet.MockMvc;
//
//import javax.servlet.http.HttpSession;
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.hamcrest.MatcherAssert.assertThat;
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//public class ControladorTrucoTest {
//
//    ServicioPartida2 servicioTruco = mock(ServicioPartida2Impl.class);
//    ServicioMano2 servicioMano = mock(ServicioManoImpl2.class);
//    ServicioRonda2 servicioRonda = mock(ServicioRondaImpl.class);
//
//    ControladorTruco controladorTruco = new ControladorTruco(servicioTruco, servicioMano, servicioRonda);
//
//    Jugador j1 = new Jugador();
//    Jugador j2 = new Jugador();
//
//    private MockMvc mockMvc;
//
//    @BeforeEach
//    public void iniciarTest () {
//        j1.setNombre("Gonzalo");
//        j2.setNombre("Leo");
//    }
//
//    @Test
//    public void queSeComienceLaPartida() throws Exception {
//        when(servicioTruco.empezar(j1, j2)).thenReturn(new Truco2());
//        mockMvc.perform(get("/envido")
//                        .param("mano", "1")
//                        .param("ronda", "2")
//                        .param("idCarta", "3")
//                        .param("nroJugador", "4"))
//                .andExpect(status().isOk()) // Verifica que el estado sea 200
//                .andExpect(view().name("vistaEnvido")) // Verifica la vista retornada
//                .andExpect(model().attribute("mensaje", "Calculando envido para mano: 1, ronda: 2, carta: 3, jugador: 4")); // Verifica el modelo
//
//    }
//
//    @Test
//    public void queUnJugadorTire() {
//        Long idCartaParam = 0L;
//        Jugador j1 = new Jugador("gonzalo");
//        Jugador j2 = new Jugador("leonel");
//        Carta c1 = new Carta(0,4,"Basto");
//        Carta c2 = new Carta(1,5,"Basto");
//        Carta c3 = new Carta(2,6,"Basto");
//        List <Carta> cartas = new ArrayList<>(List.of(c1, c2, c3));
//        j1.setCartas(cartas);
//        j2.setCartas(cartas);
//        servicioTruco.empezar(jugadores);
//        if (j1.saberSiExiste(new Carta(0,4,"Basto"))) {
//            j1.tirarCarta(new Carta(0,4,"Basto"));
//            verify(servicioTruco).empezar(jugadores);
//            assertEquals(2, j1.getCartas().size());
//            assertEquals(1, j1.getCartasTiradas().size());
//        } else fail();
//    }
//
//
//}
