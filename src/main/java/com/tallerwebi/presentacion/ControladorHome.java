package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.ServicioUsuario;
import com.tallerwebi.dominio.Usuario;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ControladorHome {

    private ServicioUsuario servicioUsuario;

    public ControladorHome (ServicioUsuario servicioUsuario) {
        this.servicioUsuario = servicioUsuario;
    }

    @RequestMapping("/home")
    public ModelAndView irAlHome() {
        String email = "pepito@pepito.com";
        servicioUsuario.registrar(email,"");
//        Usuario usuario = servicioUsuario.buscar(email);
        ModelMap model = new ModelMap();
        model.put("email", email);
        return new ModelAndView("home", model);
    }

}
