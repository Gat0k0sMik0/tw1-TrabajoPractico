package com.tallerwebi.dominio;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;

@Entity
public class Mano {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "partida_id")
    private Truco2 partida;  // Relación con Partida (muchos a uno)

    private Boolean estaTerminada;
    private Integer movimientos = 0;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(FetchMode.SUBSELECT)
    @JoinTable(name = "mano_cartas_j1",
            joinColumns = @JoinColumn(name = "mano_id"),
            inverseJoinColumns = @JoinColumn(name = "carta_id"))
    // Clave foránea en la tabla `Carta`
    private List<Carta> cartasJ1;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(name = "mano_cartas_j2",
            joinColumns = @JoinColumn(name = "mano_id"),
            inverseJoinColumns = @JoinColumn(name = "carta_id"))
// Clave foránea en la tabla `Carta`
    private List<Carta> cartasJ2;

//    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Carta> cartasTiradasJ1;
//
//    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Carta> cartasTiradasJ2;

    public Mano() {

    }


//    public List<Carta> getCartasTiradasJ1() {
//        return cartasTiradasJ1;
//    }
//
//    public void setCartasTiradasJ1(List<Carta> cartasTiradasJ1) {
//        this.cartasTiradasJ1 = cartasTiradasJ1;
//    }
//
//    public List<Carta> getCartasTiradasJ2() {
//        return cartasTiradasJ2;
//    }
//
//    public void setCartasTiradasJ2(List<Carta> cartasTiradasJ2) {
//        this.cartasTiradasJ2 = cartasTiradasJ2;
//    }
//
//    public void agregarCartaTiradaJ1(Carta carta) {
//        cartasTiradasJ1.add(carta);
//    }
//
//    public void agregarCartaTiradaJ2(Carta carta) {
//        cartasTiradasJ2.add(carta);
//    }

    public void agregarCartaJ1(Carta carta) {
        cartasJ1.add(carta);
    }

    public void agregarCartaJ2(Carta carta) {
        cartasJ2.add(carta);
    }

    public List<Carta> getCartasJ1() {
        return cartasJ1;
    }

    public void setCartasJ1(List<Carta> cartasJ1) {
        this.cartasJ1 = cartasJ1;
    }

    public List<Carta> getCartasJ2() {
        return cartasJ2;
    }

    public void setCartasJ2(List<Carta> cartasJ2) {
        this.cartasJ2 = cartasJ2;
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

    @Override
    public String toString() {
        return "Mano2{" + "id=" + id + ", partida=" + partida + ", estaTerminada=" + estaTerminada + ", movimientos=" + movimientos + ", cartasJ1=" + cartasJ1 + ", cartasJ2=" + cartasJ2 + '}';
    }
}

