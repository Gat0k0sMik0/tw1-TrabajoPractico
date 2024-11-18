package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.*;
import com.tallerwebi.dominio.excepcion.TrucoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ServicioManoImpl2 implements ServicioMano2 {

    @Autowired
    RepositorioMano repositorioMano;

    private List<Accion> acciones;

    private Integer nroAccion;
    private Integer puntosEnJuegoEnvido;

    Jugador diceEnvidoJ1;
    Jugador diceEnvidoJ2;
    Jugador diceRealEnvido;
    Jugador diceFaltaEnvido;
    Jugador jugadorMano;

    public ServicioManoImpl2(RepositorioMano repositorioMano) {
        this.acciones = new ArrayList<>();
        this.repositorioMano = repositorioMano;
        this.nroAccion = 0;
        this.diceEnvidoJ1 = null;
        this.diceEnvidoJ2 = null;
        this.diceRealEnvido = null;
        this.diceFaltaEnvido = null;
        this.puntosEnJuegoEnvido = 0;
    }

    @Override
    public Mano2 empezar(Truco2 t) {
        Mano2 m = new Mano2();
        repositorioMano.guardar(m);
        return m;
    }

    @Override
    public Mano2 obtenerManoPorId(Long id) {
        return repositorioMano.obtenerManoPorId(id);
    }

    @Override
    public Integer preguntar(Truco2 truco, String accion, Jugador ejecutor, Jugador receptor) {
        String accionEncontrada = saberAccion(accion);

        // TODO: si la ronda no es la primera, no puede cantar envido

        if (accionEncontrada.equals("ENVIDO")) {
            if (ejecutor.getNumero().equals(1)) {
                this.diceEnvidoJ1 = ejecutor;
            } else {
                this.diceEnvidoJ2 = ejecutor;
            }
            puntosEnJuegoEnvido += 2;
        } else if (accionEncontrada.equals("REAL ENVIDO")) {
            diceRealEnvido = ejecutor;
            puntosEnJuegoEnvido += 3;
        } else if (accionEncontrada.equals("FALTA ENVIDO")) {
            diceFaltaEnvido = ejecutor;
            // TODO: CALCULAR
        } else {

        }
        return this.puntosEnJuegoEnvido;
    }

    @Override
    public Jugador responder(Truco2 truco, String accion, String respuesta, Jugador ejecutor, Jugador receptor) {
        String accionQueResponde = saberAccion(accion);
        String respuestaDeLaAccion = saberAccion(respuesta);
        System.out.println(accionQueResponde);
        System.out.println(respuestaDeLaAccion);
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

    @Override
    public Integer obtenerPuntosEnJuegoDelEnvido() {
        return this.puntosEnJuegoEnvido;
    }

    private Jugador manejarRespuestaEnvido(Truco2 truco, String respuestaDeLaAccion, Jugador ejecutor, Jugador receptor) {
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
                        j1.setPuntosPartida(truco.getPuntosJ1());
                    } else if (tantosJ1 < tantosJ2) {
                        puntosParaElGanador = puntosParaGanar - puntosJ1;
                        truco.setPuntosJ2(truco.getPuntosJ2() + puntosParaElGanador);
                        j2.setPuntosPartida(truco.getPuntosJ2());
                    } else {
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







    private Jugador manejarRespuestaTruco(Truco2 truco, String respuestaDeLaAccion, Jugador ejecutor, Jugador receptor)
    {
        return null;
    }

//    // Responder a la acción propuesta (quiero, no quiero, envido, re truco)
//    @Override
//    public Jugador responder(Truco2 truco, String accion, Jugador ejecutor, Jugador receptor, Integer nroAccion) {
//
//
//        String accionEncontrada = saberAccion(accion);
//        Accion a = this.getAccionPorNro(nroAccion);
//        Jugador ganador = null;
//        Jugador leTocaResponder = null;
//
//        if (accionEncontrada.equals("QUIERO") || accionEncontrada.equals("NO QUIERO")) {
//            leTocaResponder = manejarRespuestaDirecta(accionEncontrada, a, ejecutor, receptor); // quiero, no quiero
//        } else {
//            leTocaResponder = manejarOtraAccion(accionEncontrada, a, ejecutor, receptor); // re-envido, re-truco, etc
//        }
//
//        return leTocaResponder;
//    }

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
//                servicioMano.agregarPuntosEnJuegoManoActual(puntosViejos);
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

    private void acumularPuntosAlQueGanaElEnvido(Jugador ejecutor, Jugador receptor, Integer puntosViejos) {
        Integer tantosEjecutor = this.calcularTantosDeCartasDeUnJugador(ejecutor);
        Integer tantosReceptor = this.calcularTantosDeCartasDeUnJugador(receptor);
        Jugador ganador = tantosEjecutor > tantosReceptor ? ejecutor : receptor;
        ganador.acumularPuntosDePartida(puntosViejos);
    }

    private Integer calcularTantosDeCartasDeUnJugador(Jugador jugador) {
        for (Carta c : jugador.getCartas()) {
            if (c.getNumero() >= 10 && c.getNumero() <= 12) {
                c.setValorEnvido(0);
            } else {
                c.setValorEnvido(c.getNumero());
            }
        }
        return this.calcularEnvido(jugador.getCartas());
    }

    public Integer calcularEnvido(List<Carta> cartas) {
        List<Carta> tieneDosDelMismoPalo = this.tieneDosDelMismoPalo(cartas);
        if (tieneDosDelMismoPalo.isEmpty()) {
            return 0;
        } else {
            return this.obtenerSumaDeLasMasAltas(tieneDosDelMismoPalo);
        }
    }

    private Integer manejarTruco(String accion, Jugador cantador, Jugador receptor) {
        if (accion.equalsIgnoreCase("TRUCO")) {
            return this.crearAccion(accion, cantador, receptor, 2);
        } else if (accion.equals("RE TRUCO")) {
            return this.crearAccion(accion, cantador, receptor, 3);
        } else if (accion.equals("VALE 4")) {
            return this.crearAccion(accion, cantador, receptor, 4);
        } else {
            // todo
        }
        return null;
    }

    private Integer manejarEnvido(String accion, Jugador cantador, Jugador receptor) {
        if (accion.equalsIgnoreCase("ENVIDO")) {
            return this.crearAccion(accion, cantador, receptor, 2);
        } else if (accion.equals("REAL ENVIDO")) {
            return this.crearAccion(accion, cantador, receptor, 3);
        } else if (accion.equals("FALTA ENVIDO")) {
            // todo
        } else {
            // todo
        }
        return null;
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

    private Accion getAccionPorNro(Integer nro) {
        for (Accion a : this.acciones) {
            if (a.getNroAccion().equals(nro)) {
                return a;
            }
        }
        return null;
    }

    private Integer crearAccion(String accion, Jugador cantador, Jugador receptor, Integer puntosEnJuego) {
        Accion a = new Accion(this.nroAccion++, cantador, accion, false, puntosEnJuego);
        this.acciones.add(a);
        return a.getNroAccion();
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
