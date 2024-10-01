package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.RepositorioUsuario;
import com.tallerwebi.dominio.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ControladorAmigos {
    @RequestMapping("/amigos")
    public ModelAndView irAChatAmigos(HttpServletRequest request) {
        ModelMap modelo = new ModelMap();
        modelo.put("urlAnterior", request.getHeader("Referer"));
        return new ModelAndView("amigos", modelo);
    }

}
