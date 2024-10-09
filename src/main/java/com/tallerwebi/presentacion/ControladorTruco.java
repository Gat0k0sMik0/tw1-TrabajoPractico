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


@Controller
public class ControladorTruco {

    private ServicioTruco servicioTruco;

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
            // Si no hay usuario activo en la sesión, redirige al inicio de sesión o lanza un error
            return new ModelAndView("redirect:/login"); // Cambia esto según tu vista de login
        }

        // Crear jugadores para la partida
        Jugador jugador1 = new Jugador(usuario.getNombreUsuario()); // Usuario logueado
        Jugador jugador2 = new Jugador("Juan"); // Jugador contrario o bot

        // Iniciar la partida
        servicioTruco.empezar(jugador1, jugador2);

        // Retornar la vista de la partida
        return new ModelAndView("partida-truco");
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