package com.tallerwebi.dominio;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Carta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "jugador_id")  // Se asocia con la tabla Jugador
    private Jugador jugador;  // Relación muchos a uno con Jugador

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
                ", palo='" + palo + '\'' +
                ", img='" + img + '\'' +
                ", valorEnvido=" + valorEnvido +
                '}';
    }

    public Carta(Integer valor, Integer numero, String palo) {
        this.valor = valor;
        this.numero = numero;
        this.palo = palo;
        this.valorEnvido = 0;
    }

    public Carta(Integer valor, Integer numero, String palo, String img) {
        this.valor = valor;
        this.numero = numero;
        this.palo = palo;
        this.img = img;
        this.valorEnvido = 0;
    }

    private int calcularValorEnvido(int numero) {
        // Lógica para calcular el valorEnvido
        return (numero >= 10 && numero <= 12) ? 0 : numero; // O cualquier lógica que necesites
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
