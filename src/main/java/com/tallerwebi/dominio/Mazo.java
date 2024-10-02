package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.IndiceFueraDeRangoException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Mazo {
    private List<Carta> cartas;

    public Mazo() {
        this.cartas = new ArrayList<>();
        Carta c1 = new Carta(13, 1, "Espadas");
        Carta c2 = new Carta(8, 2, "Espadas");
        Carta c3 = new Carta(9, 3, "Espadas");
        Carta c4 = new Carta(0, 4, "Espadas");
        Carta c5 = new Carta(1, 5, "Espadas");
        Carta c6 = new Carta(2, 6, "Espadas");
        Carta c7 = new Carta(11, 7, "Espadas");
        Carta c8 = new Carta(4, 10, "Espadas");
        Carta c9 = new Carta(5, 11, "Espadas");
        Carta c10 = new Carta(6, 12, "Espadas");

        Carta c11 = new Carta(7, 1, "Oros");
        Carta c12 = new Carta(8, 2, "Oros");
        Carta c13 = new Carta(9, 3, "Oros");
        Carta c14 = new Carta(0, 4, "Oros");
        Carta c15 = new Carta(1, 5, "Oros");
        Carta c16 = new Carta(2, 6, "Oros");
        Carta c17 = new Carta(10, 7, "Oros");
        Carta c18 = new Carta(4, 10, "Oros");
        Carta c19 = new Carta(5, 11, "Oros");
        Carta c20 = new Carta(6, 12, "Oros");

        Carta c21 = new Carta(12, 1, "Bastos");
        Carta c22 = new Carta(8, 2, "Bastos");
        Carta c23 = new Carta(9, 3, "Bastos");
        Carta c24 = new Carta(0, 4, "Bastos");
        Carta c25 = new Carta(1, 5, "Bastos");
        Carta c26 = new Carta(2, 6, "Bastos");
        Carta c27 = new Carta(3, 7, "Bastos");
        Carta c28 = new Carta(4, 10, "Bastos");
        Carta c29 = new Carta(5, 11, "Bastos");
        Carta c30 = new Carta(6, 12, "Bastos");

        Carta c31 = new Carta(7, 1, "Copas");
        Carta c32 = new Carta(8, 2, "Copas");
        Carta c33 = new Carta(9, 3, "Copas");
        Carta c34 = new Carta(0, 4, "Copas");
        Carta c35 = new Carta(1, 5, "Copas");
        Carta c36 = new Carta(2, 6, "Copas");
        Carta c37 = new Carta(3, 7, "Copas");
        Carta c38 = new Carta(4, 10, "Copas");
        Carta c39 = new Carta(5, 11, "Copas");
        Carta c40 = new Carta(6, 12, "Copas");

        Carta[] cartas = {c1, c2, c3, c4, c5, c6, c7, c8, c9, c10, c11, c12, c13, c14, c15, c16, c17, c18, c19, c20,
                c21, c22, c23, c24, c25, c26, c27, c28, c29, c30, c31, c32, c33, c34, c35, c36, c37, c38, c39, c40};

        for (Carta carta : cartas) {
            cargar(carta);
        }
    }

    public void cargar(Carta carta) {
        cartas.add(carta);
    }

    public Integer getCantidadDeCartas() {
        return cartas.size();
    }

    public Carta getCartaRandom () {
        return cartas.get(new Random().nextInt(cartas.size()));
    }

    public Carta getCartaPorIndice (Integer indice) throws IndiceFueraDeRangoException {
        if (indice < 0 || indice > cartas.size()) {
            throw new IndiceFueraDeRangoException();
        }
        return cartas.get(indice);
    }

    public List<Carta> getSeisCartasAleatoriasSinRepetir() {
        List<Carta> cartasBuscadas = new ArrayList<>();
        int indice = 0;
        int random = 0;
        Random r = new Random();
        while (indice < 6) {
            random = r.nextInt(cartas.size());
            Carta cartaRandom = cartas.get(random);
            if (cartasBuscadas.isEmpty()) {
                cartasBuscadas.add(cartaRandom);
                indice++;
            } else {
                if (!saberSiLaCartaSeRepiteEnMazo(cartasBuscadas, cartaRandom.getNumero(), cartaRandom.getPalo())) {
                    cartasBuscadas.add(cartaRandom);
                    indice++;
                }
            }

        }
        return cartasBuscadas;
    }

    public Boolean saberSiLaCartaSeRepiteEnMazo(List<Carta> cartas, Integer numero, String palo) {
        for (Carta carta : cartas) {
            if (carta.getNumero().equals(numero) && carta.getPalo().equalsIgnoreCase(palo)) {
                return true;
            }
        }
        return false;
    }

    public void asignarCartasAJugadores(Jugador j1, Jugador j2, List<Carta> seis) {
        Carta c1 = seis.get(0);
        Carta c2 = seis.get(1);
        Carta c3 = seis.get(2);
        List<Carta> mazo1 = new ArrayList<>();
        mazo1.add(c1);
        mazo1.add(c2);
        mazo1.add(c3);

        Carta c4 = seis.get(3);
        Carta c5 = seis.get(4);
        Carta c6 = seis.get(5);
        List<Carta> mazo2 = new ArrayList<>();
        mazo2.add(c4);
        mazo2.add(c5);
        mazo2.add(c6);

        j1.setCartas(mazo1);
        j2.setCartas(mazo2);
    }

    public List<Carta> getCartas() {
        return cartas;
    }

    public void setCartas(List<Carta> cartas) {
        this.cartas = cartas;
    }
}