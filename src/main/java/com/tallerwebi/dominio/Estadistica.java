package com.tallerwebi.dominio;

import javax.persistence.*;

@Entity
public class Estadistica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer ganadas;
    private Integer jugadas;
    private String juego;

    @ManyToOne
    @JoinColumn(name = "jugador_id", nullable = false) // Clave foránea
    private Jugador jugador;

    public Estadistica() {
    }

    public Estadistica(Integer ganadas, Integer jugadas, Jugador jugador, String juego) {
        this.ganadas = ganadas;
        this.jugadas = jugadas;
        this.jugador = jugador;
        this.juego = juego;
    }

    public String calcularNivel () {
        String nivel = "";
        if (this.ganadas >= 50) nivel = "Avanzado";
        else if (this.ganadas > 10) nivel = "Intermedio";
        else nivel = "Novato";
        return nivel;
    }

    public int getProgresoNivel() {
        if (ganadas < 20) {
            return (ganadas * 100) / 20; // Progreso hacia Rookie
        } else if (ganadas < 50) {
            return ((ganadas - 20) * 100) / 30; // Progreso hacia Medio
        } else {
            return 100; // Avanzado ya alcanzado
        }
    }

    public String getRatio() {
        if (jugadas == 0) {
            return "0%"; // Evitar división por cero
        }
        double ratio = (ganadas / (double) jugadas) * 100;
        return String.format("%.2f%%", ratio); // Formato con 2 decimales
    }

    public String getJuego() {
        return juego;
    }

    public void setJuego(String juego) {
        this.juego = juego;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getGanadas() {
        return ganadas;
    }

    public void setGanadas(Integer ganadas) {
        this.ganadas = ganadas;
    }

    public Integer getJugadas() {
        return jugadas;
    }

    public void setJugadas(Integer jugadas) {
        this.jugadas = jugadas;
    }

    public Jugador getJugador() {
        return jugador;
    }

    public void setJugador(Jugador jugador) {
        this.jugador = jugador;
    }

    @Override
    public String toString() {
        return "Estadistica{" +
                "id=" + id +
                ", ganadas=" + ganadas +
                ", jugadas=" + jugadas +
                ", juego='" + juego + '\'' +
                ", jugador=" + jugador +
                '}';
    }
}
