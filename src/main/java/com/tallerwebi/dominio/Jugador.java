package com.tallerwebi.dominio;

import javax.persistence.*;

@Entity
public class Jugador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private Integer numero;
    private Integer victorias;
    private String nivel;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    public Jugador() {
        this.victorias = 0;
        this.nivel = "Bronce";
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public Integer getVictorias() {
        return victorias;
    }

    public void setVictorias(Integer victorias) {
        this.victorias = victorias;
    }

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    @Override
    public String toString() {
        return "Jugador{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", numero=" + numero +
                ", victorias=" + victorias +
                ", nivel='" + nivel + '\'' +
                ", usuario=" + usuario +
                '}';
    }
}
    //
//    public List<Carta> getCartasTiradas() {
//        return cartasTiradas;
//    }
//
//    public void setCartasTiradas(List<Carta> cartasTiradas) {
//        this.cartasTiradas = cartasTiradas;
