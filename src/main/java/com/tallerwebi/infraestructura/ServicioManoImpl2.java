package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.*;
import com.tallerwebi.dominio.excepcion.TrucoException;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

@Service
@Transactional
public class ServicioManoImpl2 implements ServicioMano2 {

    RepositorioMano repositorioMano;
    RepositorioRonda2 repositorioRonda;
    RepositorioTruco repositorioTruco;
    RepositorioCarta repositorioCarta;

    private Integer puntosEnJuegoEnvido;
    private Integer indicadorTruco; // 1 -> truco, 2 -> retruco, 3 -> vale 4
    private Integer puntosEnJuegoMano;

    Jugador diceEnvidoJ1;
    Jugador diceEnvidoJ2;
    Jugador diceRealEnvido;
    Jugador diceFaltaEnvido;

    private List<Carta> cartasJ1;
    private List<Carta> cartasJ2;
    private List<Carta> cartasTiradasJ1;
    private List<Carta> cartasTiradasJ2;

    @Autowired
    public ServicioManoImpl2(
            RepositorioMano repositorioMano,
            RepositorioRonda2 repositorioRonda,
            RepositorioTruco repositorioTruco,
            RepositorioCarta repositorioCarta) {
        this.repositorioMano = repositorioMano;
        this.repositorioRonda = repositorioRonda;
        this.repositorioTruco = repositorioTruco;
        this.repositorioCarta = repositorioCarta;
        this.cartasJ1 = new ArrayList<>();
        this.cartasJ2 = new ArrayList<>();
        this.cartasTiradasJ1 = new ArrayList<>();
        this.cartasTiradasJ2 = new ArrayList<>();
        this.diceEnvidoJ1 = null;
        this.diceEnvidoJ2 = null;
        this.diceRealEnvido = null;
        this.diceFaltaEnvido = null;
        this.puntosEnJuegoEnvido = 0;
        this.indicadorTruco = 0;
        this.puntosEnJuegoMano = 0;
    }

    @Override
    public Mano empezar(Truco2 t, Jugador j1, Jugador j2) {
        Mano m = new Mano();
        m.setEstaTerminada(false);
        m.setPartida(t);
        m.setCartasJ1(new ArrayList<>());
        m.setCartasJ2(new ArrayList<>());
//        m.setCartasTiradasJ1(new ArrayList<>());
//        m.setCartasTiradasJ2(new ArrayList<>());
        this.asignarCartasJugadores(j1, j2, m);
        repositorioMano.guardar(m);
        return m;
    }


    @Transactional
    @Override
    public Mano obtenerManoPorId(Long id) {
        Mano m = repositorioMano.obtenerManoPorId(id);
        Hibernate.initialize(m.getCartasJ1());
        Hibernate.initialize(m.getCartasJ2());
        this.cartasJ1 = m.getCartasJ1();
        this.cartasJ2 = m.getCartasJ2();
        return m;
    }

    private void sacarCartaDeJugador(Jugador j, Carta c, Mano m) {
        if (j.getNumero().equals(1)) {
            if (this.cartasJ1.contains(c)) {
                this.cartasJ1.remove(c);
                this.cartasTiradasJ1.add(c);
//                m.setCartasTiradasJ1(this.cartasTiradasJ1);
                m.setCartasJ1(this.cartasJ1);
            } else throw new TrucoException("No existe esa carta en la mano del jugador");
        } else {
            if (this.cartasJ2.contains(c)) {
                this.cartasJ2.remove(c);
                this.cartasTiradasJ2.add(c);
//                m.setCartasTiradasJ2(this.cartasTiradasJ2);
                m.setCartasJ2(this.cartasJ2);
            } else throw new TrucoException("No existe esa carta en la mano del jugador");
        }
    }

    @Override
    public Ronda tirarCarta(Mano mano, Jugador jugador, Long idCarta, String nroJugador) {
        Carta cartaElegidaParaTirar = this.repositorioCarta.buscarCartaPorId(idCarta);
        if (mano == null) {
            throw new TrucoException("La mano es nula.");
        }
        if (jugador == null) {
            throw new TrucoException("El jugador es nulo.");
        }
        if (cartaElegidaParaTirar == null) {
            throw new TrucoException("La carta es nula.");
        }

        Jugador j = null;
        List<Carta> cartasJugador = new ArrayList<>();
        if (jugador.getNumero().equals(1)) {
            cartasJugador = mano.getCartasJ1();
        } else {
            cartasJugador = mano.getCartasJ2();
        }

        if (!mano.getEstaTerminada()) {
            if (cartasJugador.contains(cartaElegidaParaTirar)) {
                sacarCartaDeJugador(jugador, cartaElegidaParaTirar, mano);
                sumarMovimientoMano(mano);
                Ronda r = new Ronda();
                r.setNombreJugador(jugador.getNombre());
                r.setPaloCarta(cartaElegidaParaTirar.getPalo());
                r.setValorCarta(cartaElegidaParaTirar.getValor());
                r.setNroCarta(cartaElegidaParaTirar.getNumero());
                this.repositorioMano.guardar(mano);
                return r;
            } else {
                throw new TrucoException("La carta seleccionada no está en la mano del jugador.");
            }
        } else {
            throw new TrucoException("Estás tirando estando la mano terminada.");
        }
    }

    private void sumarMovimientoMano(Mano mano) {
        if (mano == null) {
            throw new TrucoException("La mano es nula.");
        }

        // Verificar que los movimientos no sean nulos
        if (mano.getMovimientos() == null) {
            throw new TrucoException("Los movimientos de la mano son nulos.");
        }

        mano.setMovimientos(mano.getMovimientos() + 1);
        repositorioMano.guardar(mano);

        // Log para verificar que la mano se guardó correctamente
        System.out.println("Movimiento sumado a la mano con ID: " + mano.getId() + ", movimientos: " + mano.getMovimientos());
    }


    private void asignarCartasJugadores(Jugador j1, Jugador j2, Mano m) {
        List<Carta> cartas = repositorioCarta.obtenerCartas();
        List<Carta> seisCartasRandom = obtenerSeisCartasRandom(cartas);
        asignarCartasJugador(j1, seisCartasRandom, m);
        asignarCartasJugador(j2, seisCartasRandom, m);
    }

    private void asignarCartasJugador(Jugador j, List<Carta> seisCartasRandom, Mano m) {
        if (j.getNumero().equals(1)) {
            Iterator<Carta> iterator = seisCartasRandom.iterator();
            while (iterator.hasNext() && this.cartasJ1.size() < 3) {
                Carta carta = iterator.next();
                this.cartasJ1.add(carta);
                iterator.remove();
            }
            m.setCartasJ1(this.cartasJ1);
            System.out.println("Le asigné " + m.getCartasJ1().size() + " a J1");
        } else {
            Iterator<Carta> iterator = seisCartasRandom.iterator();
            while (iterator.hasNext() && this.cartasJ2.size() < 3) {
                Carta carta = iterator.next();
                this.cartasJ2.add(carta);
                iterator.remove();
            }
            m.setCartasJ2(this.cartasJ2);
            System.out.println("Le asigné " + m.getCartasJ2().size() + " a J2");
        }
    }

    private List<Carta> obtenerSeisCartasRandom(List<Carta> cartas) {
        System.out.println("Largo de cartas: " + cartas.size());
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

    private Boolean saberSiLaCartaSeRepiteEnMazo(List<Carta> cartas, Integer numero, String palo) {
        for (Carta carta : cartas) {
            if (carta.getNumero().equals(numero) && carta.getPalo().equalsIgnoreCase(palo)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Mano reset(Truco2 truco) {
        this.diceEnvidoJ1 = null;
        this.diceEnvidoJ2 = null;
        this.diceRealEnvido = null;
        this.diceFaltaEnvido = null;
        this.puntosEnJuegoEnvido = 0;
        this.indicadorTruco = 0;
        this.puntosEnJuegoMano = 0;
        Mano nueva = new Mano();
        nueva.setEstaTerminada(false);
        nueva.setPartida(truco);
        this.repositorioMano.guardar(nueva);
        return nueva;
    }


    @Override
    public Integer obtenerPuntosEnJuegoPorTruco() {
        return this.puntosEnJuegoMano;
    }

    @Override
    public Integer obtenerPuntosEnJuegoDelEnvido() {
        return this.puntosEnJuegoEnvido;
    }

    @Override
    public Integer obtenerMovimientosDeLaMano(Mano mano) {
        return mano.getMovimientos();
    }

    @Override
    public Jugador preguntar(Mano mano, String accion, Jugador ejecutor, Jugador receptor) {
        Truco2 truco = mano.getPartida();
        String accionRealizada = saberAccion(accion);
        if (esTruco(accionRealizada)) {
            preguntarTruco(accionRealizada);
            return receptor;
        } else if (esEnvido(accionRealizada)) {
            preguntarEnvido(accionRealizada, ejecutor);
            return receptor;
        } else if (esFlor(accionRealizada)) {
            // TODO: calcular flor
            return null;
        } else if (accionRealizada.equals("MAZO")) {
            List<Ronda> rondasMano = this.repositorioRonda.obtenerRondasDeUnaMano(mano.getId());
            Integer puntosEnJuego = 1;
            if (rondasMano.isEmpty()) {
                puntosEnJuego = 2;
            }
            if (ejecutor.getNumero().equals(1)) {
                truco.setPuntosJ2(truco.getPuntosJ2() + puntosEnJuego);
            } else {
                truco.setPuntosJ1(truco.getPuntosJ1() + puntosEnJuego);
            }
            mano.setEstaTerminada(true);
            this.repositorioTruco.guardarPartida(truco);
            this.repositorioMano.guardar(mano);
            return null;
        } else {
            throw new TrucoException("Preguntar: ocurrió un error.");
        }

        // TODO: si la ronda no es la primera, no puede cantar envido
    }

    private void preguntarEnvido(String accionEncontrada, Jugador ejecutor) {
        switch (accionEncontrada) {
            case "ENVIDO":
                if (ejecutor.getNumero().equals(1)) {
                    this.diceEnvidoJ1 = ejecutor;
                } else {
                    this.diceEnvidoJ2 = ejecutor;
                }
                puntosEnJuegoEnvido += 2;
                break;
            case "REAL ENVIDO":
                diceRealEnvido = ejecutor;
                puntosEnJuegoEnvido += 3;
                break;
            case "FALTA ENVIDO":
                diceFaltaEnvido = ejecutor;
                break;
            default:
                throw new TrucoException("PreguntarEnvido: ocurrió un error.");
        }
    }

    private void preguntarTruco(String accionEncontrada) {
        if (accionEncontrada.equals("TRUCO")) {
            this.indicadorTruco++;
        } else {
            throw new TrucoException("PreguntarTruco: ocurrió un error");
        }
    }

    @Override
    public Jugador responder(Truco2 truco, String accion, String respuesta, Jugador ejecutor, Jugador receptor) {
        String accionQueResponde = saberAccion(accion);
        String respuestaDeLaAccion = saberAccion(respuesta);
        Jugador respondeAhora;
        if (esTruco(accionQueResponde)) {
            respondeAhora = manejarRespuestaTruco(truco, respuestaDeLaAccion, ejecutor, receptor);
        } else if (esEnvido(accionQueResponde)) {
            respondeAhora = manejarRespuestaEnvido(truco, respuestaDeLaAccion, ejecutor, receptor);
        } else {
            throw new TrucoException("Responder: ocurrió un error");
        }
        return respondeAhora;
    }

    private Jugador manejarRespuestaEnvido(Truco2 truco, String respuestaDeLaAccion,
                                           Jugador ejecutor, Jugador receptor) {
        // Saber quien es el j1 y j2
        Jugador j1 = ejecutor.getNumero().equals(1) ? ejecutor : receptor;
        Jugador j2 = receptor.getNumero().equals(2) ? receptor : ejecutor;

        if (respuestaDeLaAccion.equals("QUIERO") || respuestaDeLaAccion.equals("NO QUIERO")) {
            // RESPUESTAS DIRECTAS
            if (respuestaDeLaAccion.equals("QUIERO")) {
                // ACEPTA Y CALCULAMOS TANTOS
                Integer tantosJ1 = this.calcularTantosDeCartasDeUnJugador(j1);
                Integer tantosJ2 = this.calcularTantosDeCartasDeUnJugador(j2);
                if (this.diceFaltaEnvido != null) {
                    // falta envido (anula todos los anteriores)
                    Integer puntosParaGanar = truco.getPuntosParaGanar();
                    Integer puntosJ1 = truco.getPuntosJ1();
                    Integer puntosJ2 = truco.getPuntosJ2();
                    Integer puntosParaElGanador = 0;
                    if (tantosJ1 > tantosJ2) {
                        puntosParaElGanador = puntosParaGanar - puntosJ2;
                        truco.setPuntosJ1(truco.getPuntosJ2() + puntosParaElGanador);
                        System.out.println("Asigno puntos a J1");
                    } else if (tantosJ1 < tantosJ2) {
                        puntosParaElGanador = puntosParaGanar - puntosJ1;
                        truco.setPuntosJ2(truco.getPuntosJ2() + puntosParaElGanador);
                        System.out.println("Asigno puntos a J2");
                    } else {
                        System.out.println("TJ1: " + tantosJ1);
                        System.out.println("TJ2: " + tantosJ2);
                        System.out.println("Entre donde no debía");
                        // TODO: manejar solucion cuando tienen los mismos tantos
                    }
                    return null;
                } else {
                    // envidos
                    if (tantosJ1 > tantosJ2) {
                        truco.setPuntosJ1(truco.getPuntosJ2() + this.puntosEnJuegoEnvido);
                        j1.setPuntosPartida(truco.getPuntosJ1());
                    } else if (tantosJ1 < tantosJ2) {
                        truco.setPuntosJ2(truco.getPuntosJ2() + this.puntosEnJuegoEnvido);
                        j2.setPuntosPartida(truco.getPuntosJ2());
                    } else {
                        // TODO: manejar solucion cuando tienen los mismos tantos
                    }
                    return null;
                }
            } else {
                // NO QUIERE
                return null;
            }
        } else if (respuestaDeLaAccion.equals("ENVIDO")) {
            if (ejecutor.getNumero().equals(j1.getNumero())) {
                this.diceEnvidoJ1 = ejecutor;
            } else {
                this.diceEnvidoJ2 = receptor;
            }
            this.puntosEnJuegoEnvido += 2;
            return receptor;
        } else if (respuestaDeLaAccion.equals("REAL ENVIDO")) {
            this.diceRealEnvido = ejecutor;
            this.puntosEnJuegoEnvido += 3;
            return receptor;
        } else if (respuestaDeLaAccion.equals("FALTA ENVIDO")) {
            this.diceFaltaEnvido = ejecutor;
            return receptor;
        } else {
            throw new TrucoException("ManejarEnvido2: ocurrió un error.");
        }
    }

    private Jugador manejarRespuestaTruco(Truco2 truco, String respuestaDeLaAccion, Jugador ejecutor, Jugador receptor) {
        if (respuestaDeLaAccion.equals("QUIERO")) {
            if (indicadorTruco.equals(1)) {
                this.puntosEnJuegoMano = 2;
            }
           return null;
        } else if (respuestaDeLaAccion.equals("NO QUIERO")) {
            if (ejecutor.getNumero().equals(1)) {
                truco.setPuntosJ1(truco.getPuntosJ1() + 1);
            } else {
                truco.setPuntosJ2(truco.getPuntosJ2() + 1);
            }
            return null;
        } else {
            if (this.indicadorTruco.equals(1) && respuestaDeLaAccion.equals("RE TRUCO")) {
                this.indicadorTruco++;
                this.puntosEnJuegoMano = 3;
                return receptor;
            } else if (this.indicadorTruco.equals(2) && respuestaDeLaAccion.equals("VALE 4")) {
                this.indicadorTruco++;
                this.puntosEnJuegoMano = 4;
                return receptor;
            } else {
                throw new TrucoException("ManejarRespuestaTruco: ocurrió un error.");
            }
        }
    }

    private Integer calcularTantosDeCartasDeUnJugador(Jugador jugador) {
        List<Carta> cartasJugador = null;
        if (jugador.getNumero().equals(1)) {
            cartasJugador = this.cartasJ1;
        } else {
            cartasJugador = this.cartasJ2;
        }

        for (Carta c : cartasJugador) {
            if (c.getNumero() >= 10 && c.getNumero() <= 12) {
                c.setValorEnvido(0);
            } else {
                c.setValorEnvido(c.getNumero());
            }
        }
        return this.calcularEnvido(cartasJugador);
    }

    public Integer calcularEnvido(List<Carta> cartas) {
        List<Carta> tieneDosDelMismoPalo = this.tieneDosDelMismoPalo(cartas);
        if (tieneDosDelMismoPalo.isEmpty()) {
            return 0;
        } else {
            return this.obtenerSumaDeLasMasAltas(tieneDosDelMismoPalo);
        }
    }

    private Integer obtenerSumaDeLasMasAltas(List<Carta> cartas) {
        int envidoMax = 0;

        // Agrupa las cartas por palo
        for (int i = 0; i < cartas.size(); i++) {
            for (int j = i + 1; j < cartas.size(); j++) {
                if (cartas.get(i).getPalo().equals(cartas.get(j).getPalo())) {
                    // Si dos cartas tienen el mismo palo, suma sus valores y añade 20
                    int envido = cartas.get(i).getValorEnvido() + cartas.get(j).getValorEnvido() + 20;
                    envidoMax = Math.max(envidoMax, envido);
                }
            }
        }

        // Si no hay dos cartas del mismo palo, el envido es el valor más alto de la mano
        if (envidoMax == 0) {
            for (Carta carta : cartas) {
                envidoMax = Math.max(envidoMax, carta.getValorEnvido());
            }
        }

        return envidoMax;
    }

    private List<Carta> tieneDosDelMismoPalo(List<Carta> cartas) {
        int contador = 0;
        List<Carta> delMismoPalo = new ArrayList<>();
        for (Carta carta : cartas) {
            for (Carta carta2 : cartas) {
                if (carta.getPalo().equals(carta2.getPalo())) {
                    delMismoPalo.add(carta2);
                }
            }
            if (delMismoPalo.size() >= 2) {
                contador = 1;
                break;
            }
        }
        if (contador == 1) {
            return delMismoPalo;
        }
        return new ArrayList<>();
    }

    private Boolean esEnvido(String accion) {
        return accion.equals("ENVIDO") || accion.equals("REAL ENVIDO") || accion.equals("FALTA ENVIDO");
    }

    private Boolean esTruco(String accion) {
        return accion.equals("TRUCO") || accion.equals("RE TRUCO") || accion.equals("VALE 4");
    }

    private Boolean esFlor(String accion) {
        return accion.equals("FLOR") || accion.equals("CONTRAFLOR AL RESTO") || accion.equals("CONTRAFLOR");
    }

    private String saberAccion(String numberValue) {
        String respuestaDada = "";
        switch (Integer.parseInt(numberValue)) {
            case 0:
                respuestaDada = "NO QUIERO";
                break;
            case 1:
                respuestaDada = "QUIERO";
                break;
            case 2:
                respuestaDada = "ENVIDO";
                break;
            case 3:
                respuestaDada = "REAL ENVIDO";
                break;
            case 4:
                respuestaDada = "FALTA ENVIDO";
                break;
            case 5:
                respuestaDada = "TRUCO";
                break;
            case 6:
                respuestaDada = "RE TRUCO";
                break;
            case 7:
                respuestaDada = "VALE 4";
                break;
            case 8:
                respuestaDada = "FLOR";
                break;
            case 9:
                respuestaDada = "MAZO";
                break;
            default:
                return "";
        }
        return respuestaDada;
    }

}
