package com.tallerwebi.dominio;

import java.time.LocalDateTime;
import java.util.List;

public interface ServicioMensajes {
    Mensaje guardar(Usuario envia, Usuario recibe, String content);

    List<Mensaje> obtenerMensajesEntreUsuarios(Long fromUserId, Long toUserId);
}
