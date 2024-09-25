package com.tallerwebi.dominio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Service
public class ServicioTruco {
    private Partida partida;

    public class iniciarPartida (List <Usuario> usuario) {
        List<Carta> mazo = crearMazo();
        List <Usuario> jugadores = new ArrayList<>();
        jugadores.add(new Usuario(usuario.get(0), repartirCartas(mazo)));
        jugadores.add(new Usuario ("IA", repartirCartas(mazo)));
        partida = new Partida(jugadores);
        return partida;
    }

}
