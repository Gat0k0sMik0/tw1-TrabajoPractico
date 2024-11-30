package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.dominio.excepcion.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import com.tallerwebi.dominio.ServicioUsuario;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class ControladorPerfil {

    private final ServicioUsuario servicioUsuario;

    public ControladorPerfil(ServicioUsuario servicioUsuario) {
        this.servicioUsuario = servicioUsuario;
    }

    @RequestMapping("/perfil")
    public ModelAndView perfilUsuario() {
        ModelMap model = new ModelMap();
        model.put("fullname", "John Doe");
        model.put("username", "johndoe123");
        return new ModelAndView("perfil", model);
    }


    @RequestMapping("/modificar-perfil")
    public ModelAndView irAModificarPerfil(HttpSession session) {
        Usuario u = (Usuario) session.getAttribute("usuarioActivo");
        if (u == null) return new ModelAndView("redirect:/login");

        Usuario usuario = servicioUsuario.buscarPorId(u.getId());
        if (usuario == null) return new ModelAndView("redirect:/login");

        ModelMap model = new ModelMap();

        model.put("datosUsuario", new Usuario());
        model.put("usuario", usuario);

        session.setAttribute("usuarioActivo", usuario);

        return new ModelAndView("modificar-perfil", model);
    }


    @PostMapping("/actualizar-perfil")
    public ModelAndView actualizarPerfil(
            @ModelAttribute("datosUsuario") Usuario usuarioNuevo,
            HttpSession session) {

        Usuario usuarioViejo = (Usuario)session.getAttribute("usuarioActivo");
        ModelMap model = new ModelMap();

        try {
            servicioUsuario.verificarDatos(usuarioViejo, usuarioNuevo);
        } catch (ActualizarUsuarioException e) {
            model.put("error", e.getMessage());
        }

        Usuario usuarioActualizado = servicioUsuario.actualizarPerfil(usuarioNuevo);
        session.setAttribute("usuarioActivo", usuarioActualizado);
        model.put("usuario", usuarioActualizado);
        model.addAttribute("mostrarModal", true);  // Variable para controlar el modal
        model.addAttribute("mensajeModal", "Usuario actualizado correctamente.");

        return new ModelAndView("modificar-perfil", model);
    }

    @GetMapping("/volver-home")
    public ModelAndView volverHome(HttpServletRequest request) {
        HttpSession sesion = request.getSession();
        Usuario usuarioLogueado = (Usuario) sesion.getAttribute("usuarioActivo");
        ModelMap modelo = new ModelMap();
        modelo.addAttribute("email", usuarioLogueado.getEmail());
        return new ModelAndView("home", modelo);
    }


}