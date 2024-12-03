package com.tallerwebi.dominio;

import javax.persistence.*;
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
    private Integer valorEnvido = 0;

    public Carta() {

    }


    @Override
    public String toString() {
        return "Carta{" +
                "id=" + id +
                ", valor=" + valor +
                ", numero=" + numero +
                ", palo='" + palo +
                ", valorEnvido=" + valorEnvido +
                '}';
    }


    public Integer getValorEnvido() {
        return this.valorEnvido;
    }

    public void setValorEnvido(Integer valorEnvido) {
        this.valorEnvido = valorEnvido;
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
