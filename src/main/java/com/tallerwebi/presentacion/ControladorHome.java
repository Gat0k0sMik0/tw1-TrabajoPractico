package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.ServicioAmistad;
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
import java.util.List;

@Controller
public class ControladorHome {

    private ServicioUsuario servicioUsuario;
    private ServicioAmistad servicioAmistad;

    public ControladorHome (ServicioUsuario servicioUsuario, ServicioAmistad servicioAmistad) {
        this.servicioUsuario = servicioUsuario;
        this.servicioAmistad = servicioAmistad;
    }

    @RequestMapping("/home")
    public ModelAndView irAlHome(@ModelAttribute("email") String email) {
        ModelMap model = new ModelMap();

        Usuario usuario = servicioUsuario.buscar(email);
        // Verifica que el usuario esté logueado
        if (usuario == null) {
            return new ModelAndView("redirect:/login"); // Redirige al login si no hay sesión activa
        }

        // Obtén las recomendaciones de amigos
        List<Usuario> usuariosSugeridos = servicioAmistad.obtenerRecomendacionesQueNoSeanSusAmigos(usuario.getId());

        // Agrega las recomendaciones al modelo
        model.put("usuariosSugeridos", usuariosSugeridos);
        model.put("email", usuario.getEmail());
        model.put("usuario", usuario);
        model.put("IdUsuario", usuario.getId());

        return new ModelAndView("home", model);  // Retorna la vista home.html
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
