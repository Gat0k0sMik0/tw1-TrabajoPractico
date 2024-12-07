package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.swing.*;
import java.util.List;

@Controller
public class ControladorMenuJuego {
    private final ServicioUsuario servicioUsuario;
    private final ServicioPartida servicioTruco;
    private final ServicioEstadisticas  servicioEstadisticas;

    @Autowired
    public ControladorMenuJuego(
            ServicioUsuario servicioUsuario,
            ServicioPartida servicioTruco, ServicioEstadisticas servicioEstadisticas) {
        this.servicioUsuario = servicioUsuario;
        this.servicioTruco = servicioTruco;
        this.servicioEstadisticas = servicioEstadisticas;
    }

    // Método para mostrar el menú de juego
    @RequestMapping("/menuJuego")
    public ModelAndView mostrarMenuJuego(@RequestParam("idUsuario") Long idUsuario) {
        Usuario ua = servicioUsuario.buscarPorId(idUsuario);
        if (ua == null) return new ModelAndView("redirect:/login");

        ModelMap model = new ModelMap();
        List<Partida> partidasDisponibles = servicioTruco.getPartidasDisponibles();
        List<Partida> partidas = servicioTruco.obtenerUltimas3PartidasDeUnJugador(ua);
        List <Estadistica> top =  servicioEstadisticas.obtenerTopJugadores();
        Estadistica misEstadisticas = servicioEstadisticas.obtenerEstadisticasDeUnJugador(ua);

        System.out.println(partidasDisponibles);

        model.put("partidasDisponibles", partidasDisponibles);
        model.put("partidas", partidas);
        model.put("usuario", ua);
        model.put("topJugadores", top);
        model.put("misEstadisticas", misEstadisticas);

        return new ModelAndView("menu-juego", model);
    }

    // Método para crear una sala
    @PostMapping("/crearSala")
    public ModelAndView crearSala(
            @RequestParam("idUsuario") Long idUsuario,
            @RequestParam("puntos") String puntosMaximos
    ) {

        // Obtener el usuario activo
        Usuario usuarioActivo = servicioUsuario.buscarPorId(idUsuario);
        if (usuarioActivo == null) return new ModelAndView("redirect:/login");

        // Redirigir a la vista de la partida
        return new ModelAndView("redirect:/espera?idJugador=" + usuarioActivo.getId() + "&ptsmax=" + puntosMaximos);
    }

    /*@GetMapping(path = "/verSalas")
    public ModelAndView verSalas(
            HttpSession session
    ) {
        Usuario ua = (Usuario) session.getAttribute("usuarioActivo");
        ua = servicioUsuario.buscarPorId(ua.getId());
        if (ua == null) return new ModelAndView("redirect:/login");

        ModelMap model = new ModelMap();
        List<Partida> partidas = servicioTruco.getPartidasDisponibles();

        model.put("partidas", partidas);
        model.put("ua", ua);
        return new ModelAndView("salas", model);
    }*/
}
