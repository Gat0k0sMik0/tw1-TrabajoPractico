package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.*;
import com.tallerwebi.dominio.excepcion.AmistadesException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ControladorAmigos {

    @Autowired
    ServicioAmistad servicioAmistad;
    @Autowired
    ServicioUsuario servicioUsuario;

    public ControladorAmigos(ServicioAmistad servicioAmistad, ServicioUsuario servicioUsuario) {
        this.servicioAmistad = servicioAmistad;
        this.servicioUsuario = servicioUsuario;
    }

    @RequestMapping("/amigos")
    public ModelAndView irAmigos(@ModelAttribute("idUsuario") Long idUsuario) throws AmistadesException {
        ModelMap model = new ModelMap();
        Usuario usuarioActual = servicioUsuario.buscarPorId(idUsuario);
        List<Usuario> amigosDelUsuario = servicioAmistad.getAmigosDeUnUsuarioPorId(usuarioActual.getId());
        List<Usuario> filtrados = servicioAmistad.obtenerRecomendacionesQueNoSeanSusAmigos(usuarioActual.getId());

        model.put("amigosSugeridos", filtrados);
        model.put("amigos", amigosDelUsuario);
        model.put("usuarioActual", usuarioActual);

        return new ModelAndView("amigos", model);
    }
/*
    @RequestMapping("/amigos")
    public ModelAndView irAChatAmigos(HttpServletRequest request) throws AmistadesException {
        ModelMap model = new ModelMap();
        Usuario usuarioSesion = (Usuario) request.getSession().getAttribute("usuarioActivo");
        List<Usuario> amigosDelUsuario = servicioAmistad.getAmigosDeUnUsuarioPorId(usuarioSesion.getId());
        List<Usuario> filtrados = servicioAmistad.obtenerRecomendacionesQueNoSeanSusAmigos(usuarioSesion.getId());
        model.put("amigosSugeridos", filtrados);
        if (amigosDelUsuario.isEmpty()) {
            model.put("error", "No tenes amigos flaco");
        } else {
            model.put("amigos", amigosDelUsuario);
        }
        return new ModelAndView("amigos", model);
    }*/

    @RequestMapping(path = "/agregar-amigo")
    public ModelAndView agregarAmigo(@RequestParam("idAmigo") Long idAmigo, @RequestParam("idUsuario") Long idUsuario,
                                     RedirectAttributes redirectAttributes) throws AmistadesException {
        Usuario usuario = servicioUsuario.buscarPorId(idUsuario);
        Usuario amigo = servicioUsuario.buscarPorId(idAmigo);
        try {
            servicioAmistad.agregarAmigo(usuario, amigo);
            return new ModelAndView("redirect:/amigos?idUsuario=" + idUsuario);
        } catch (AmistadesException e) {
           redirectAttributes.addFlashAttribute("error", e.getMessage());
            return new ModelAndView("redirect:/amigos?idUsuario=" + usuario.getId());
        }
    }

    @RequestMapping(path = "/eliminar-amigo")
    public ModelAndView eliminarAmigo(@RequestParam("idAmigo") Long idAmigo,
                                      @RequestParam("idUsuario") Long idUsuario) {
        ModelMap model = new ModelMap();
        Usuario usuario = servicioUsuario.buscarPorId(idUsuario);
        Usuario amigo = servicioUsuario.buscarPorId(idAmigo);
        try {
            servicioAmistad.eliminarAmigo(usuario, amigo);
            return new ModelAndView("redirect:/amigos?idUsuario=" + idUsuario);
        } catch (AmistadesException e) {
            model.put("error", e.getMessage());
            return new ModelAndView("redirect:/amigos?idUsuario=" + usuario.getId());
        }
    }
}
