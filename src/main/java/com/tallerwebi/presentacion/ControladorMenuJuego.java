package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class ControladorMenuJuego {
    private final ServicioSalaDeEspera servicioSalaDeEspera;

    @Autowired
    public ControladorMenuJuego(ServicioSalaDeEspera servicioSalaDeEspera) {
        this.servicioSalaDeEspera = servicioSalaDeEspera;
    }

    // Método para mostrar el menú de juego
    @RequestMapping("/menuJuego")
    public ModelAndView mostrarMenuJuego(HttpSession session) {
        ModelMap model = new ModelMap();

        // Obtener el usuario activo desde la sesión
        Usuario usuarioActivo = (Usuario) session.getAttribute("usuarioActivo");

        if (usuarioActivo != null) {
            // Obtén la lista de salas disponibles
            List<SalaDeEspera> salasDeEspera = servicioSalaDeEspera.obtenerSalas();
            model.addAttribute("salasDeEspera", salasDeEspera);
            model.addAttribute("usuarioActivo", usuarioActivo); // Pasar el usuario a la vista
        } else {
            // Si no hay un usuario activo, redirigir a login o mostrar mensaje
            model.addAttribute("error", "No se ha encontrado usuario activo");
            return new ModelAndView("inicioDeSesion"); // Redirigir a login (por ejemplo)
        }

        return new ModelAndView("menu-juego", model); // nombre del archivo HTML que muestra el menú
    }

    // Método para crear una sala
    @RequestMapping("/crearSala")
    public ModelAndView crearSala(@RequestParam("usuario1") String nombreUsuario, HttpSession session) {
        ModelMap model = new ModelMap();

        // Obtener el usuario activo desde la sesión
        Usuario usuarioActivo = (Usuario) session.getAttribute("usuarioActivo");

        // Crear un jugador con el nombre del usuario activo
        Jugador jugador1 = new Jugador();
        jugador1.setNombre(nombreUsuario);

        // Crear la sala con el jugador 1
        SalaDeEspera salaDeEspera = new SalaDeEspera();
        salaDeEspera.setNombreJugador1(jugador1.getNombre());
        salaDeEspera.setPartidaIniciada(false);

        // Guardar la sala
        servicioSalaDeEspera.guardarSala(salaDeEspera);

        // Almacenar jugador1 en la sesión
        session.setAttribute("jugador1", jugador1);
        session.setAttribute("sala", salaDeEspera);  // Asegúrate de guardar la sala también

        // Pasar la sala y jugador a la vista
        model.put("jugador1", jugador1);
        model.put("sala", salaDeEspera);

        // Redirigir a la vista de la partida
        return new ModelAndView("partida-truco", model);
    }

    @RequestMapping("/unirseSala")
    public ModelAndView unirseSala(@RequestParam("idSala") Long idSala, HttpSession session) {
        ModelMap model = new ModelMap();

        // Obtener el usuario activo desde la sesión
        Usuario usuarioActivo = (Usuario) session.getAttribute("usuarioActivo");
        if (usuarioActivo == null) {
            // Redirigir al login si no hay usuario activo
            return new ModelAndView("redirect:/login");
        }

        // Buscar la sala de espera por ID
        SalaDeEspera salaDeEspera = servicioSalaDeEspera.obtenerSalaPorId(idSala);
        if (salaDeEspera == null) {
            model.put("error", "La sala no existe.");
            return new ModelAndView("menu-juego", model);
        }

        // Verificar si la sala ya tiene un segundo jugador
        if (salaDeEspera.getNombreJugador2() != null) {
            model.put("error", "La sala ya está completa.");
            return new ModelAndView("menu-juego", model);
        }

        // Crear el segundo jugador y asignarlo a la sala
        Jugador jugador2 = new Jugador();
        jugador2.setNombre(usuarioActivo.getNombreUsuario());
        salaDeEspera.setNombreJugador2(jugador2.getNombre());

        // Almacenar el jugador2 en la sesión
        session.setAttribute("jugador2", jugador2);

        // Actualizar la sala en la base de datos
        servicioSalaDeEspera.actualizarSala(salaDeEspera);

        // Pasar jugadores y sala a la vista de la partida
        model.put("jugador1", salaDeEspera.getNombreJugador1());
        model.put("jugador2", jugador2.getNombre());
        return new ModelAndView("partida-truco", model);
    }

}
