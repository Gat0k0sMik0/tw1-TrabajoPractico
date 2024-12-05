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
    private String urlNivel;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false) // Clave foránea
    private Usuario usuario;

    public Estadistica() {
    }

    public Estadistica(Integer ganadas, Integer jugadas, Usuario usuario, String juego) {
        this.ganadas = ganadas;
        this.jugadas = jugadas;
        this.usuario = usuario;
        this.juego = juego;
    }

    public void calcularNivel () {
        if(this.usuario.getNivel().equals("Oro") || this.usuario.getVictorias() >= 50) {
            urlNivel = "img/logos/divisiones/Oro.png";
        } else if (this.usuario.getNivel().equals("Plata") || this.usuario.getVictorias() >= 30) {
            urlNivel = "img/logos/divisiones/Plata.png";
        } else if (this.usuario.getNivel().equals("Bronce") || this.usuario.getVictorias() >= 10) {
            urlNivel = "img/logos/divisiones/Bronce.png";
        } else if (this.usuario.getNivel().equals("Sin Categoria") || this.usuario.getVictorias() < 50) {
            urlNivel = "img/logos/divisiones/SinDivision.png";
        } else {
            urlNivel = "img/logos/divisiones/SinDivision.png";
        }
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
        this.ganadas = this.usuario.getVictorias();
        return ganadas;
    }

    public void setGanadas(Integer ganadas) {
        this.usuario.setVictorias(ganadas);
        this.ganadas = ganadas;
    }

    public Integer getJugadas() {
        return jugadas;
    }

    public void setJugadas(Integer jugadas) {
        this.jugadas = jugadas;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getUrlNivel() {
        this.calcularNivel();
        return urlNivel;
    }

    public void setUrlNivel(String urlNivel) {
        this.urlNivel = urlNivel;
    }

    @Override
    public String toString() {
        return "Estadistica{" +
                "id=" + id +
                ", ganadas=" + ganadas +
                ", jugadas=" + jugadas +
                ", juego='" + juego + '\'' +
                ", usuario=" + usuario +
                '}';
    }
}
