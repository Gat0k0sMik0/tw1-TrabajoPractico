package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.*;
import com.tallerwebi.dominio.excepcion.TrucoException;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
public class ServicioManoImpl implements ServicioMano {

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
    Jugador leTocaTirar;

    private List<Carta> cartasJ1;
    private List<Carta> cartasJ2;

    @Autowired
    public ServicioManoImpl(
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
        this.diceEnvidoJ1 = null;
        this.diceEnvidoJ2 = null;
        this.diceRealEnvido = null;
        this.diceFaltaEnvido = null;
        this.leTocaTirar = null;
        this.puntosEnJuegoEnvido = 0;
        this.indicadorTruco = 0;
        this.puntosEnJuegoMano = 0;
    }

    @Override
    public Mano empezar(Partida t, Jugador j1, Jugador j2) {
        Mano m = new Mano();
        m.setEstaTerminada(false);
        m.setConfirmacionTerminada(false);
        m.setPartida(t);
        m.setCartasJ1(new ArrayList<>());
        m.setCartasJ2(new ArrayList<>());
        m.setCartasTiradasJ1(new ArrayList<>());
        m.setCartasTiradasJ2(new ArrayList<>());
        m.setGanador(null);
        this.asignarCartasJugadores(j1, j2, m);
        repositorioMano.guardar(m);
        return m;
    }

    @Override
    public Mano reset(Partida truco) {
        this.diceEnvidoJ1 = null;
        this.diceEnvidoJ2 = null;
        this.diceRealEnvido = null;
        this.diceFaltaEnvido = null;
        this.leTocaTirar = null;
        this.puntosEnJuegoEnvido = 0;
        this.indicadorTruco = 0;
        this.puntosEnJuegoMano = 0;

        Mano nueva = new Mano();
        nueva.setEstaTerminada(false);
        nueva.setConfirmacionTerminada(false);
        nueva.setPartida(truco);
        nueva.setCartasJ1(new ArrayList<>());
        nueva.setCartasJ2(new ArrayList<>());
        nueva.setCartasTiradasJ1(new ArrayList<>());
        nueva.setCartasTiradasJ2(new ArrayList<>());
        nueva.setGanador(null);

        this.asignarCartasJugadores(truco.getJ1(), truco.getJ2(), nueva);

        this.repositorioMano.guardar(nueva);
        return nueva;
    }

    @Override
    public Mano obtenerManoPorId(Long idPartida) {
        Mano m = repositorioMano.obtenerUltimaMano(idPartida);
        if (m == null) return null;
        Hibernate.initialize(m.getCartasJ1());
        Hibernate.initialize(m.getCartasJ2());
        Hibernate.initialize(m.getCartasTiradasJ1());
        Hibernate.initialize(m.getCartasTiradasJ2());
        return m;
    }

    @Override
    public void guardar(Mano mano) {
        this.repositorioMano.guardar(mano);
    }

    public void sacarCartaDeJugador(Jugador j, Carta c, Mano m) {
        if (j.getNumero().equals(1)) {
            if (m.getCartasJ1().contains(c)) {
                m.getCartasJ1().remove(c);
                m.getCartasTiradasJ1().add(c);
            } else throw new TrucoException("No existe esa carta en la mano del jugador");
        } else {
            if (m.getCartasJ2().contains(c)) {
                m.getCartasJ2().remove(c);
                m.getCartasTiradasJ2().add(c);
            } else throw new TrucoException("No existe esa carta en la mano del jugador");
        }
    }

    @Override
    public Ronda tirarCarta(Partida truco, Mano mano, Long idCarta, String nroJugador) {
        Carta cartaElegidaParaTirar = this.repositorioCarta.buscarCartaPorId(idCarta);
        Jugador jugador;

        if (truco.getJ1().getNumero().toString().equals(nroJugador)) {
            jugador = truco.getJ1();
            this.leTocaTirar = truco.getJ2();
        } else {
            jugador = truco.getJ2();
            this.leTocaTirar = truco.getJ1();
        }

        if (mano == null) throw new TrucoException("La mano es nula.");
        if (jugador == null) throw new TrucoException("El jugador es nulo.");
        if (cartaElegidaParaTirar == null) throw new TrucoException("La carta es nula.");


        List<Carta> cartasJugador = jugador.getNumero().equals(1)
                ? mano.getCartasJ1() : mano.getCartasJ2();

        if (!mano.getEstaTerminada()) {
            if (cartasJugador.contains(cartaElegidaParaTirar)) {
                sacarCartaDeJugador(jugador, cartaElegidaParaTirar, mano);
                sumarMovimientoMano(mano);
                Ronda r = new Ronda();
                r.setNombreJugador(jugador.getNombre());
                r.setPaloCarta(cartaElegidaParaTirar.getPalo());
                r.setValorCarta(cartaElegidaParaTirar.getValor());
                r.setNroCarta(cartaElegidaParaTirar.getNumero());
                r.setMano(mano);
                this.repositorioRonda.guardar(r);
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
    public Jugador saberQuienTiraAhora() {
        return this.leTocaTirar;
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
    public Jugador preguntar(Mano mano, String accion, Integer nroJugador) {
        Partida truco = mano.getPartida();
        Jugador ejecutor = nroJugador.equals(1) ? truco.getJ1() : truco.getJ2();
        Jugador receptor = nroJugador.equals(1) ? truco.getJ2() : truco.getJ1();
        System.out.println("Ejecuta: " + ejecutor.getNombre());
        System.out.println("Recibe: " + receptor.getNombre());
        String accionRealizada = saberAccion(accion);
        if (esTruco(accionRealizada)) {
            preguntarTruco(accionRealizada);
            mano.setRespondeAhora(receptor);
            this.repositorioMano.merge(mano);
            return receptor;
        } else if (esEnvido(accionRealizada)) {
            preguntarEnvido(accionRealizada, ejecutor);
            mano.setRespondeAhora(receptor);
            this.repositorioMano.merge(mano);
            return receptor;
        } else if (esFlor(accionRealizada)) {
            // TODO: calcular flor
            mano.setRespondeAhora(null);
            this.repositorioMano.merge(mano);
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
            this.repositorioTruco.merge(truco);
            this.repositorioMano.merge(mano);
            mano.setRespondeAhora(null);
            return null;
        } else {
            throw new TrucoException("Preguntar: ocurrió un error.");
        }
        // TODO: si la ronda no es la primera, no puede cantar envido
    }

    @Override
    public void determinarGanadorRonda(Partida truco, Mano mano) {
        if (truco == null) throw new TrucoException("La partida es nula.");
        if (truco.getJ1() == null) throw new TrucoException("El jugador 1 es nulo.");
        if (truco.getJ2() == null) throw new TrucoException("El jugador 2 es nulo.");

        obtenerGanadorDeRonda(mano, truco.getJ1(), truco.getJ2());

        if (mano.getEstaTerminada()) {
            System.out.println("Mano terminada: resultados ->");
            System.out.println("PuntosRondaJ1: " + mano.getPuntosRondaJ1());
            System.out.println("PuntosRondaJ2: " + mano.getPuntosRondaJ2());
            if (mano.getPuntosRondaJ1() > mano.getPuntosRondaJ2()) {
                truco.setPuntosJ1(truco.getPuntosJ1() + 1);
                mano.setGanador(truco.getJ1());
                System.out.println("J1 GANASTE AMIGO!");
            } else {
                mano.setGanador(truco.getJ2());
                truco.setPuntosJ2(truco.getPuntosJ2() + 1);
                System.out.println("J2 GANASTE AMIGO!");
            }
        }

        System.out.println(truco);

        this.repositorioTruco.merge(truco);
        this.repositorioMano.merge(mano);
    }

    private void obtenerGanadorDeRonda(Mano mano, Jugador jugador1, Jugador jugador2) {
        // Si ya tiraron los 2
        if (mano.getCartasTiradasJ1().size() == mano.getCartasTiradasJ2().size()) {

            // Verificar que las listas no estén vacías y que los índices sean válidos
            if (mano.getCartasJ1().isEmpty() || mano.getCartasJ2().isEmpty()) {
                mano.setEstaTerminada(true);
                System.out.println("Cambie la mano a terminada porque ya no tienen cartas");
            }

            // Conseguimos las últimas tiradas.
            Carta cartaJ1 = mano.getCartasTiradasJ1().get(mano.getCartasTiradasJ2().size() - 1);
            Carta cartaJ2 = mano.getCartasTiradasJ2().get(mano.getCartasTiradasJ1().size() - 1);

            // Comparamos quien tiró la más alta, en base a eso damos poder
            if (cartaJ1.getValor() > cartaJ2.getValor()) {
                mano.setPuntosRondaJ1(mano.getPuntosRondaJ1() + 1);
                this.leTocaTirar = jugador1;
            } else if (cartaJ2.getValor() > cartaJ1.getValor()) {
                mano.setPuntosRondaJ2(mano.getPuntosRondaJ2() + 1);
                this.leTocaTirar = jugador2;
            } else {
                System.out.println("obtenerGanadorDeRonda: NO SUME A NADIE");
            }
        }
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
    public Jugador responder(Mano mano, String accion, String respuesta, Integer nroJugador) {
        Partida truco = mano.getPartida();
        Jugador ejecutor = nroJugador.equals(1) ? truco.getJ1() : truco.getJ2();
        Jugador receptor = nroJugador.equals(1) ? truco.getJ2() : truco.getJ1();
        String accionQueResponde = saberAccion(accion);
        String respuestaDeLaAccion = saberAccion(respuesta);
        Jugador respondeAhora;
        System.out.println("Ejecuta " + ejecutor.getNombre() + " y dice " + respuesta + " a la accion de " + accionQueResponde + " que preguntó " + receptor.getNombre());
        if (esTruco(accionQueResponde)) {
            respondeAhora = manejarRespuestaTruco(truco, mano, respuestaDeLaAccion, ejecutor, receptor);
        } else if (esEnvido(accionQueResponde)) {
            respondeAhora = manejarRespuestaEnvido(truco, mano, respuestaDeLaAccion, ejecutor, receptor);
        } else {
            throw new TrucoException("Responder: ocurrió un error");
        }
        mano.setRespondeAhora(respondeAhora);
        this.repositorioTruco.merge(truco);
        this.repositorioMano.merge(mano);
        return respondeAhora;
    }


    private Jugador manejarRespuestaEnvido(Partida truco, Mano mano, String respuestaDeLaAccion,
                                           Jugador ejecutor, Jugador receptor) {
        // Saber quien es el j1 y j2
        Jugador j1 = ejecutor.getNumero().equals(1) ? ejecutor : receptor;
        Jugador j2 = receptor.getNumero().equals(2) ? receptor : ejecutor;
        System.out.println("manejarRespuestaEnvido: j1 es " + j1.getNombre());
        System.out.println("manejarRespuestaEnvido: j2 es " + j2.getNombre());

        if (respuestaDeLaAccion.equals("QUIERO") || respuestaDeLaAccion.equals("NO QUIERO")) {
            // RESPUESTAS DIRECTAS
            if (respuestaDeLaAccion.equals("QUIERO")) {
                // ACEPTA Y CALCULAMOS TANTOS
                Integer tantosJ1 = this.calcularTantosDeCartasDeUnJugador(mano.getCartasJ1());
                Integer tantosJ2 = this.calcularTantosDeCartasDeUnJugador(mano.getCartasJ2());
                System.out.println("manejarRespuestaEnvido: Envido J1 " + tantosJ1);
                System.out.println("manejarRespuestaEnvido: Envido J2 " + tantosJ2);
                if (this.diceFaltaEnvido != null) {
                    // falta envido (anula todos los anteriores)
                    Integer puntosParaGanar = truco.getPuntosParaGanar();
                    Integer puntosJ1 = truco.getPuntosJ1();
                    Integer puntosJ2 = truco.getPuntosJ2();
                    Integer puntosParaElGanador;
                    if (tantosJ1 > tantosJ2) {
                        puntosParaElGanador = puntosParaGanar - puntosJ2;
                        truco.setPuntosJ1(truco.getPuntosJ2() + puntosParaElGanador);
                    } else if (tantosJ1 < tantosJ2) {
                        puntosParaElGanador = puntosParaGanar - puntosJ1;
                        truco.setPuntosJ2(truco.getPuntosJ2() + puntosParaElGanador);
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
                    } else if (tantosJ1 < tantosJ2) {
                        truco.setPuntosJ2(truco.getPuntosJ2() + this.puntosEnJuegoEnvido);
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

    private Jugador manejarRespuestaTruco(Partida truco, Mano mano, String respuestaDeLaAccion, Jugador ejecutor, Jugador receptor) {
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

    private Integer calcularTantosDeCartasDeUnJugador(List<Carta> cartas) {
        for (Carta c : cartas) {
            if (c.getNumero() >= 10 && c.getNumero() <= 12) {
                c.setValorEnvido(0);
            } else {
                c.setValorEnvido(c.getNumero());
            }
        }

        return this.calcularEnvido(cartas);
    }

    public Integer calcularEnvido(List<Carta> cartas) {
        List<Carta> tieneDosDelMismoPalo = this.tieneDosDelMismoPalo(cartas);
        if (tieneDosDelMismoPalo.isEmpty()) {
            return 0;
        } else {
            System.out.println("Encontré dos o más con el mismo palo");
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

        System.out.println("Sumé " + envidoMax);

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
