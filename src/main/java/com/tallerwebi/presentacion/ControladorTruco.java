package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;


@Controller
public class ControladorTruco {

    @Autowired
    private ServicioTruco servicioTruco;

    public ControladorTruco(ServicioTruco servicioTruco) {
        this.servicioTruco = servicioTruco;
    }

    // Comienzo -> repartir
    // Mesa cargada
    // Tiro
    // Mesa cargada
    // ...bucle hasta terminar...
    // Termina mano

    @RequestMapping("/partida-truco")
    public ModelAndView irAPartidaTruco(HttpSession session) {
        if (session.getAttribute("usuarioActivo") == null) {
            return new ModelAndView("redirect:/login");
        }

        List<Carta> cartasJugador1 = (List<Carta>) session.getAttribute("cartasJugador1");
        List<Carta> cartasJugador2 = (List<Carta>) session.getAttribute("cartasJugador2");


        ModelMap model = new ModelMap();
        model.put("cartasJugador1", cartasJugador1);
        model.put("cartasJugador2", cartasJugador2);
        model.put("jugador1", session.getAttribute("jugador1"));
        model.put("jugador2", session.getAttribute("jugador2"));
        model.put("cartasTiradasJ1", session.getAttribute("cartasTiradasJ1"));
        model.put("cartasTiradasJ2", session.getAttribute("cartasTiradasJ2"));
        model.put("turnoJugador", session.getAttribute("turnoJugador"));
        model.put("todasLasCartas", session.getAttribute("todasLasCartas"));
        model.put("jugadas", session.getAttribute("jugadas"));
        model.put("rondas", session.getAttribute("rondas"));

        boolean partidaIniciada = (cartasJugador1 != null && cartasJugador2 != null) &&
                (!cartasJugador1.isEmpty() || !cartasJugador2.isEmpty());
        model.put("partidaIniciada", partidaIniciada);

        return new ModelAndView("partida-truco", model);
    }


    @RequestMapping("/comenzar-truco")
    public ModelAndView comenzarTruco(HttpSession sesion) {
        Usuario usuario = (Usuario) sesion.getAttribute("usuarioActivo");
        if (usuario == null) return new ModelAndView("redirect:/login");

        // Asignamos usuario como jugador
        Jugador jugador1 = new Jugador(usuario.getNombreUsuario());
        Jugador jugador2 = new Jugador("Juan ElComeChancho");

        // Recuperar cartas de la sesion
        List<Carta> cartasJugador1 = (List<Carta>) sesion.getAttribute("cartasJugador1");
        List<Carta> cartasJugador2 = (List<Carta>) sesion.getAttribute("cartasJugador2");

        // Verificar si las cartas son null
        if (cartasJugador1 == null) {
            cartasJugador1 = new ArrayList<>();
        }
        if (cartasJugador2 == null) {
            cartasJugador2 = new ArrayList<>();
        }

        // Verificar si las cartas están agotadas
        boolean cartasAgotadas = cartasJugador1.isEmpty() && cartasJugador2.isEmpty();

        // Verificar si las cartas ya han sido repartidas
        Boolean cartasRepartidas = (Boolean) sesion.getAttribute("cartasRepartidas");

        if((cartasJugador1 != null && cartasJugador2 != null) &&
                (cartasJugador1.isEmpty() && cartasJugador2.isEmpty())){
            sesion.setAttribute("cartasTiradasJ1", null);
            sesion.setAttribute("cartasTiradasJ2", null);
            sesion.setAttribute("jugadas", null);
            sesion.setAttribute("rondas", null);
        }

        // Repartir cartas si nunca se repartieron o si están agotadas
        if (cartasRepartidas == null || !cartasRepartidas || cartasAgotadas) {
            servicioTruco.empezar(jugador1, jugador2);

            // Actualizar las cartas en la sesión
            cartasJugador1 = jugador1.getCartas();
            cartasJugador2 = jugador2.getCartas();

            // Marcar las cartas como repartidas
            sesion.setAttribute("cartasRepartidas", true);
        } else {
            // Si ya fueron repartidas, establecer las cartas desde la sesión
            jugador1.setCartas(cartasJugador1);
            jugador2.setCartas(cartasJugador2);
        }

        List<Carta> todasLasCartas = new ArrayList<>();
        todasLasCartas.addAll(cartasJugador1);
        todasLasCartas.addAll(cartasJugador2);
      
        sesion.setAttribute("cartasJugador1", cartasJugador1);
        sesion.setAttribute("cartasJugador2", cartasJugador2);
        sesion.setAttribute("jugador1", jugador1);
        sesion.setAttribute("jugador2", jugador2);
        sesion.setAttribute("todasLasCartas", todasLasCartas);
        sesion.setAttribute("partidaIniciada", true);

        return new ModelAndView("redirect:/partida-truco");
    }

    @RequestMapping(path = "/accion-tirar", method = RequestMethod.POST)
    public ModelAndView accionTirarCarta(
            @RequestParam("cartaId") Long cartaId,
            @RequestParam("jugador") String jugadorNombre,
            HttpSession session) {

        ModelMap model = new ModelMap();
        Jugador jugador1 = (Jugador) session.getAttribute("jugador1");
        Jugador jugador2 = (Jugador) session.getAttribute("jugador2");
        if (jugador1 == null || jugador2 == null) return new ModelAndView("redirect:/home");

        Carta cartaSeleccionada = null;
        Jugador actual = null;
        if (jugadorNombre.equalsIgnoreCase(jugador1.getNombre())) {
            actual = jugador1;
        } else {
            actual = jugador2;
        }

        servicioTruco.cambiarTurno(actual);

        // Buscar la carta seleccionada en la mano del jugador
        for (Carta carta : actual.getCartas()) {
            if (carta.getId().equals(cartaId)) {
                cartaSeleccionada = carta;
                break;
            }
        }

        servicioTruco.tirarCarta(actual, cartaSeleccionada);

        if (jugador1.getCartasTiradas().size() == jugador2.getCartasTiradas().size()) {
            servicioTruco.determinarGanadorRonda(jugador1, jugador2);
        }

        model.put("jugadas", servicioTruco.getRondasJugadas().size());
        model.put("rondas", servicioTruco.getRondasJugadas());
      
        session.setAttribute("jugadas", servicioTruco.getRondasJugadas().size());
        session.setAttribute("rondas", servicioTruco.getRondasJugadas());


        // Actualizar los jugadores en la sesión para mantener el estado del juego
        session.setAttribute("jugador1", jugador1);
        session.setAttribute("jugador2", jugador2);

        // Agregar las cartas jugadas al modelo para visualizarlas
        session.setAttribute("cartasTiradasJ1", jugador1.getCartasTiradas());
        session.setAttribute("cartasTiradasJ2", jugador2.getCartasTiradas());
        session.setAttribute("cartasJugador1", jugador1.getCartas());
        session.setAttribute("cartasJugador2", jugador2.getCartas());
        session.setAttribute("jugador1", jugador1);
        session.setAttribute("jugador2", jugador2);
        session.setAttribute("turnoJugador", servicioTruco.getTurnoJugador());


        return new ModelAndView("redirect:/partida-truco", model);
    }


}