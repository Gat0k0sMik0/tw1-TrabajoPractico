package com.tallerwebi.dominio;

import java.time.LocalDateTime;
import java.util.List;

public interface RepositorioMensaje {
    void guardar(Mensaje mensaje);

    List<Mensaje> obtenerMensajeEntreUsuarios(Long fromUserId, Long toUserId);
}
