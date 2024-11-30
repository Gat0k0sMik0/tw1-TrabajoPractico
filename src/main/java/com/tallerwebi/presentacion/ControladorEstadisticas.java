package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Estadistica;
import com.tallerwebi.dominio.ServicioEstadisticas;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class ControladorEstadisticas {

    private ServicioEstadisticas servicioEstadisticas;

    @Autowired
    public ControladorEstadisticas(ServicioEstadisticas servicioEstadisticas) {
        this.servicioEstadisticas = servicioEstadisticas;
    }

    @RequestMapping("/estadisticas")
    public ModelAndView obtenerEstadisticas() {
        ModelMap model = new ModelMap();
        List<Estadistica> estadisticas = servicioEstadisticas.obtenerTodasLasEstadisticas();
        model.addAttribute("estadisticas", estadisticas);
        return new ModelAndView("estadisticas", model);
    }
}


