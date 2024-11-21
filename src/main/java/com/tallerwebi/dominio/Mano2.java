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
    private List<Ronda> rondas;  // Relación uno a muchos con Ronda

    private Boolean estaTerminada;
    private Integer movimientos = 0;

    public Mano2() {

    }

    public Integer getMovimientos() {
        return movimientos;
    }

    public void setMovimientos(Integer movimientos) {
        this.movimientos = movimientos;
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

    public List<Ronda> getRondas() {
        return rondas;
    }

    public void setRondas(List<Ronda> rondas) {
        this.rondas = rondas;
    }
}

// creo una mano
// la guardo
