package com.tallerwebi.dominio;

import javax.persistence.*;

@Entity
public class SolicitudAmistad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Usuario solicitante; // El que envía la solicitud

    @ManyToOne
    private Usuario receptor; // El que recibe la solicitud

    private String estado; // Ejemplo: PENDIENTE, ACEPTADA, RECHAZADA

    // Getters y Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getSolicitante() {
        return solicitante;
    }

    public void setSolicitante(Usuario solicitante) {
        this.solicitante = solicitante;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Usuario getReceptor() {
        return receptor;
    }

    public void setReceptor(Usuario receptor) {
        this.receptor = receptor;
    }
}
