package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.ServicioInicio;
import com.tallerwebi.dominio.ServicioLogin;
import com.tallerwebi.dominio.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
@Controller
public class ControladorInicio {

    @Autowired
    private ServicioInicio servicioInicio;

    // Redirigir a la vista de inicio de sesión
    @RequestMapping("/InicioDeSesion")
    public ModelAndView irAInicioDeSesion() {
        ModelMap model = new ModelMap();
        model.put("datosInicio", new DatosInicio());
        return new ModelAndView("InicioDeSesion", model);
    }

    // Validar datos de inicio
    @RequestMapping(path = "/validar-inicio", method = RequestMethod.POST)
    public ModelAndView validarLogin(@ModelAttribute("datosInicio") DatosInicio datosInicio, HttpServletRequest request) {

        ModelMap model = new ModelMap();
        Usuario usuarioBuscado = servicioInicio.consultarUsuario(datosInicio.getNombre(), datosInicio.getPassword());
        if (usuarioBuscado != null) {
            request.getSession().setAttribute("ROL", usuarioBuscado.getRol());
            return new ModelAndView("redirect:/home");
        } else {
            model.put("error", "Usuario o clave incorrecta");
        }
        return new ModelAndView("InicioDeSesion", model);
    }

}