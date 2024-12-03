package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Mensaje;
import com.tallerwebi.dominio.RepositorioMensaje;
import com.tallerwebi.dominio.ServicioMensajes;
import com.tallerwebi.dominio.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ServicioMensajesImpl implements ServicioMensajes {

    RepositorioMensaje repositorioMensaje;

    @Autowired
    public ServicioMensajesImpl(RepositorioMensaje repositorioMensaje) {
        this.repositorioMensaje = repositorioMensaje;
    }

    @Override
    public Mensaje guardar(Usuario envia, Usuario recibe, String content) {
        Mensaje mensaje = new Mensaje();
        mensaje.setFromUser(envia);
        mensaje.setToUser(recibe);
        mensaje.setTimestamp(LocalDateTime.now());
        mensaje.setContent(content);
        this.repositorioMensaje.guardar(mensaje);
        System.out.println("Mensaje guardado exitosamente");
        System.out.println(mensaje);
        return mensaje;
    }

    @Override
    public List<Mensaje> obtenerMensajesEntreUsuarios(Long fromUserId, Long toUserId) {
        System.out.println("Buscando entre " + fromUserId + " y " + toUserId);
        return this.repositorioMensaje.obtenerMensajeEntreUsuarios(fromUserId, toUserId);
    }
}
