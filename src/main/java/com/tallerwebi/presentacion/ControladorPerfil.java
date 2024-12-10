package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.*;
import com.tallerwebi.dominio.excepcion.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
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
    public ModelAndView perfilUsuario(@RequestParam(value = "id", required = false) Long idUsuario) {
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


   /* @RequestMapping("/modificar-perfil")
    public ModelAndView irAModificarPerfil(@RequestParam("idUsuario") Long idUsuario) {
        Usuario usuario = servicioUsuario.buscarPorId(idUsuario);
        if (usuario == null) {
            return new ModelAndView("redirect:/login");
        }

        ModelMap model = new ModelMap();
        model.put("usuarioActual", usuario); // Utiliza el usuario existente
        return new ModelAndView("modificar-perfil", model);
    }*/

    @PostMapping("/actualizar-perfil")
    public String actualizarPerfil(
            @RequestParam("idUsuario") Long idUsuario,
            @RequestParam("nombreUsuario") String nombreUsuario,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("confirmPassword") String confirmPassword,
            @RequestParam(value = "descripcion", required = false) String descripcion,
            Model model) {

        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Las contraseñas no coinciden.");
            return "perfil"; // Mostrar error y redirigir a la vista del perfil
        }

        try {
            // Buscar el usuario en la base de datos
            Usuario usuario = servicioUsuario.buscarPorId(idUsuario);
            if (usuario == null) {
                model.addAttribute("error", "Usuario no encontrado.");
                return "perfil";
            }

            // Actualizar los datos del usuario
            usuario.setNombreUsuario(nombreUsuario);
            usuario.setEmail(email);
            usuario.setPassword(password);  // Recuerda encriptar la contraseña
            usuario.setDescripcion(descripcion);

            // Guardar el usuario actualizado
            servicioUsuario.actualizarPerfil(usuario);

            model.addAttribute("mensaje", "Perfil actualizado con exito.");
            return "redirect:/perfil?id=" + usuario.getId();
        } catch (Exception e) {
            model.addAttribute("error", "Error al actualizar el perfil: " + e.getMessage());
            return "perfil"; // Mostrar error
        }
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