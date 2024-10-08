package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.ServicioTruco;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ControladorTruco {

    @RequestMapping("/partida-truco")
    public ModelAndView irAPartidaTruco() {
        ModelMap modelo = new ModelMap();
        modelo.put("jugador", "NombreDelJugador");
        return new ModelAndView("partida-truco", modelo);
    }
}