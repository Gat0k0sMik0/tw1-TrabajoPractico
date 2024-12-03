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
    RepositorioRonda repositorioRonda;
    RepositorioTruco repositorioTruco;
    RepositorioCarta repositorioCarta;

    private Integer puntosEnJuegoEnvido;
    private Integer puntosEnJuegoFlor;
    private Integer indicadorTruco; // 1 -> truco, 2 -> retruco, 3 -> vale 4
    private Integer puntosEnJuegoMano;

    private Jugador diceEnvidoJ1;
    private Jugador diceEnvidoJ2;
    private Jugador diceRealEnvido;
    private Jugador diceFaltaEnvido;
    private Jugador diceFlorJ1;
    private Jugador diceFlorJ2;
    private Jugador diceContraflor;
    private Jugador diceContraflorAlResto;
    private Jugador leTocaTirar;
    private Jugador ultimoGanadorMano;
    private Jugador empezoLaMano;

    @Autowired
    public ServicioManoImpl(
            RepositorioMano repositorioMano,
            RepositorioRonda repositorioRonda,
            RepositorioTruco repositorioTruco,
            RepositorioCarta repositorioCarta) {
        this.repositorioMano = repositorioMano;
        this.repositorioRonda = repositorioRonda;
        this.repositorioTruco = repositorioTruco;
        this.repositorioCarta = repositorioCarta;
        this.diceEnvidoJ1 = null;
        this.diceEnvidoJ2 = null;
        this.diceRealEnvido = null;
        this.diceFaltaEnvido = null;
        this.diceFlorJ1 = null;
        this.diceFlorJ2 = null;
        this.diceContraflor = null;
        this.diceContraflorAlResto = null;
        this.leTocaTirar = null;
        this.puntosEnJuegoEnvido = 0;
        this.puntosEnJuegoFlor = 0;
        this.indicadorTruco = 0;
        this.puntosEnJuegoMano = 0;
    }


    @Override
    public void empezar(Partida truco) {
        // Crear mano nueva y setear valores
        Mano m = new Mano();
        m.setEstaTerminada(false);
        m.setConfirmacionTerminada(false);
        m.setPartida(truco);
        m.setCartasJ1(new ArrayList<>());
        m.setCartasJ2(new ArrayList<>());
        m.setCartasTiradasJ1(new ArrayList<>());
        m.setCartasTiradasJ2(new ArrayList<>());
        m.setGanador(null);
        this.leTocaTirar = getRandom(truco);
        this.empezoLaMano = this.leTocaTirar;

        // Asignacion de cartas a los jugadores
        this.asignarCartasJugadores(truco.getJ1(), truco.getJ2(), m);

        System.out.println(m);

        // Guardamos  mano
        repositorioMano.guardar(m);
    }

    @Override
    public Mano reset(Partida truco) {
        // Reset de valores del servicio
        this.diceEnvidoJ1 = null;
        this.diceEnvidoJ2 = null;
        this.diceRealEnvido = null;
        this.diceFaltaEnvido = null;
        this.leTocaTirar = this.ultimoGanadorMano;
        this.puntosEnJuegoEnvido = 0;
        this.indicadorTruco = 0;
        this.puntosEnJuegoMano = 0;

        // Creamos nueva mano
        Mano nueva = new Mano();
        nueva.setEstaTerminada(false);
        nueva.setConfirmacionTerminada(false);
        nueva.setPartida(truco);
        nueva.setCartasJ1(new ArrayList<>());
        nueva.setCartasJ2(new ArrayList<>());
        nueva.setCartasTiradasJ1(new ArrayList<>());
        nueva.setCartasTiradasJ2(new ArrayList<>());
        nueva.setGanador(null);

        // Asignacion de cartas nuevas
        this.asignarCartasJugadores(truco.getJ1(), truco.getJ2(), nueva);

        // Guardamos nueva mano
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
    public void tirarCarta(Partida truco, Mano mano, Long idCarta, String nroJugador) {
        // Buscar carta en el repositorio
        Carta cartaElegidaParaTirar = this.repositorioCarta.buscarCartaPorId(idCarta);

        // Saber jugador que tira
        Jugador jugador = truco.getJ1().getNumero().toString().equals(nroJugador) ? truco.getJ1() : truco.getJ2();

        // Definir al que le toca ahora (contrario al que tiró)
        this.leTocaTirar = truco.getJ1().getNumero().toString().equals(nroJugador) ? truco.getJ2() : truco.getJ1();

        if (mano == null) throw new TrucoException("La mano es nula.");
        if (jugador == null) throw new TrucoException("El jugador es nulo.");
        if (cartaElegidaParaTirar == null) throw new TrucoException("La carta es nula.");

        // Obtener copias de cartas del que tiró
        List<Carta> cartasJugador = jugador.getNumero().equals(1) ? mano.getCartasJ1() : mano.getCartasJ2();




            if (cartasJugador.contains(cartaElegidaParaTirar)) {
                // Sacarle carta que tira y agregarsela a las que tira
                sacarCartaDeJugador(jugador, cartaElegidaParaTirar, mano);

                // Sumar movimiento realizado
                mano.setMovimientos(mano.getMovimientos() + 1);
            } else {
                throw new TrucoException("La carta seleccionada no está en la mano del jugador.");
            }


        // Si el jugador tiró y ambos se quedaron sin cartas, terminó la mano
        if (mano.getCartasJ1().isEmpty() && mano.getCartasJ2().isEmpty()) {
            mano.setEstaTerminada(true);
        }
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
            while (iterator.hasNext() && m.getCartasJ1().size() < 3) {
                Carta carta = iterator.next();
                m.getCartasJ1().add(carta);
                iterator.remove();
                System.out.println("Asigno a J1: " + carta);
            }
        } else {
            Iterator<Carta> iterator = seisCartasRandom.iterator();
            while (iterator.hasNext() && m.getCartasJ2().size() < 3) {
                Carta carta = iterator.next();
                m.getCartasJ2().add(carta);
                iterator.remove();
                System.out.println("Asigno a J2: " + carta);
            }
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
    public Jugador preguntar(Mano mano, String accion, Integer nroJugador) {
        Partida truco = mano.getPartida();
        Jugador ejecutor = nroJugador.equals(1) ? truco.getJ1() : truco.getJ2();
        Jugador receptor = nroJugador.equals(1) ? truco.getJ2() : truco.getJ1();
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
            if(tieneFlor(ejecutor, mano) && !tieneFlor(receptor, mano)){
                this.puntosEnJuegoFlor = 3;
                return receptor;
            }
            preguntarFlor(accionRealizada, ejecutor);
            return receptor;

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
            mano.setRespondeAhora(null);
            this.repositorioTruco.merge(truco);
            this.repositorioMano.merge(mano);
            return null;
        } else {
            throw new TrucoException("Preguntar: ocurrió un error.");
        }
        // TODO: si la ronda no es la primera, no puede cantar envido
    }

    @Override
    public boolean tieneFlor(Jugador jugador, Mano mano) {
        if (jugador.getNumero().equals(1)) {
            List<Carta> cartas = mano.getCartasJ1();
            if (cartas.get(0).getPalo().equals(cartas.get(1).getPalo()) &&
                    cartas.get(1).getPalo().equals(cartas.get(2).getPalo())){
                return true;
            }
        }

        if (jugador.getNumero().equals(2)) {
            List<Carta> cartas = mano.getCartasJ2();
            if (cartas.get(0).getPalo().equals(cartas.get(1).getPalo()) &&
                    cartas.get(1).getPalo().equals(cartas.get(2).getPalo())){
                return true;
            }
        }

        return false;

    }


    @Override
    public void determinarGanadorRonda(Partida truco, Mano mano) {
        if (truco == null) throw new TrucoException("La partida es nula.");
        if (truco.getJ1() == null) throw new TrucoException("El jugador 1 es nulo.");
        if (truco.getJ2() == null) throw new TrucoException("El jugador 2 es nulo.");

        if(mano.getPuntosRondaJ1() != 2 || mano.getPuntosRondaJ2() != 2){
            obtenerGanadorDeRonda(truco, mano, truco.getJ1(), truco.getJ2());
        }

        if(mano.getPuntosRondaJ1() == 2 || mano.getPuntosRondaJ2() == 2){
            mano.setEstaTerminada(true);
        }

        if (mano.getEstaTerminada()) {
            this.puntosEnJuegoMano = this.puntosEnJuegoMano.equals(0) ? 1 : this.puntosEnJuegoMano;
            if (mano.getPuntosRondaJ1() > mano.getPuntosRondaJ2()) {
                truco.setPuntosJ1(truco.getPuntosJ1() + this.puntosEnJuegoMano);
                mano.setGanador(truco.getJ1());
                setUltimoGanadorMano(truco.getJ1());
                mano.getCartasJ1().clear();
                mano.getCartasJ2().clear();
            } else if (mano.getPuntosRondaJ1() < mano.getPuntosRondaJ2()) {
                mano.setGanador(truco.getJ2());
                truco.setPuntosJ2(truco.getPuntosJ2() + this.puntosEnJuegoMano);
                setUltimoGanadorMano(truco.getJ2());
                mano.getCartasJ1().clear();
                mano.getCartasJ2().clear();
            }
        }

        // Guardamos resultados en mano y truco
        this.repositorioTruco.merge(truco);
        this.repositorioMano.merge(mano);
    }

    private void obtenerGanadorDeRonda(Partida truco, Mano mano, Jugador jugador1, Jugador jugador2) {
        // Si ya tiraron los 2
        if (mano.getCartasTiradasJ1().size() == mano.getCartasTiradasJ2().size()) {

            // Conseguimos las últimas tiradas.
            Carta cartaJ1 = mano.getCartasTiradasJ1().get(mano.getCartasTiradasJ2().size() - 1);
            Carta cartaJ2 = mano.getCartasTiradasJ2().get(mano.getCartasTiradasJ1().size() - 1);

            // Comparamos quien tiró la más alta, en base a eso damos poder
            if (cartaJ1.getValor() > cartaJ2.getValor()) {
                System.out.println("Ronda j1");
                mano.setPuntosRondaJ1(mano.getPuntosRondaJ1() + 1);
                this.leTocaTirar = jugador1;
            } else if (cartaJ2.getValor() > cartaJ1.getValor()) {
                System.out.println("Ronda j2");
                mano.setPuntosRondaJ2(mano.getPuntosRondaJ2() + 1);
                this.leTocaTirar = jugador2;
            } else {
                System.out.println("Es parda");
                if(mano.getCartasTiradasJ1().size() == 1){
                    // Empardaron en la primer ronda, se define el ganador de la mano en la segunda ronda
                    mano.setPuntosRondaJ1(mano.getPuntosRondaJ1() + 1);
                    mano.setPuntosRondaJ2(mano.getPuntosRondaJ2() + 1);
                } else if (mano.getCartasTiradasJ1().size() == 2) {
                    // Empardaron en la segunda ronda, gana la mano el que gano la primer ronda
                    // Si la primer ronda fue parda tambien, se iria a la tercer ronda
                    if(mano.getPuntosRondaJ1().equals(mano.getPuntosRondaJ2())){
                        this.leTocaTirar = this.empezoLaMano;
                    }
                    if(!mano.getCartasTiradasJ1().get(0).equals(mano.getCartasTiradasJ2().get(0))){
                        declararGanadorAlGanadorDeLaPrimerRonda(truco, mano);
                    }

                } else if (mano.getCartasTiradasJ1().size() == 3) {
                    // Empardaron en la tercer ronda, gana la mano el que gano la primer ronda
                    declararGanadorAlGanadorDeLaPrimerRonda(truco, mano);
                }

            }
        }
    }

    private void declararGanadorAlGanadorDeLaPrimerRonda (Partida truco, Mano mano){
        Carta primerCartaTiradaJ1 = mano.getCartasTiradasJ1().get(0);
        Carta primerCartaTiradaJ2 = mano.getCartasTiradasJ2().get(0);

        if(primerCartaTiradaJ1.getValor() > primerCartaTiradaJ2.getValor()){
            mano.setPuntosRondaJ1(mano.getPuntosRondaJ1() + 1);
            mano.setEstaTerminada(true);
        }

        if(primerCartaTiradaJ2.getValor() > primerCartaTiradaJ1.getValor()){
            mano.setPuntosRondaJ2(mano.getPuntosRondaJ2() + 1);
            mano.setEstaTerminada(true);
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

    private void preguntarFlor(String accionRealizada, Jugador ejecutor) {
        switch (accionRealizada) {
            case "FLOR":
                if (ejecutor.getNumero().equals(1)) {
                    this.diceFlorJ1 = ejecutor;
                } else {
                    this.diceFlorJ2 = ejecutor;
                }
                puntosEnJuegoFlor += 3;
                break;
            case "CONTRAFLOR":
                diceContraflor = ejecutor;
                puntosEnJuegoEnvido += 6;
                break;
            case "CONTRAFLOR AL RESTO":
                diceContraflorAlResto = ejecutor;
                //TODO: Jugador que gana, gana la partida
                break;
            default:
                throw new TrucoException("Preguntar flor: ocurrió un error.");
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

        System.out.println("Accion planteada: " + accionQueResponde);
        System.out.println("Respuesta de accion: " + respuestaDeLaAccion);
        if (esTruco(accionQueResponde)) {
            System.out.println("PASO ALGO CON EL TRUCO");
            respondeAhora = manejarRespuestaTruco(truco, mano, respuestaDeLaAccion, ejecutor, receptor);
        } else if (esEnvido(accionQueResponde)) {
            System.out.println("PASO ALGO CON EL ENVIDO");
            respondeAhora = manejarRespuestaEnvido(truco, mano, respuestaDeLaAccion, ejecutor, receptor);
        } else if (esFlor(accionQueResponde)) {
        respondeAhora = manejarRespuestaFlor(truco, mano, respuestaDeLaAccion, ejecutor, receptor);
        
        } else {
            throw new TrucoException("Responder: ocurrió un error");
        }
        mano.setRespondeAhora(respondeAhora);
        this.repositorioTruco.merge(truco);
        this.repositorioMano.merge(mano);
        return respondeAhora;
    }

    private Jugador manejarRespuestaFlor(Partida truco, Mano mano, String respuestaDeLaAccion, Jugador ejecutor, Jugador receptor) {
        // Saber quien es el j1 y j2
        Jugador j1 = ejecutor.getNumero().equals(1) ? ejecutor : receptor;
        Jugador j2 = receptor.getNumero().equals(2) ? receptor : ejecutor;

        if (respuestaDeLaAccion.equals("CONTRAFLOR") || respuestaDeLaAccion.equals("CONTRAFLOR AL RESTO")) {
            // RESPUESTAS DIRECTAS
            if (respuestaDeLaAccion.equals("CONTRAFLOR")) {
                // CALCULAMOS TANTOS
                Integer tantosJ1 = this.calcularTantosFlor(j1, mano);
                Integer tantosJ2 = this.calcularTantosFlor(j2, mano);

                if (this.diceContraflorAlResto != null) {
                    // Contraflor al resto (anula todos los anteriores)
                    Integer puntosParaGanar = truco.getPuntosParaGanar();
                    Integer puntosJ1 = truco.getPuntosJ1();
                    Integer puntosJ2 = truco.getPuntosJ2();
                    Integer puntosParaElGanador = 0;
                    if (tantosJ1 > tantosJ2) {
                        puntosParaElGanador = puntosParaGanar - puntosJ2;
                        truco.setPuntosJ1(truco.getPuntosJ1() + puntosParaElGanador);
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
                    // Contraflor
                    if (tantosJ1 > tantosJ2) {
                        truco.setPuntosJ1(truco.getPuntosJ2() + this.puntosEnJuegoFlor);
                    } else if (tantosJ1 < tantosJ2) {
                        truco.setPuntosJ2(truco.getPuntosJ2() + this.puntosEnJuegoFlor);
                    } else {
                        // TODO: manejar solucion cuando tienen los mismos tantos
                    }
                    return null;
                }
            } else {
                // NO QUIERE
                return null;
            }
        } else if (respuestaDeLaAccion.equals("CONTRAFLOR AL RESTO")) {
            if (ejecutor.getNumero().equals(j1.getNumero())) {
                this.diceContraflorAlResto = ejecutor;
            } else {
                this.diceContraflorAlResto = receptor;
            }
            return receptor;
        }  else {
            throw new TrucoException("ManejarEnvido2: ocurrió un error.");
        }

    }

    private Integer calcularTantosFlor(Jugador jugador, Mano mano) {
        List<Carta> cartasJugador = null;
        if (jugador.getNumero().equals(1)) {
            cartasJugador = mano.getCartasJ1();
        } else {
            cartasJugador = mano.getCartasJ2();
        }

        Integer tantosIniciales = 20;
        for (Carta carta : cartasJugador) {
            if (carta.getNumero() >= 10 && carta.getNumero() <= 12) {
                tantosIniciales += 0;
            } else {
                tantosIniciales += carta.getNumero();
            }
        }
        return tantosIniciales;

    }


    private Jugador manejarRespuestaEnvido(Partida truco, Mano mano, String respuestaDeLaAccion,
                                           Jugador ejecutor, Jugador receptor) {
        // Saber quien es el j1 y j2
        Jugador j1 = ejecutor.getNumero().equals(1) ? ejecutor : receptor;

        if (respuestaDeLaAccion.equals("QUIERO") || respuestaDeLaAccion.equals("NO QUIERO")) {
            // RESPUESTAS DIRECTAS
            if (respuestaDeLaAccion.equals("QUIERO")) {
                // ACEPTA Y CALCULAMOS TANTOS
                Integer tantosJ1 = this.calcularTantosDeCartasDeUnJugador(mano.getCartasJ1());
                Integer tantosJ2 = this.calcularTantosDeCartasDeUnJugador(mano.getCartasJ2());
                if (this.diceFaltaEnvido != null) {
                    // falta envido (anula todos los anteriores)
                    Integer puntosParaGanar = truco.getPuntosParaGanar();
                    Integer puntosJ1 = truco.getPuntosJ1();
                    Integer puntosJ2 = truco.getPuntosJ2();
                    Integer puntosParaElGanador;
                    if (tantosJ1 > tantosJ2) {
                        puntosParaElGanador = puntosParaGanar - puntosJ2;
                        truco.setPuntosJ1(truco.getPuntosJ1() + puntosParaElGanador);
                    } else if (tantosJ1 < tantosJ2) {
                        puntosParaElGanador = puntosParaGanar - puntosJ1;
                        truco.setPuntosJ2(truco.getPuntosJ2() + puntosParaElGanador);
                    } else {
                        if(this.empezoLaMano.equals(truco.getJ1())){
                            puntosParaElGanador = puntosParaGanar - puntosJ2;
                            truco.setPuntosJ1(truco.getPuntosJ1() + puntosParaElGanador);
                        }
                        if(this.empezoLaMano.equals(truco.getJ2())){
                            puntosParaElGanador = puntosParaGanar - puntosJ1;
                            truco.setPuntosJ2(truco.getPuntosJ2() + puntosParaElGanador);
                        }
                    }

                    if (truco.getPuntosJ1().equals(truco.getPuntosParaGanar())) {
                        truco.setGanador(truco.getJ1());
                    } else if (truco.getPuntosJ2().equals(truco.getPuntosParaGanar())) {
                        truco.setGanador(truco.getJ2());
                    }

                    return null;
                } else {
                    // envidos
                    if (tantosJ1 > tantosJ2) {
                        truco.setPuntosJ1(truco.getPuntosJ1() + this.puntosEnJuegoEnvido);
                    } else if (tantosJ1 < tantosJ2) {
                        truco.setPuntosJ2(truco.getPuntosJ2() + this.puntosEnJuegoEnvido);
                    } else {
                        if(this.empezoLaMano.equals(truco.getJ1())){
                            truco.setPuntosJ1(truco.getPuntosJ1() + this.puntosEnJuegoEnvido);
                        }
                        if(this.empezoLaMano.equals(truco.getJ2())){
                            truco.setPuntosJ2(truco.getPuntosJ2() + this.puntosEnJuegoEnvido);
                        }
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
            case 10:
                respuestaDada = "CONTRAFLOR";
                break;
            case 11:
                respuestaDada = "CONTRAFLOR AL RESTO";
                break;
            default:
                return "";
        }
        return respuestaDada;
    }

    public static Jugador getRandom(Partida truco) {
        Random random = new Random();
        int i = random.nextInt(2) + 1;
        if (i == 1) {
            return truco.getJ1();
        } else {
            return truco.getJ2();
        }
    }

    public Jugador getUltimoGanadorMano() {
        return ultimoGanadorMano;
    }

    public void setUltimoGanadorMano(Jugador ultimoGanadorMano) {
        this.ultimoGanadorMano = ultimoGanadorMano;
    }
}
