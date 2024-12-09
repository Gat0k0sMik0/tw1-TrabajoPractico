package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.*;
import com.tallerwebi.dominio.excepcion.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class ControladorPerfil {

    private final ServicioUsuario servicioUsuario;
    private final ServicioEstadisticas servicioEstadisticas;
    private final ServicioPartida servicioPartida;
    private final ServicioAmistad servicioAmistad;

    public ControladorPerfil(ServicioUsuario servicioUsuario, ServicioEstadisticas servicioEstadisticas, ServicioPartida servicioPartida, ServicioAmistad servicioAmistad) {
        this.servicioUsuario = servicioUsuario;
        this.servicioEstadisticas = servicioEstadisticas;
        this.servicioPartida = servicioPartida;
        this.servicioAmistad = servicioAmistad;
    }

    @RequestMapping("/perfil")
    public ModelAndView perfilUsuario(@ModelAttribute("id") Long idUsuario) {
        ModelMap model = new ModelMap();

        Usuario usuario = servicioUsuario.buscarPorId(idUsuario);
        if (usuario == null) return new ModelAndView("redirect:/login");

        Partida ultimaPartida = servicioPartida.obtenerUltimaPartidaDeUnJugador(usuario);
        Estadistica estadisticas = servicioEstadisticas.obtenerEstadisticasDeUnJugador(usuario);
        // Obtenenemos 4 amigos aleatorios
        List<Usuario> amigosDelUsuario = servicioAmistad.getAmigosDeUnUsuarioPorId(usuario.getId());
        if (amigosDelUsuario.size() > 4) {
            amigosDelUsuario = amigosDelUsuario.subList(0, 4);
        }

        model.put("usuarioActual", usuario);
        model.put("misEstadisticas", estadisticas);
        model.put("ultimaPartida", ultimaPartida);
        model.put("amigosDelUsuario", amigosDelUsuario);

        return new ModelAndView("perfil", model);
    }


    @RequestMapping("/modificar-perfil")
    public ModelAndView irAModificarPerfil(@ModelAttribute("id") Long idUsuario) {
        Usuario usuario = servicioUsuario.buscarPorId(idUsuario);
        if (usuario == null) return new ModelAndView("redirect:/login");

        ModelMap model = new ModelMap();

        model.put("datosUsuario", new Usuario());
        model.put("usuario", usuario);

        return new ModelAndView("modificar-perfil", model);
    }


    @PostMapping("/actualizar-perfil")
    public ModelAndView actualizarPerfil(
            @ModelAttribute("datosUsuario") Usuario usuarioActual,
            RedirectAttributes redirectAttributes) {

        servicioUsuario.actualizarPerfil(usuarioActual);

        Usuario usuarioActualizado = servicioUsuario.actualizarPerfil(usuarioActual);
        redirectAttributes.addAttribute("mensaje", "Usuario actualizado correctamente.");

        return new ModelAndView("modificar-perfil");
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