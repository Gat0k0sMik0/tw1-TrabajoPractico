package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Mensaje;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WSChatController {

    @MessageMapping("/envio") // esta sobre /app -> /app/envio
    @SendTo("/tema/mensajes") // a donde envia ese mensaje
    public Mensaje envio (Mensaje mensaje) {
        return new Mensaje(mensaje.getNombre(), mensaje.getContenido());
    }

}
