package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.*;
import com.tallerwebi.dominio.excepcion.AmistadesException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

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
    public ModelAndView irAChatAmigos(HttpServletRequest request) throws AmistadesException {
        ModelMap model = new ModelMap();
        Usuario usuarioSesion = (Usuario) request.getSession().getAttribute("usuarioActivo");
        List<Usuario> amigosDelUsuario = servicioAmistad.getAmigosDeUnUsuarioPorId(usuarioSesion.getId());
        List<Usuario> filtrados = servicioAmistad.obtenerRecomendacionesQueNoSeanSusAmigos(usuarioSesion.getId());

        model.put("amigosSugeridos", filtrados);  // Se pasa la lista correctamente al modelo

        if (amigosDelUsuario.isEmpty()) {
            model.put("error", "No tienes amigos, ¡empieza a agregar algunos!");
        } else {
            model.put("amigos", amigosDelUsuario);
        }

        model.put("usuarioActual", usuarioSesion);

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

    @RequestMapping(path = "/agregar-amigo", method = RequestMethod.POST)
    public ModelAndView agregarAmigo(@RequestParam("idAmigo") Long idAmigo, HttpServletRequest request) {
        ModelMap model = new ModelMap();
        Usuario usuarioSesion = (Usuario) request.getSession().getAttribute("usuarioActivo");
        Usuario amigo = servicioUsuario.buscarPorId(idAmigo);
        try {
            servicioAmistad.agregarAmigo(usuarioSesion, amigo);
            return new ModelAndView("redirect:/amigos");
        } catch (AmistadesException e) {
            model.put("error", e.getMessage());
            return new ModelAndView("amigos", model);
        }
    }

    @RequestMapping(path = "/eliminar-amigo", method = RequestMethod.POST)
    public ModelAndView eliminarAmigo(@RequestParam("idAmigo") Long idAmigo, HttpServletRequest request) {
        ModelMap model = new ModelMap();
        Usuario usuarioSesion = (Usuario) request.getSession().getAttribute("usuarioActivo");
        Usuario amigo = servicioUsuario.buscarPorId(idAmigo);
        try {
            servicioAmistad.eliminarAmigo(usuarioSesion, amigo);
            return new ModelAndView("redirect:/amigos");
        } catch (AmistadesException e) {
            model.put("error", e.getMessage());
            return new ModelAndView("amigos", model);
        }
    }
}
