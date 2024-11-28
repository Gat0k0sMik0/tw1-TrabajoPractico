package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.RepositorioSalaDeEspera;
import com.tallerwebi.dominio.SalaDeEspera;
import com.tallerwebi.dominio.ServicioSalaDeEspera;
import com.tallerwebi.dominio.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

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
    public ModelAndView mostrarMenuJuego() {
        ModelMap model = new ModelMap();
        // Obtén la lista de salas disponibles
        List<SalaDeEspera> salasDeEspera = servicioSalaDeEspera.obtenerSalas();
        model.addAttribute("salasDeEspera", salasDeEspera);
        return new ModelAndView("menu-juego", model); // nombre del archivo HTML que muestra el menú
    }

    // Método para crear una sala
    @RequestMapping("/crearSala")
    public ModelAndView crearSala(@RequestParam("usuario1") Usuario usuario1) {
        ModelMap model = new ModelMap();
        SalaDeEspera salaDeEspera = new SalaDeEspera();
        salaDeEspera.setNombreJugador1(usuario1.getNombreUsuario());
        salaDeEspera.setPartidaIniciada(false);
        servicioSalaDeEspera.guardarSala(salaDeEspera);
        model.put("jugador1", usuario1);
        return new ModelAndView("partida-truco"); // Redirigir al menú después de crear la sala
    }

    // Método para unirse a una sala
    @RequestMapping("/unirseSala")
    public String unirseSala(@RequestParam("idSala") Long idSala, @RequestParam("nombreJugador2") String nombreJugador2) {
        SalaDeEspera salaDeEspera = servicioSalaDeEspera.obtenerSalaPorId(idSala);
        if (salaDeEspera != null && !salaDeEspera.isPartidaIniciada() && salaDeEspera.getNombreJugador2() == null) {
            salaDeEspera.setNombreJugador2(nombreJugador2);
            servicioSalaDeEspera.guardarSala(salaDeEspera);
        }
        return "redirect:/menuJuego";
    }

}
