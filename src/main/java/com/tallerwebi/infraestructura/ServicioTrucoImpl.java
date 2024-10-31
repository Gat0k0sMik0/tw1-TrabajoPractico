package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.*;
import com.tallerwebi.dominio.excepcion.TrucoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ServicioTrucoImpl implements ServicioTruco {

    private Truco truco;
    private RepositorioCarta repositorioCarta;
    private Jugador turno;
    private Jugador debeResponder;
    /*private Integer puntosJugador1;
    private Integer puntosJugador2;*/
    private Boolean trucoCantado;

    /*
    Class truco maneja todo
    Una mano tiene rondas
    La mano termina al haber 3 rondas (6 movimientos)
    Mano (id_jugador_ganador, puntos_ganados)



    */

    @Autowired
    public ServicioTrucoImpl(RepositorioCarta repositorioCarta) { // ServicioTurno turnos
        this.repositorioCarta = repositorioCarta;
    }

    @Override
    public void empezar(List<Jugador> jugadores) {
        this.truco = new Truco();
        this.truco.getMazo().setCartas(repositorioCarta.obtenerCartas());
        List<Carta> s = this.truco.getMazo().getCartas();
        for (Carta c : s) {
            if (c.getNumero() >= 10 && c.getNumero() <= 12) {
                c.setValorEnvido(0);
            } else {
                c.setValorEnvido(c.getNumero());
            }
        }
        this.truco.getMazo().setCartas(s);
        this.truco.asignarCartasJugadores(jugadores);
        this.truco.empezarMano(jugadores);
    }

    @Override
    public void empezar(List<Jugador> jugadores, List<Carta> cartas) {
        this.truco = new Truco();
        for (Carta c : cartas) {
            if (c.getNumero() >= 10 && c.getNumero() <= 12) {
                c.setValorEnvido(0);
            } else {
                c.setValorEnvido(c.getNumero());
            }
        }
        this.truco.asignarCartasJugadores(jugadores, cartas);
        this.truco.empezarMano(jugadores);
    }

    @Override
    public void accion (String accion, Jugador cantador, Jugador receptor) {
       this.truco.guardarAccion(cantador, accion, false);
    }

    @Override
    public Integer calcularTantosDeCartasDeUnJugador (Jugador jugador) {
        return this.truco.calcularEnvido(jugador.getCartas());
    }


    // Método para que el jugador tire una carta
    public void tirarCarta(Jugador jugador, Carta cartaSeleccionada) {
        if (!truco.getLaManoTerminada()) {
            // que va a tirar si no tiene más cartas ?
            List<Carta> cartasJugador = jugador.getCartas();
            if (cartasJugador.contains(cartaSeleccionada)) {
                jugador.tirarCarta(cartaSeleccionada); // Se le saca la carta de la mano al jugador
                truco.registrarJugada(jugador, cartaSeleccionada);
            } else {
                throw new TrucoException("La carta seleccionada no está en la mano del jugador.");
            }
        } else {
            // tiene cartas, entonces tira normal
            throw new TrucoException("No hay mas cartas rey.");
        }

        if (truco.yaNoTieneCartas()) {
            truco.terminarManoActual();
        }
    }

    @Override
    public List<Ronda> getRondasDeLaManoActual() {
        return this.truco.getRondasDeManoActual();
    }

    @Override
    public Integer getNumeroDeRondasJugadasDeLaManoActual() {
        return this.truco.getNumeroDeRondasJugadasDeLaManoActual();
    }

    public Integer getMovimientosDeLaManoActual() {
        return this.truco.getMovimientosDeLaManoActual();
    }

    @Override
    public Truco getTruco() {
        return this.truco;
    }

    @Override
    public List<Mano> getManosJugadas() {
        return this.truco.getManosDePartida();
    }

    @Override
    public Boolean saberSiLaManoEstaTerminada() {
        return this.truco.getLaManoTerminada();
    }

//    private void obtenerGanadorDeLaMano (Jugador jugador1, Jugador jugador2) {
//
//        int puntosJugador1 = 0;
//        int puntosJugador2 = 0;
//        Jugador ganador = null;
//
//        Jugador ganadorRonda0 = truco.saberQuienGanoNumeroDeRonda(0);
//        Jugador ganadorRonda1 = truco.saberQuienGanoNumeroDeRonda(1);
//        Jugador ganadorRonda2 = truco.saberQuienGanoNumeroDeRonda(2);
//
//        List <Jugador> ganadores = new ArrayList<>();
//        ganadores.add(ganadorRonda0);
//        ganadores.add(ganadorRonda1);
//        ganadores.add(ganadorRonda2);
//
//        for (Jugador jugador : ganadores) {
//            if (jugador.equals(jugador1)) puntosJugador1++;
//            if (jugador.equals(jugador2)) puntosJugador2++;
//        }
//
//        if (puntosJugador1 > puntosJugador2) {
//            ganador = jugador1;
//        } else {
//            ganador = jugador2;
//        }
//
//    }

    @Override
    public void cambiarTurno(Jugador jugador) {
        this.turno = jugador;
    }

    @Override
    public Boolean esTurnoJugador(String nombreJugador) {
        return (this.turno.getNombre().equals(nombreJugador));
    }

    @Override
    public Jugador getTurnoJugador() {
        return this.turno;
    }

    @Override
    public void determinarGanadorRonda(Jugador jugador1, Jugador jugador2) {
        // Si ya tiraron los 2
        if (jugador1.getCartasTiradas().size() == jugador2.getCartasTiradas().size()) {
            // Conseguimos las últimas tiradas.
            Carta cartaJ1 = jugador1.getCartasTiradas().get(jugador1.getCartasTiradas().size() - 1);
            Carta cartaJ2 = jugador2.getCartasTiradas().get(jugador2.getCartasTiradas().size() - 1);

            // Comparamos quien tiró la más alta, en base a eso damos poder
            if (cartaJ1.getValor() > cartaJ2.getValor()) {
                this.truco.sumarPuntoDeRonda(jugador1);
                cambiarTurno(jugador2);
            } else if (cartaJ2.getValor() > cartaJ1.getValor()) {
                cambiarTurno(jugador1);
                this.truco.sumarPuntoDeRonda(jugador2);
            }
        }
    }

    @Override
    public void validarCartas(List<Carta> cartasJugador) {
        if (cartasJugador.isEmpty()) throw new TrucoException("Las cartas del jugador no existen");
    }

    @Override
    public Boolean esLaPrimerRonda() {
        return truco.getRondasDeManoActual().size() <= 1;
    }

    @Override
    public List<Accion> getAcciones() {
        return this.truco.getAcciones();
    }

    @Override
    public void verificarEnvido(Jugador jugador1, Jugador jugador2) {
        Integer tantosJ1 = verLosTantos(jugador1);
        Integer tantosJ2 = verLosTantos(jugador2);

        if (tantosJ1 > tantosJ2) {
            //puntosJugador1++;
        }

        if (tantosJ2 > tantosJ1) {
            //puntosJugador2++;
        }

        if (tantosJ2.equals(tantosJ1)) {
            //Gana jugador actual
        }
    }

    private Integer verLosTantos(Jugador jugador) {
        List<Carta> cartasManoJugador = jugador.getCartas();
        List<Carta> cartasTiradasJugador = jugador.getCartasTiradas();
        List<Carta> todasLasCartas = new ArrayList<>();

        //Obtengo todas las cartas del jugador
        if (cartasManoJugador.size() == 2) {
            todasLasCartas.add(cartasManoJugador.get(0));
            todasLasCartas.add(cartasManoJugador.get(1));
            todasLasCartas.add(cartasTiradasJugador.get(0));
        } else {
            todasLasCartas.add(cartasManoJugador.get(0));
            todasLasCartas.add(cartasManoJugador.get(1));
            todasLasCartas.add(cartasManoJugador.get(2));
        }

        Integer tantos = 0;

        // For para recorrer una carta y su siguiente
        for (int i = 0; i < todasLasCartas.size(); i++) {
            for (int j = i + 1; j < todasLasCartas.size(); j++) {

                // Verifico si no es flor
                if (!todasLasCartas.get(0).getPalo().equals(todasLasCartas.get(1).getPalo()) &&
                        !todasLasCartas.get(1).getPalo().equals(todasLasCartas.get(2).getPalo())) {

                    // Si no tenemos ningun palo que se repita en las 3 cartas, obtengo el numero mayor
                    // que no sea ni 10, ni 11, ni 12
                    if (!todasLasCartas.get(i).getPalo().equals(todasLasCartas.get(j).getPalo())) {
                        tantos = obtenerPaloMayor(todasLasCartas);
                    }

                    //Ahora si busco las cartas que tengan el mismo palo y sumo los tantos
                    if (todasLasCartas.get(i).getPalo().equals(todasLasCartas.get(j).getPalo())) {
                        Integer valor1 = todasLasCartas.get(i).getNumero();
                        Integer valor2 = todasLasCartas.get(j).getNumero();
                        tantos = sumarTantos(valor1, valor2);
                        break;
                    }
                }
            }
        }

        return tantos;
    }

    private Integer obtenerPaloMayor(List<Carta> todasLasCartas) {
        Carta mayor = null;

        for (int i = 0; i < todasLasCartas.size(); i++) {
            if ((todasLasCartas.get(i).getNumero() != 10 && todasLasCartas.get(i).getNumero() != 11
                    && todasLasCartas.get(i).getNumero() != 12) &&
                    (mayor.equals(null) || todasLasCartas.get(i).getNumero() > mayor.getNumero())) {
                mayor = todasLasCartas.get(i);
            }
        }

        Integer resultado = (mayor != null) ? mayor.getNumero() : 0;
        return resultado;
    }

    private Integer sumarTantos(Integer valor1, Integer valor2) {
        Integer tantosIniciales = 20;
        Integer tantos = 0;

        if (!valor1.equals(10) || !valor1.equals(11) || !valor1.equals(12) || !valor2.equals(10) || !valor2.equals(11) || !valor2.equals(12)) {
            tantos = tantosIniciales + valor1 + valor2;
        }

        if (valor1.equals(10) || valor1.equals(11) || valor1.equals(12) && valor2.equals(10) || valor2.equals(11) || valor2.equals(12)) {
            tantos = tantosIniciales;
        }

        if (valor1.equals(10) || valor1.equals(11) || valor1.equals(12)) {
            tantos = tantosIniciales + valor2;
        }

        if (valor2.equals(10) || valor2.equals(11) || valor2.equals(12)) {
            tantos = tantosIniciales + valor1;
        }

        return tantos;
    }


    public List<Carta> getCartasJugadas(Jugador j) {
        return j.getCartasTiradas();
    }

    @Override
    public List<Ronda> getRondasJugadas() {
//        return truco.getRondasJugadas();
        return null;
    }

    @Override
    public Boolean cantarTruco() {
        truco.setTrucoCantado(true);
        trucoCantado = truco.getTrucoCantado();
        return trucoCantado;
    }

    /* --ENVIDO--
    public void verificarEnvido(Jugador jugador1, Jugador jugador2) {
        Integer tantosJ1 = verLosTantos(jugador1);
        Integer tantosJ2 = verLosTantos(jugador2);

        if (tantosJ1 > tantosJ2) {
            this.puntosJugador1 = puntosJugador1 + 2;
        }

        if (tantosJ2 > tantosJ1) {
            this.puntosJugador2 = puntosJugador2 + 2;
        }


    }

    private Integer verLosTantos(Jugador jugador) {
        List<Carta> cartasJugador = jugador.getCartas();
        Integer tantos = 0;
        boolean mismoPalo = false;

        for (int i = 0; i < cartasJugador.size(); i++) {
            for (int j = i + 1; j < cartasJugador.size(); j++) {
                if (cartasJugador.get(i).getPalo().equals(cartasJugador.get(j).getPalo())) {
                    mismoPalo = true;

                    Integer valor1 = cartasJugador.get(i).getNumero();
                    Integer valor2 = cartasJugador.get(j).getNumero();
                    tantos = sumarTantos(valor1, valor2);
                    break;
                }
            }
        }

        return tantos;
    }

    private Integer sumarTantos(Integer valor1, Integer valor2) {
        Integer tantosIniciales = 20;
        Integer tantos = 0;

        if (!valor1.equals(10) || !valor1.equals(11) || !valor1.equals(12) || !valor2.equals(10) || !valor2.equals(11) || !valor2.equals(12)){
            tantos = tantosIniciales + valor1 + valor2;
        }

        if (valor1.equals(10) || valor1.equals(11) || valor1.equals(12) && valor2.equals(10) || valor2.equals(11) || valor2.equals(12)){
            tantos = tantosIniciales;
        }

        if (valor1.equals(10) || valor1.equals(11) || valor1.equals(12)) {
            tantos = tantosIniciales + valor2;
        }

        if (valor2.equals(10) || valor2.equals(11) || valor2.equals(12)) {
            tantos = tantosIniciales + valor1;
        }

        return tantos;
    }*/


    public Jugador ganadorGeneral() {
        // TODO REVISAR
//        Integer puntosGanador = 30;
//        Jugador jugadorGanador = null;
//
//       for (Jugador jugador : jugadores) {
//           if (jugador.getPuntos().stream().mapToInt(Integer::intValue).sum() == puntosGanador) {
//               return jugador;
//           }
//       }
//        return jugadorGanador;
        return null;
    }

    private void contadorPuntos(Integer puntoTotal, Jugador jugador) {
        // TODO REVISAR
//        jugador.getPuntos().add(puntoTotal);
        ganadorGeneral();
    }

    private void comparadorDePuntos(Jugador jugador1, Jugador jugador2) {
        // TODO REVISAR
//        // totales del jugador 1
//        int puntosJugador1 = jugador1.getPuntos().stream().mapToInt(Integer::intValue).sum();
//
//        // del jugador 2
//        int puntosJugador2 = jugador2.getPuntos().stream().mapToInt(Integer::intValue).sum();
//
//        // los puntos totales
//        if (puntosJugador1 > puntosJugador2) {
//            System.out.println(jugador1.getNombre() + " le esta ganando a " + jugador2.getNombre());
//        } else if (puntosJugador2 > puntosJugador1) {
//            System.out.println(jugador2.getNombre() + " le esta ganando a " + jugador1.getNombre());
//        } else {
//            System.out.println(jugador1.getNombre() + " y " + jugador2.getNombre() + " estan empatados!!.");
//        }
    }

}



