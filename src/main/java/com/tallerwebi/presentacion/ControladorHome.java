package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.ServicioUsuario;
import com.tallerwebi.dominio.Usuario;
import org.hibernate.Session;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class ControladorHome {

    private ServicioUsuario servicioUsuario;

    public ControladorHome (ServicioUsuario servicioUsuario) {
        this.servicioUsuario = servicioUsuario;
    }

    @RequestMapping("/home")
    public ModelAndView irAlHome(@ModelAttribute("email") String email) {
        if (email.isEmpty()) {
            return new ModelAndView("redirect:/login");
        }
        Usuario usuario = servicioUsuario.buscar(email);
        if (usuario == null) {
            return new ModelAndView("redirect:/login");
        }
        ModelMap model = new ModelMap();
        model.put("email", usuario.getEmail());
        return new ModelAndView("home", model);
    }

    @RequestMapping("/volver-home")
    public ModelAndView volverHome(HttpSession session, RedirectAttributes redirectAttributes) {
        // Agregar atributos flash
        String nombreUsuario = (String) session.getAttribute("nombreUsuario");
        redirectAttributes.addFlashAttribute("email", session.getAttribute("email"));
        redirectAttributes.addFlashAttribute("nombreUsuario", nombreUsuario);

        // Redireccionar a la ruta /home
        return new ModelAndView("redirect:/home");
    }



}
