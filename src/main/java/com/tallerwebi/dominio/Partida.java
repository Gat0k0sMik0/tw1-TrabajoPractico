/*package com.tallerwebi.dominio;

import javax.persistence.*;
import java.util.List;

@Entity
public class Partida {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany
    List <JugadorN> jugadores;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Mano> manos;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public JugadorN getJugador1() {
        return jugador1;
    }

    public void setJugador1(JugadorN jugador1) {
        this.jugador1 = jugador1;
    }

    public JugadorN getJugador2() {
        return jugador2;
    }

    public void setJugador2(JugadorN jugador2) {
        this.jugador2 = jugador2;
    }

    public List<Mano> getManos() {
        return manos;
    }

    public void setManos(List<Mano> manos) {
        this.manos = manos;
    }
}*/
