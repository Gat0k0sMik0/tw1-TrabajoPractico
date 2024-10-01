package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.ServicioUsuario;
import com.tallerwebi.dominio.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ControladorPerfil {

    @RequestMapping("/perfil")
        public ModelAndView perfilUsuario() {
            ModelMap model = new ModelMap();
            model.put("fullname", "John Doe");
            model.put("username", "johndoe123");
            return new ModelAndView("perfil", model);
        }

    @RequestMapping("/modificar-perfil")
    public ModelAndView modificarPerfil() {
        ModelMap model = new ModelMap();
        return new ModelAndView("modificar-perfil", model);
    }


}