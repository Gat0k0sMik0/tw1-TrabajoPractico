package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;


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
    public ModelAndView comenzarPartidaTruco(@RequestParam(value = "cartaTirada", required = false )Long idCartaTirada, HttpServletRequest request) {
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
            // Almacenar jugadores en la sesión
            sesion.setAttribute("jugador1", jugador1);
            sesion.setAttribute("jugador2", jugador2);

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
        if(idCartaTirada != null){
            model.put("cartaTirada", idCartaTirada);
        }

        return new ModelAndView("partida-truco", model);
    }

    @RequestMapping(path="/tirar-carta", method = RequestMethod.POST)
    public ModelAndView tirarCarta(@RequestParam("idCartaSeleccionada") Long cartaId, HttpServletRequest request) {
        HttpSession sesion = request.getSession();
        Jugador jugador1 = (Jugador) sesion.getAttribute("jugador1");
        Carta cartaTirada = servicioTruco.tirarCarta(jugador1, cartaId);
        ModelMap model = new ModelMap();
        model.put("cartaTirada", cartaTirada);
        return new ModelAndView("redirect:/comenzar-partida-truco", model);
    }
}