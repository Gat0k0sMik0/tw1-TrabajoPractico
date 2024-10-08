package com.tallerwebi.dominio;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Carta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer valor;
    private Integer numero;
    private String palo;
    private String img;

    public Carta() {

    }

    public Carta(Integer valor, Integer numero, String palo) {
        this.valor = valor;
        this.numero = numero;
        this.palo = palo;
    }

    public Carta(Integer valor, Integer numero, String palo, String img) {
        this.valor = valor;
        this.numero = numero;
        this.palo = palo;
        this.img = img;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getValor() {
        return valor;
    }

    public void setValor(Integer valor) {
        this.valor = valor;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public String getPalo() {
        return palo;
    }

    public void setPalo(String palo) {
        this.palo = palo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Carta carta = (Carta) o;
        return Objects.equals(valor, carta.valor) && Objects.equals(numero, carta.numero) && Objects.equals(palo, carta.palo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(valor, numero, palo);
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
