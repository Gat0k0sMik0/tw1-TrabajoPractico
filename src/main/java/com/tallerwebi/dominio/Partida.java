package com.tallerwebi.dominio;

import javax.persistence.*;
import java.util.List;

@Entity
public class Partida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "j1_id")
    private Jugador j1;

    @ManyToOne
    @JoinColumn(name = "j2_id")
    private Jugador j2;

    private Integer puntosJ1;
    private Integer puntosJ2;
    private Integer puntosParaGanar;
    private Boolean puedeEmpezar;
    private Boolean seGuardo;

    @OneToOne
    private Jugador ganador;


    public Partida() {

    }

    public Boolean getSeGuardo() {
        return seGuardo;
    }

    public void setSeGuardo(Boolean seGuardo) {
        this.seGuardo = seGuardo;
    }

    public Boolean getPuedeEmpezar() {
        return puedeEmpezar;
    }

    public void setPuedeEmpezar(Boolean puedeEmpezar) {
        this.puedeEmpezar = puedeEmpezar;
    }

    public Jugador getGanador() {
        return ganador;
    }

    public void setGanador(Jugador ganador) {
        this.ganador = ganador;
    }

    public Integer getPuntosParaGanar() {
        return puntosParaGanar;
    }

    public void setPuntosParaGanar(Integer puntosParaGanar) {
        this.puntosParaGanar = puntosParaGanar;
    }

    public Integer getPuntosJ1() {
        return puntosJ1;
    }

    public void setPuntosJ1(Integer puntosJ1) {
        this.puntosJ1 = puntosJ1;
    }

    public Integer getPuntosJ2() {
        return puntosJ2;
    }

    public void setPuntosJ2(Integer puntosJ2) {
        this.puntosJ2 = puntosJ2;
    }

    public Jugador getJ1() {
        return j1;
    }

    public void setJ1(Jugador j1) {
        this.j1 = j1;
    }

    public Jugador getJ2() {
        return j2;
    }

    public void setJ2(Jugador j2) {
        this.j2 = j2;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isPartidaFinalizada() {
        return this.ganador != null;
    }

    @Override
    public String toString() {
        return "Partida{" +
                "id=" + id +
                ", j1=" + j1 +
                ", j2=" + j2 +
                ", puntosJ1=" + puntosJ1 +
                ", puntosJ2=" + puntosJ2 +
                ", puntosParaGanar=" + puntosParaGanar +
                ", puedeEmpezar=" + puedeEmpezar +
                ", ganador=" + ganador +
                '}';
    }
}
