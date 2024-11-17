package com.tallerwebi.dominio;

import javax.persistence.*;
import java.util.List;

@Entity
public class Mano2 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "partida_id")
    private Truco2 partida;  // Relación con Partida (muchos a uno)

    @OneToMany(mappedBy = "mano", cascade = CascadeType.ALL)
    private List<Ronda2> rondas;  // Relación uno a muchos con Ronda

    private Boolean estaTerminada;

    public Mano2() {

    }

    public Boolean getEstaTerminada() {
        return estaTerminada;
    }

    public void setEstaTerminada(Boolean estaTerminada) {
        this.estaTerminada = estaTerminada;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Truco2 getPartida() {
        return partida;
    }

    public void setPartida(Truco2 partida) {
        this.partida = partida;
    }

    public List<Ronda2> getRondas() {
        return rondas;
    }

    public void setRondas(List<Ronda2> rondas) {
        this.rondas = rondas;
    }
}

// creo una mano
// la guardo
