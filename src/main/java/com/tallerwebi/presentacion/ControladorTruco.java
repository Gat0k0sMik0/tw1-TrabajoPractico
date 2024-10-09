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
import java.util.List;


@Controller
public class ControladorTruco {

    @Autowired
    private ServicioTruco servicioTruco;

    public ControladorTruco(ServicioTruco servicioTruco) {
        this.servicioTruco = servicioTruco;
    }

    @RequestMapping("/partida-truco")
    public ModelAndView irAPartidaTruco() {
        ModelMap modelo = new ModelMap();
        modelo.put("jugador", "NombreDelJugador");
        return new ModelAndView("partida-truco", modelo);
    }

    @RequestMapping(path = "/comenzar-partida-truco", method = RequestMethod.GET)
    public ModelAndView comenzarPartidaTruco(HttpServletRequest request) {
        HttpSession sesion = request.getSession();
        Usuario usuario = (Usuario) sesion.getAttribute("usuarioActivo");

        if (usuario == null) {
            return new ModelAndView("redirect:/login");
        }

        Jugador jugador1 = new Jugador(usuario.getNombreUsuario());
        Jugador jugador2 = new Jugador("Juan ElComeChancho");

        // Recuperar cartas de la sesión
        List<Carta> cartasJugador1 = (List<Carta>) sesion.getAttribute("cartasJugador1");
        List<Carta> cartasJugador2 = (List<Carta>) sesion.getAttribute("cartasJugador2");

        if (cartasJugador1 == null || cartasJugador2 == null) {
            // Iniciar la partida y generar cartas si no hay cartas en la sesión
            servicioTruco.empezar(jugador1, jugador2);

            cartasJugador1 = jugador1.getCartas();
            cartasJugador2 = jugador2.getCartas();

            // Almacenar cartas en la sesión
            sesion.setAttribute("cartasJugador1", cartasJugador1);
            sesion.setAttribute("cartasJugador2", cartasJugador2);
        } else {
            // Crear los jugadores y recuperar sus cartas de la sesión
            jugador1.setCartas(cartasJugador1);
            jugador2.setCartas(cartasJugador2);
        }

        ModelMap model = new ModelMap();
        model.put("cartasJugador1", cartasJugador1);
        model.put("cartasJugador2", cartasJugador2);
        model.put("Jugador1", jugador1);
        model.put("Jugador2", jugador2);
        model.put("partidaIniciada", true);

        return new ModelAndView("partida-truco", model);
    }

    @RequestMapping(path="/tirar-carta",  method = RequestMethod.POST)
    public String tirarCarta(@RequestParam("cartaId") Long cartaId, HttpSession session, Model model) {
        // Obtener el jugador actual de la sesión
        Jugador jugador = (Jugador) session.getAttribute("jugadorActual");

        // Buscar la carta seleccionada en la mano del jugador
        Carta cartaSeleccionada = null;
        for (Carta carta : jugador.getCartas()) {
            if (carta.getId().equals(cartaId)) {
                cartaSeleccionada = carta;
                break;
            }
        }

        // Verificar si se encontró la carta seleccionada
        if (cartaSeleccionada == null) {
            // Si no se encuentra la carta, mostrar un error
            model.addAttribute("error", "La carta seleccionada no está en la mano del jugador.");
            return "error"; // Redirigir a una página de error si lo prefieres
        }

        // Llamar al servicio para tirar la carta
        servicioTruco.tirarCarta(jugador, cartaSeleccionada);

        // Agregar las cartas jugadas al modelo para visualizarlas
        model.addAttribute("cartasJugadas", servicioTruco.getCartasJugadas());

        // Redirigir a la página de la partida
        return "redirect:/truco/partida-truco";
    }

}