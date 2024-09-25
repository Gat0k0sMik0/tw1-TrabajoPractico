package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.ServicioTruco;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.dominio.Partida;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController;
public class ControladorTruco {

    @Autowired
    private ServicioTruco servicioTruco;

    @PostMapping("/iniciar");
    public Partida iniciarPartida(@RequestBody List<Usuario> usuario){
        return  ServicioTruco.iniciarPartida(usuario);
    }

    @PostMapping("/jugar");
    public Partida iniciarPartida(@RequestBody Jugada jugada){
        return  ServicioTruco.jugar(jugada);
    }
}
