package com.tallerwebi.dominio;

import javax.persistence.*;

@Entity
public class Truco2 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Jugador j1;
    @ManyToOne
    private Jugador j2;

    private Integer puntosJ1;
    private Integer puntosJ2;
    private Integer puntosParaGanar;

    public Truco2() {

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

    @Override
    public String toString() {
        return "Truco2{" +
                "id=" + id +
                ", j1=" + j1 +
                ", j2=" + j2 +
                '}';
    }
}
