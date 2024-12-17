package com.tallerwebi.presentacion;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tallerwebi.dominio.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import java.util.List;

@Controller
public class ControladorWebSocket {

    private final ServicioMensajes servicioMensajes;
    private final ServicioUsuario servicioUsuario;
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public ControladorWebSocket(
            ServicioMensajes servicioMensajes,
            ServicioUsuario servicioUsuario,
            @Qualifier("brokerMessagingTemplate") SimpMessagingTemplate messagingTemplate) {
        this.servicioMensajes = servicioMensajes;
        this.servicioUsuario = servicioUsuario;
        this.messagingTemplate = messagingTemplate;
    }

    // Muestra la vista del chat
    @GetMapping("/chat")
    public ModelAndView showChatPage(
            @RequestParam Long idUser1,
            @RequestParam Long idUser2
    ) {
        Usuario u1 = servicioUsuario.buscarPorId(idUser1);
        Usuario u2 = servicioUsuario.buscarPorId(idUser2);
        if (u1 == null || u2 == null) return new ModelAndView("redirect:/login");

        ModelMap model = new ModelMap();

        List<Mensaje> historial = servicioMensajes.obtenerMensajesEntreUsuarios(u1.getId(), u2.getId());

        model.put("u1", u1);
        model.put("idUsuario1", u1.getId());
        model.put("idUsuario2", u2.getId());
        model.put("u2", u2);
        model.put("usuarioActual", u1);
        model.put("historial", historial);

        return new ModelAndView("sala-chat", model);
    }

    @MessageMapping("/chat")
    @SendTo("/queue")
    public String handleChat(
            MensajeDTO mensaje
    ) throws JsonProcessingException {

        Usuario fromUser = servicioUsuario.buscarPorId(Long.parseLong(mensaje.getIdUsuario1()));
        Usuario toUser = servicioUsuario.buscarPorId(Long.parseLong(mensaje.getIdUsuario2()));
        servicioMensajes.guardar(fromUser, toUser, mensaje.getContent());

        // Crear un canal unificado basado en los IDs
        String chatChannel = String.format("/queue/chat/%s-%s",
                Math.min(Long.parseLong(mensaje.getIdUsuario1()), Long.parseLong(mensaje.getIdUsuario2())),
                Math.max(Long.parseLong(mensaje.getIdUsuario1()), Long.parseLong(mensaje.getIdUsuario2()))
        );

        // Enviar mensaje al canal
        messagingTemplate.convertAndSend(chatChannel, mensaje);
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(mensaje);
        return json;
    }

}
