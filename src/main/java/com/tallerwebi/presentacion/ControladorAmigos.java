package com.tallerwebi.presentacion;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ControladorAmigos {
    @RequestMapping("/amigos")
    public ModelAndView irAChatAmigos(HttpServletRequest request) {
        ModelMap modelo = new ModelMap();
        modelo.put("urlAnterior", request.getHeader("Referer"));
        return new ModelAndView("amigos", modelo);
    }
}

