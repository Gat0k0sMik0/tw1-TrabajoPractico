package com.tallerwebi.presentacion;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ControladorPerfilUsuario {

    @RequestMapping("/perfilUsuario")
        public ModelAndView perfilUsuario() {
            ModelMap model = new ModelMap();
            model.put("fullname", "John Doe");
            model.put("username", "johndoe123");
            return new ModelAndView("perfilUsuario", model);
        }

}
