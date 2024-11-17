package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.*;
import com.tallerwebi.dominio.excepcion.TrucoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class ServicioTrucoImpl implements ServicioTruco {

    private Truco truco;
    private Jugador turno;
    private Boolean trucoCantado;

    private RepositorioCarta repositorioCarta;
    private RepositorioTruco repositorioTruco; // partida
    private ServicioMano servicioMano;

    // LISTAS
    private List<Jugador> jugadores;
    private List<Ronda> rondas;
    private List<Mano> manos;

    // ATRIBUTOS
    private Boolean estaEmpezada = false;
    private Boolean estaLaManoTerminada = false;
    private Integer nroRonda = 0;
    private Integer nroMovimientos = 0;
    private Integer cartasTiradas = 0;

    // ATRIBUTOS CON CLASES
    private Mano manoActual;
    private Jugador ultimoGanadorDeMano;

    /*
    Class truco maneja todo
    Una mano tiene rondas
    La mano termina al haber 3 rondas (6 movimientos)
    Mano (id_jugador_ganador, puntos_ganados)
    */

    /*
    - MÉTODOS PRINCIPALES

    - MÉTODOS CARTA
    - MÉTODOS JUGADORES
    - MÉTODOS RONDA
    - MÉTODOS MANO

    */

    @Autowired
    public ServicioTrucoImpl(
            RepositorioCarta repositorioCarta,
            RepositorioTruco repositorioTruco,
            ServicioMano servicioMano) { // ServicioTurno turnos
        this.repositorioCarta = repositorioCarta;
        this.repositorioTruco = repositorioTruco;

        this.servicioMano = servicioMano;

        this.jugadores = new ArrayList<>();
        this.rondas = new ArrayList<>();
        this.manos = new ArrayList<>();
        this.ultimoGanadorDeMano = null;
    }

    // -- MÉTODOS PRINCIPALES

    // Comenzar partida
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
        repositorioTruco.guardarPartida(this.truco);

      truco truco  =    new Truco ();
      truco.setJugador1(j1);
      truco.setJugador2(j1);


      this.repositorioTruco.guardarPartida(truco);
      return truco;
    }

    // Comenzar partida (TEST)
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


    // Comenzar partida (TEST)
    @Override
    public void empezar(Jugador j1, Jugador j2, List<Carta> cartas) {
        servicioMano.empezar();

        this.jugadores.add(j1);
        this.jugadores.add(j2);

        this.estaEmpezada = true;
//        List<Carta> cartas = this.getCartas(); // 6 cartas random
        this.asignarCartasAJugadores(this.jugadores, cartas);
//        this.truco.asignarCartasJugadores(jugadores, cartas);
//        this.truco.empezarMano(jugadores);
    }

    @Override
    public void empezar(Jugador j1, Jugador j2) {

    }

    // Método para que el jugador tire una carta
    public void tirarCarta(Jugador jugador, Carta cartaSeleccionada) {
        if (!servicioMano.estaLaManoTerminada()) {
            // Si la mano no esta terminada
            List<Carta> cartasJugador = jugador.getCartas();
            if (cartasJugador.contains(cartaSeleccionada)) {
                jugador.tirarCarta(cartaSeleccionada);
                servicioMano.guardarRonda(jugador, cartaSeleccionada);
                System.out.println("GUARDE RONDA PA");
                this.cartasTiradas++;
            } else {
                throw new TrucoException("La carta seleccionada no está en la mano del jugador.");
            }
        } else {
            throw new TrucoException("Ocurrió un error al intentar tirar la carta");
        }
    }

    // --- MANEJO DE CARTAS ---

    // Obtener 6 aleatorias y asigna valor de envido.
    private List<Carta> getCartas() {
//        List<Carta> cartasBD = this.repositorioCarta.obtenerCartas();
        // SOLO TEST
        this.truco = new Truco();
        List<Carta> cartasBD = this.truco.getMazo().getCartas();
        System.out.println("Longitud cartas: " + cartasBD.size());
        // FIN SOLO TEST

        List<Carta> cartasBuscadas = new ArrayList<>();
        int indice = 0;
        int random = 0;
        Random r = new Random();
        while (indice < 6) {
            random = r.nextInt(cartasBD.size());
            Carta cartaRandom = cartasBD.get(random);
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

        // Asignar valor de envido.
        for (Carta c : cartasBuscadas) {
            if (c.getNumero() >= 10 && c.getNumero() <= 12) {
                c.setValorEnvido(0);
            } else {
                c.setValorEnvido(c.getNumero());
            }
        }

        return cartasBuscadas;
    }

    // Validación para no repetir
    private Boolean saberSiLaCartaSeRepiteEnMazo(List<Carta> cartas, Integer numero, String palo) {
        for (Carta carta : cartas) {
            if (carta.getNumero().equals(numero) && carta.getPalo().equalsIgnoreCase(palo)) {
                return true;
            }
        }
        return false;
    }

    // Asignar a jugadores de lista
    private void asignarCartasAJugadores(List<Jugador> jugadores, List<Carta> seis) {
        if (jugadores.size() == 2) {
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
            jugadores.get(0).setCartas(mazo1);
            jugadores.get(1).setCartas(mazo2);
        } else {
            // Algo
        }
    }


    // --- MANEJO DE JUGADORES ---

    @Override
    public Integer getCantidadJugadores() {
        return this.jugadores.size();
    }

    @Override
    public List<Jugador> getJugadores() {
        return this.jugadores;
    }

    // --- MANEJO DE RONDAS ---

    // Compara las cartas tiradas y suma punto de ronda al que tiro la de mayor valor
    // Si se llegaron a las 6 rondas, ya se determina el ganador
    // TODO: que pasa si se termina antes? (ejemplo: gano las 2 primeras rondas yo)
//    @Override
//    public void determinarGanadorRonda(Jugador jugador1, Jugador jugador2) {
//        // Si ya tiraron los 2
//        if (jugador1.getCartasTiradas().size() == jugador2.getCartasTiradas().size()) {
//            // Conseguimos las últimas tiradas.
//            Carta cartaJ1 = jugador1.getCartasTiradas().get(jugador1.getCartasTiradas().size() - 1);
//            Carta cartaJ2 = jugador2.getCartasTiradas().get(jugador2.getCartasTiradas().size() - 1);
//
//            // Comparamos quien tiró la más alta, en base a eso damos poder
//            if (cartaJ1.getValor() > cartaJ2.getValor()) {
//                servicioMano.sumarPuntoDeRonda(jugador1);
//                System.out.println("PuntoRonda para J1");
////                this.sumarPuntoDeRonda(jugador1);
//                cambiarTurno(jugador2);
//            } else if (cartaJ2.getValor() > cartaJ1.getValor()) {
//                servicioMano.sumarPuntoDeRonda(jugador2);
//                System.out.println("PuntoRonda para J2");
//                cambiarTurno(jugador1);
////                this.sumarPuntoDeRonda(jugador2);
//            }
//        }
//        System.out.println(servicioMano.getRondas().size());
//        if (servicioMano.getRondas().size() == 6) {
//            System.out.println("Ya se hicieron 6 rondas");
//            if (jugador1.getPuntosRonda() > jugador2.getPuntosRonda()) {
//                servicioMano.setGanadorDeMano(jugador1);
//                this.ultimoGanadorDeMano = jugador1;
//            } else {
//                servicioMano.setGanadorDeMano(jugador2);
//                this.ultimoGanadorDeMano = jugador2;
//            }
//        }
//    }


    @Override
    public List<Ronda> getRondas() {
        return servicioMano.getRondas();
    }

    // Suma punto para saber que jugador ganó la ronda
    // Sí Franco gano 2 rondas, tendrá 2 puntos.
    // Si Gonzalo ganó 1 ronda, tendrá 1 punto
    // Entonces el que se llevá un punto por ganar la mano es Franco
    // Ese punto se ve afectado si se canta truco, envido, flor, etc.
    private void sumarPuntoDeRonda(Jugador jugador) {
//        this.manoActual.guardarGanadorDeRonda(jugador);
        jugador.agregarPuntoRonda();
//        // Si ya se jugaronn todas las cartas
//        if (this.rondas.size() == 6) {
//            Jugador ganador = this.manoActual.obtenerGanador();
//
//            if (!this.saberSiHuboAlgunTruco()) {
//                System.out.println("sumarPuntoDeRonda: No hubo trucos cantados");
//                this.manoActual.acumularPuntosEnJuego(1); // por ganar 2/3 rondas sin truco
//            }
//
//            this.manoActual.acumularPuntosEnJuego(puntosEnJuegoDeLaMano); // por ganar algún truco
//
//            System.out.println("sumarPuntoDeRonda: Puntos en juego de la mano: " + this.manoActual.getPuntos());
//            ganador.acumularPuntosDePartida(this.manoActual.getPuntos());
//
////            this.ultimoGanador = ganador; // el ultimo que ganó
////            this.manoActual.setJugador(ganador); // el que ganó más rondas
//
//            this.manosDePartida.add(this.manoActual); // guardar historico
//
////            this.manoActual = null;
//        }
    }

    // --- MANEJO DE MANOS ---

    @Override
    public List<Mano> getManos() {
        return this.manos;
    }

    // Generar acción (truco, envido, real envido, vale 4)
    @Override
    public Integer accion(String accion, Jugador cantador, Jugador receptor, Integer puntosEnJuego) {
        Integer nroAccion = servicioMano.guardarAccion(cantador,accion, false, puntosEnJuego);
        return this.truco.guardarAccion(cantador, accion, false, puntosEnJuego);
    }

    // --- MANEJO DE ACCIONES ---

    // Preguntar si quiere una acción (truco, envido, real envido, vale 4)
    @Override
    public Integer preguntar(String accion, Jugador ejecutor, Jugador receptor) {
        String accionEncontrada = saberAccion(accion);
        System.out.println("servicioTruco.preguntar: recibí la acción " + accionEncontrada + ".");
        if (accionEncontrada.equalsIgnoreCase("TRUCO")) {
            return manejarTruco(accionEncontrada, ejecutor, receptor);
        } else {
            return manejarEnvido(accionEncontrada, ejecutor, receptor);
        }
    }

    // Responder a la acción propuesta (quiero, no quiero, envido, re truco)
    @Override
    public Jugador responder(String accion, Jugador ejecutor, Jugador receptor, Integer nroAccion) {
        String accionEncontrada = saberAccion(accion);
        Accion a = this.getAccionPorNro(nroAccion);
        Jugador ganador = null;
        Jugador leTocaResponder = null;

        if (accionEncontrada.equals("QUIERO") || accionEncontrada.equals("NO QUIERO")) {
            leTocaResponder = manejarRespuestaDirecta(accionEncontrada, a, ejecutor, receptor); // quiero, no quiero
        } else {
            leTocaResponder = manejarOtraAccion(accionEncontrada, a, ejecutor, receptor); // re-envido, re-truco, etc
        }

        return leTocaResponder;
    }


    // Terminar la mano cuando no tengan cartas
    // TODO: que se corte antes -> EJ: truco en la segunda ronda y la respuesta es "NO QUIERO"
    @Override
    public void terminarMano() {
//        this.truco.terminarManoActual();
    }

    private void manejarRespuestaTruco() {

    }

    private void acumularPuntosAlQueGanaElEnvido(Jugador ejecutor, Jugador receptor, Integer puntosViejos) {
        Integer tantosEjecutor = this.calcularTantosDeCartasDeUnJugador(ejecutor);
        Integer tantosReceptor = this.calcularTantosDeCartasDeUnJugador(receptor);
        Jugador ganador = tantosEjecutor > tantosReceptor ? ejecutor : receptor;
        ganador.acumularPuntosDePartida(puntosViejos);
    }

    private Jugador manejarRespuestaDirecta(String accion, Accion a, Jugador ejecutor, Jugador receptor) {
        Jugador leTocaResponder = null;
        a.setRespuesta(true);

        // Respuesta positiva
        if (accion.equals("QUIERO")) {
            Integer puntosViejos = a.getPuntosEnJuego();
            // condicionales para saber que se cantó
            if (esEnvido(a.getAccion())) { // SOLO ENVIDO/RE ENVIDO/REAL ENVIDO
                acumularPuntosAlQueGanaElEnvido(ejecutor, receptor, puntosViejos);
                leTocaResponder = ejecutor;
            } else if (a.getAccion().equals("FALTA ENVIDO")) {

            } else if (esTruco(a.getAccion())) { // TRUCO/RE TRUCO/VALE 4
                servicioMano.agregarPuntosEnJuegoManoActual(puntosViejos);
//                truco.agregarPuntosEnJuegoManoActual(puntosViejos);
                leTocaResponder = receptor;
            } else {
                // no debería entrar aca
            }
        } else if (accion.equals("NO QUIERO")) {
            leTocaResponder = ejecutor;
        } else {
            // nada debe entrar aca
        }
        return leTocaResponder;
    }

    private Boolean esEnvido(String accion) {
        return accion.equals("ENVIDO") || accion.equals("REAL ENVIDO");
    }

    private Boolean esTruco(String accion) {
        return accion.equals("TRUCO") || accion.equals("RE TRUCO") || accion.equals("VALE 4");
    }

    private Jugador manejarOtraAccion(String accion, Accion a, Jugador ejecutor, Jugador receptor) {
        switch (accion) {
            case "RE TRUCO":
            case "VALE 4":
                a.acumularPuntos(1);
                break;
            case "ENVIDO":
                a.acumularPuntos(2);
                break;
            case "REAL ENVIDO":
                a.acumularPuntos(3);
                break;
            case "FALTA ENVIDO":

                break;
        }
        return receptor;
    }


    private Integer manejarTruco(String accion, Jugador cantador, Jugador receptor) {
        if (accion.equalsIgnoreCase("TRUCO")) {
            return this.accion(accion, cantador, receptor, 2);
        } else if (accion.equals("RE TRUCO")) {
            return this.accion(accion, cantador, receptor, 3);
        } else if (accion.equals("VALE 4")) {
            return this.accion(accion, cantador, receptor, 4);
        } else {
            // todo
        }
        return null;
    }

    private Integer manejarEnvido(String accion, Jugador cantador, Jugador receptor) {
        if (accion.equalsIgnoreCase("ENVIDO")) {
            return this.accion(accion, cantador, receptor, 2);
        } else if (accion.equals("REAL ENVIDO")) {
            return this.accion(accion, cantador, receptor, 3);
        } else if (accion.equals("FALTA ENVIDO")) {
            // todo
        } else {
            // todo
        }
        return null;
    }

    @Override
    public String saberAccion(Integer nroAccion) {
        Accion a = this.getAccionPorNro(nroAccion);
        return a.getAccion();
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

    @Override
    public Accion getAccionPorNro(Integer nro) {
        return this.truco.getAccionPorNro(nro);
    }


    @Override
    public Integer getPuntosDeJugador(Jugador jugador) {
        return jugador.getPuntosPartida();
    }


    @Override
    public Integer getPuntosEnJuegoDeAccion(Integer nroAccion) {
        Accion a = this.getAccionPorNro(nroAccion);
        return a.getPuntosEnJuego();
    }


    @Override
    public Integer calcularTantosDeCartasDeUnJugador(Jugador jugador) {
        return this.truco.calcularEnvido(jugador.getCartas());
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


    // -- MÉTODOS REFERENTES AL MANEJO DE LA MANO

    // Saber si esta terminada
    @Override
    public Boolean saberSiLaManoEstaTerminada() {
        return this.truco.isLaManoTerminada();
    }

    // Obtener Lista de Mano jugadas en la partida
    @Override
    public List<Mano> getManosJugadas() {
        return this.truco.getManosDePartida();
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
    public Jugador getTurnoJugador() {
        return this.turno;
    }

    // -- HANDLERS DE RONDA
    // -- ACLARACIONES
    // Ronda -> Gonzalo tira una carta y Franco otra.
    // El que tira la mayor (peso) gana la ronda.
    // Es a 3 rondas, si Gonzalo gana 2, ya está. Franco pierde
    // Ejemplo -> Gonzalo tira un 1 de espada y Franco un 4 de basto.
    // La ronda la gana Gonzalo.
    // Tira el que gana la ronda -> Si gané la primera, en la segunda tiro yo primero.

    // Determinar quien gano la ronda
    // sumarPuntoDeRonda() -> suma punto para llevar control de quien ganará la mano.
    // cambiarTrurno() -> Depende quien la gano, es quien le toca ahora


    @Override
    public Jugador getUltimoGanadorDeMano() {
        return servicioMano.getGanadorDeManoActual();
    }

    @Override
    public Boolean esLaPrimerRonda() {
        return truco.getRondasDeManoActual().size() <= 1;
    }

    @Override
    public List<Accion> getAcciones() {
        return this.truco.getAcciones();
    }


//    @Override
//    public void verificarEnvido(Jugador jugador1, Jugador jugador2) {
//        Integer tantosJ1 = verLosTantos(jugador1);
//        Integer tantosJ2 = verLosTantos(jugador2);
//
//        if (tantosJ1 > tantosJ2) {
//            //puntosJugador1++;
//        }
//
//        if (tantosJ2 > tantosJ1) {
//            //puntosJugador2++;
//        }
//
//        if (tantosJ2.equals(tantosJ1)) {
//            //Gana jugador actual
//        }
//    }

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
                if (!todasLasCartas.get(0).getPalo().equals(todasLasCartas.get(1).getPalo()) && !todasLasCartas.get(1).getPalo().equals(todasLasCartas.get(2).getPalo())) {

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
            if ((todasLasCartas.get(i).getNumero() != 10 && todasLasCartas.get(i).getNumero() != 11 && todasLasCartas.get(i).getNumero() != 12) && (mayor.equals(null) || todasLasCartas.get(i).getNumero() > mayor.getNumero())) {
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

    @Override
    public Integer getCartasTiradas() {
        return this.cartasTiradas;
    }

    @Override
    public String getGanadorDeRondaPorNumero(int i) {
        return servicioMano.getGanadorDeRondaPorNumero(i);
    }

    public void setCartasTiradas(Integer cartasTiradas) {
        this.cartasTiradas = cartasTiradas;
    }
}



