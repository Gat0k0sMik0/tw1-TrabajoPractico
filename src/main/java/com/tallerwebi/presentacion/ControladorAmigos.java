package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.*;
import com.tallerwebi.dominio.excepcion.AmistadesException;
import org.dom4j.rule.Mode;
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
    @Autowired
    ServicioSolicitudAmistad servicioSolicitudAmistad;

    public ControladorAmigos(ServicioAmistad servicioAmistad, ServicioUsuario servicioUsuario, ServicioSolicitudAmistad servicioSolicitudAmistad) {
        this.servicioAmistad = servicioAmistad;
        this.servicioUsuario = servicioUsuario;
        this.servicioSolicitudAmistad = servicioSolicitudAmistad;
    }

    @RequestMapping("/amigos")
    public ModelAndView irAmigos(@ModelAttribute("idUsuario") Long idUsuario) throws AmistadesException {
        ModelMap model = new ModelMap();
        Usuario usuarioActual = servicioUsuario.buscarPorId(idUsuario);

        // Obtenemos a nuestros amigos
        List<Usuario> amigosDelUsuario = servicioAmistad.getAmigosDeUnUsuarioPorId(usuarioActual.getId());
        // Filtramos a los usuarios, sacamos a los amigos y a los que enviamos o nos enviaron solicitud
        List<Usuario> filtrados = servicioAmistad.obtenerRecomendacionesQueNoSeanSusAmigos(usuarioActual.getId());
        filtrados = servicioSolicitudAmistad.queNoTenganSolicitudesEntreEllos(idUsuario, filtrados);
        //Obtenemos las solicitudes enviadas y recibidas
        List<SolicitudAmistad> solicitudesEnviadas = servicioSolicitudAmistad.obtenerSolicitudesEnviadas(idUsuario);
        List<SolicitudAmistad> solicitudesRecibidas = servicioSolicitudAmistad.obtenerSolicitudesRecibidasNoAceptadas(idUsuario);

        model.put("amigosSugeridos", filtrados);
        model.put("amigos", amigosDelUsuario);
        model.put("usuarioActual", usuarioActual);
        model.put("solicitudesEnviadas", solicitudesEnviadas);
        model.put("solicitudesRecibidas", solicitudesRecibidas);

        return new ModelAndView("amigos", model);
    }

    @RequestMapping(path = "/eliminar-amigo")
    public ModelAndView eliminarAmigo(@RequestParam("idAmigo") Long idAmigo,
                                      @RequestParam("idUsuario") Long idUsuario) {
        ModelMap model = new ModelMap();
        Usuario usuario = servicioUsuario.buscarPorId(idUsuario);
        Usuario amigo = servicioUsuario.buscarPorId(idAmigo);

        try {
            servicioAmistad.eliminarAmigo(usuario, amigo);
            servicioSolicitudAmistad.eliminarSolicitudEntreAmbos(idAmigo, idUsuario);
            return new ModelAndView("redirect:/amigos?idUsuario=" + idUsuario);
        } catch (AmistadesException e) {
            model.put("error", e.getMessage());
            return new ModelAndView("redirect:/amigos?idUsuario=" + usuario.getId());
        }
    }

    // SOLICITUDES
    @RequestMapping("/enviar-solicitud")
    public ModelAndView enviarSolicitud(@ModelAttribute("receptorId") Long receptorId,
                                        @ModelAttribute("idUsuario") Long idUsuario,
                                        RedirectAttributes redirectAttributes) {
        try {
            servicioSolicitudAmistad.enviarSolicitudAmistad(idUsuario, receptorId);
            redirectAttributes.addFlashAttribute("mensaje", "Solicitud de amistad enviada correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return new ModelAndView("redirect:/amigos?idUsuario=" + idUsuario);
    }

    // Aceptar solicitud de amistad
    @RequestMapping("/aceptar-solicitud")
    public ModelAndView aceptarSolicitud(@ModelAttribute("solicitudId") Long solicitudId,
                                        @ModelAttribute("idUsuario") Long idUsuario,
                                        RedirectAttributes redirectAttributes) {
        try {
            servicioSolicitudAmistad.aceptarSolicitudAmistad(solicitudId);
            redirectAttributes.addFlashAttribute("mensaje", "Solicitud de amistad aceptada.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "La solicitud expiró.");
        } catch (AmistadesException e) {
            throw new RuntimeException(e);
        }
        servicioSolicitudAmistad.eliminarSolicitud(solicitudId);
        return new ModelAndView("redirect:/amigos?idUsuario=" + idUsuario);
    }

    // Rechazar solicitud de amistad
    @RequestMapping("/rechazar-solicitud")
    public ModelAndView rechazarSolicitud(@ModelAttribute("solicitudId") Long solicitudId,
                                          @ModelAttribute("idUsuario") Long idUsuario,
                                          RedirectAttributes redirectAttributes) {
        try {
            servicioSolicitudAmistad.rechazarSolicitudAmistad(solicitudId);
            redirectAttributes.addFlashAttribute("mensaje", "Solicitud de amistad rechazada.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "La solicitud expiró.");
        }
        servicioSolicitudAmistad.eliminarSolicitud(solicitudId);
        return new ModelAndView("redirect:/amigos?idUsuario=" + idUsuario);
    }

    @RequestMapping("/cancelar-solicitud")
    public ModelAndView cancelarSolicitud(@ModelAttribute("solicitudId") Long solicitudId,
                                          @ModelAttribute("idUsuario") Long idUsuario,
                                          RedirectAttributes redirectAttributes) {
        try {
            servicioSolicitudAmistad.eliminarSolicitud(solicitudId);
            redirectAttributes.addFlashAttribute("mensaje", "Solicitud de amistad cancelada.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "La solicitud ya ha sido respondida.");
        }
        return new ModelAndView("redirect:/amigos?idUsuario=" + idUsuario);
    }

    //    @RequestMapping(path = "/agregar-amigo")
//    public ModelAndView agregarAmigo(@ModelAttribute("idAmigo") Long idAmigo,
//                                     @ModelAttribute("idUsuario") Long idUsuario,
//                                     RedirectAttributes redirectAttributes) {
//        Usuario usuario = servicioUsuario.buscarPorId(idUsuario);
//        Usuario amigo = servicioUsuario.buscarPorId(idAmigo);
//        try {
//            servicioAmistad.agregarAmigo(usuario, amigo);
//            return new ModelAndView("redirect:/amigos?idUsuario=" + idUsuario);
//        } catch (AmistadesException e) {
//           redirectAttributes.addFlashAttribute("error", e.getMessage());
//            return new ModelAndView("redirect:/amigos?idUsuario=" + usuario.getId());
//        }
//    }
}
