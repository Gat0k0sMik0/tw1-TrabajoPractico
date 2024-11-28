package com.tallerwebi.dominio;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.List;

@Entity
public class SalaDeEspera {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombreJugador1;
    private String nombreJugador2;
    private Long idJugador1;
    private Long idJugador2;
    private Boolean partidaIniciada;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setNombreJugador1(String nombreJugador1) {
        this.nombreJugador1 = nombreJugador1;
    }

    public String getNombreJugador1() {
        return nombreJugador1;
    }

    public void setNombreJugador2(String nombreJugador2) {
        this.nombreJugador2 = nombreJugador2;
    }

    public String getNombreJugador2() {
        return nombreJugador2;
    }

    public Boolean isPartidaIniciada() {
        return partidaIniciada;
    }

    public void setPartidaIniciada(Boolean partidaIniciada) {
        this.partidaIniciada = partidaIniciada;
    }

    public Long getIdJugador1() {
        return idJugador1;
    }

    public void setIdJugador1(Long idJugador1) {
        this.idJugador1 = idJugador1;
    }

    public Long getIdJugador2() {
        return idJugador2;
    }

    public void setIdJugador2(Long idJugador2) {
        this.idJugador2 = idJugador2;
    }
}