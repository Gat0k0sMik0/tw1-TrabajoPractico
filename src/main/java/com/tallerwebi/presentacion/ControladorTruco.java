package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;


@Controller
public class ControladorTruco {

    @Autowired
    private ServicioPartida servicioTruco;
    @Autowired
    private ServicioMano servicioMano;
    @Autowired
    private ServicioRonda servicioRonda2;


    public ControladorTruco(ServicioPartida servicioTruco,
                            ServicioMano servicioMano2,
                            ServicioRonda servicioRonda2) {
        this.servicioTruco = servicioTruco;
        this.servicioMano = servicioMano2;
        this.servicioRonda2 = servicioRonda2;
    }

    @RequestMapping("/partida-truco")
    public ModelAndView irAPartidaTruco(HttpSession session) {

        ModelMap model = new ModelMap();
        Long partidaId = (Long) session.getAttribute("idPartida");

        if (partidaId != null) {
            Partida partida = servicioTruco.obtenerPartidaPorId(partidaId);
            Mano mano = servicioMano.obtenerManoPorId(partidaId);

            model.put("seTermino", mano.getEstaTerminada());

            Jugador leTocaTirar = servicioMano.saberQuienTiraAhora();

            Ronda ronda = new Ronda();
            ronda.setId(0L);

            model.put("cartasJugador1", mano.getCartasJ1());
            model.put("cartasJugador2", mano.getCartasJ2());

            model.put("jugador1", partida.getJ1());
            model.put("jugador2", partida.getJ2());

            model.put("cartasTiradasJ1", mano.getCartasTiradasJ1());
            model.put("cartasTiradasJ2", mano.getCartasTiradasJ2());

            model.put("puntosJ1", partida.getPuntosJ1());
            model.put("puntosJ2", partida.getPuntosJ2());

            System.out.println("Responde ahora: " + mano.getRespondeAhora());

            model.put("mostrarRespuestasEnvidoJ1", mano.getRespondeAhora() != null);
            model.put("mostrarRespuestasEnvidoJ2", mano.getRespondeAhora() != null);
            model.put("mostrarRespuestasTrucoJ1", mano.getRespondeAhora() != null);
            model.put("mostrarRespuestasTrucoJ2", mano.getRespondeAhora() != null);
            model.put("mostrarRespuestasJ1", mano.getRespondeAhora() == null);
            model.put("mostrarRespuestasJ2", mano.getRespondeAhora() == null);

            model.put("puntosParaGanar", partida.getPuntosParaGanar());
            model.put("mano", mano);
            model.put("ronda", ronda);
            model.put("partida", partida);
            model.put("partidaIniciada", true);
            model.put("accionAResponder", session.getAttribute("accionAResponder"));

            model.put("turnoJugador", leTocaTirar != null ? leTocaTirar.getNumero() : 1);
            model.put("leTocaResponder", mano.getRespondeAhora());
        }

        return new ModelAndView("partida-truco", model);
    }

    @GetMapping("/comenzar-truco")
    public ModelAndView comenzarTruco(@RequestParam("jugador1") String nombreJugador1,
                                      @RequestParam("jugador2") String nombreJugador2,
                                      HttpSession session) {
        System.out.println("Empezado");
        Usuario usuario = (Usuario) session.getAttribute("usuarioActivo");
        if (usuario == null) return new ModelAndView("redirect:/login");

        // Asignamos usuario como jugador
        Jugador jugador1 = new Jugador();
        Jugador jugador2 = new Jugador();
        jugador1.setNombre(nombreJugador1);
        jugador2.setNombre(nombreJugador2);

        // Empezamos la partida
        Partida truco = this.servicioTruco.empezar(jugador1, jugador2);

        // Empezamos mano
        servicioMano.empezar(truco, jugador1, jugador2);

        // Guardar IDs en la sesión
        session.setAttribute("idPartida", truco.getId());

        return new ModelAndView("redirect:/partida-truco");
    }

    @GetMapping(path = "/accion-tirar")
    public ModelAndView accionTirarCarta(
            @RequestParam("cartaId") Long cartaId,
            @RequestParam("manoId") String manoId,
            @RequestParam("nroJugador") String nroJugador,
            HttpSession session) {

        Long idPartida = (Long) session.getAttribute("idPartida");

        // Obtener partida
        Partida truco = servicioTruco.obtenerPartidaPorId(idPartida);
        if (truco == null) return new ModelAndView("redirect:/home");

        // Buscar id_mano de parametro en BD
        Mano mano = servicioMano.obtenerManoPorId(Long.parseLong(manoId));
        if (mano == null) return new ModelAndView("redirect:/home");

        // Tiramos carta, retorna ronda creada
        Ronda ronda = servicioMano.tirarCarta(truco, mano, cartaId, nroJugador);
        if (ronda == null) return new ModelAndView("redirect:/home");

        // Para saber quien tira la proxima ronda. Si es null, hay parda
        servicioMano.determinarGanadorRonda(truco, mano);

        return new ModelAndView("redirect:/partida-truco");
    }

    @RequestMapping("/cambiar-mano")
    public ModelAndView cambiarMano(
            HttpSession session
    ) {
        Long idPartida = (Long) session.getAttribute("idPartida");

        Partida truco = servicioTruco.obtenerPartidaPorId(idPartida);
        Mano ultimaMano = servicioMano.obtenerManoPorId(idPartida);
        ultimaMano.setConfirmacionTerminada(true);
        servicioMano.guardar(ultimaMano);
        servicioMano.reset(truco);

        // Guardar IDs en la sesión
        session.setAttribute("idPartida", truco.getId());

        return new ModelAndView("redirect:/partida-truco");
    }


    @GetMapping(path = "/accion")
    public ModelAndView accion(
            @RequestParam("accion") String accionValue,
            @RequestParam("jugador") String nroJugador,
            HttpSession session
    ) {
        Long idPartida = (Long) session.getAttribute("idPartida");
        session.setAttribute("accionAResponder", accionValue);

        // Obtener partida y mano
        Mano mano = servicioMano.obtenerManoPorId(idPartida);

        System.out.println(mano);

        // Saber quien reponde -> null si se va al mazo
        Jugador respondeAhora = servicioMano.preguntar(mano, accionValue, Integer.parseInt(nroJugador));

        if (respondeAhora == null) {
            // TODO VA AL MAZO
        }


        return new ModelAndView("redirect:/partida-truco");
    }


    @GetMapping(path = "/respuesta")
    public ModelAndView responder(
            @RequestParam("accion") String accionAlCualResponde,
            @RequestParam("respuesta") String nroRespuesta,
            @RequestParam("jugador") String nroJugador,
            HttpSession session

    ) {

        Long idPartida = (Long) session.getAttribute("idPartida");

        Mano mano = servicioMano.obtenerManoPorId(idPartida);

        // Retorna jugador que le toca responder si es que responde algo que no sea quiero/no quiero. Si es así, da null;
        Jugador respondeAhora = servicioMano.responder(mano, accionAlCualResponde, nroRespuesta, Integer.parseInt(nroJugador));

        if (respondeAhora == null) {
            // TODO RECHAZA
        }

        return new ModelAndView("redirect:/partida-truco");
    }


//    @RequestMapping(path = "/accion-envido", method = RequestMethod.POST)
//    public ModelAndView cantarEnvido(
//            @RequestParam("jugador") String jugadorNombre,
//            @RequestParam("respuesta") String respuesta,
//            HttpSession session) {
//        Jugador jugador1 = (Jugador) session.getAttribute("jugador1");
//        Jugador jugador2 = (Jugador) session.getAttribute("jugador2");
//        Jugador cantador = null;
//        Jugador receptor = null;
//
//        if (jugador1 == null || jugador2 == null) return new ModelAndView("redirect:/home");
//
//        // Mostrar respuesta -> quiero/no quiero
//        if (jugadorNombre.equalsIgnoreCase(jugador1.getNombre())) {
//            session.setAttribute("mostrarRespuestasEnvidoJ2", true);
//            cantador = jugador1;
//            receptor = jugador2;
//        } else {
//            session.setAttribute("mostrarRespuestasEnvidoJ1", true);
//            cantador = jugador2;
//            receptor = jugador1;
//        }
//
//        servicioTruco.accion("envido", cantador, receptor, 2);
//        Integer tantoJ1 = servicioTruco.calcularTantosDeCartasDeUnJugador(jugador1);
//        Integer tantoJ2 = servicioTruco.calcularTantosDeCartasDeUnJugador(jugador2);
//
//        // Actualizar los jugadores en la sesión para mantener el estado del juego
//        session.setAttribute("jugador1", jugador1);
//        session.setAttribute("jugador2", jugador2);
//        session.setAttribute("cartasTiradasJ1", jugador1.getCartasTiradas());
//        session.setAttribute("cartasTiradasJ2", jugador2.getCartasTiradas());
//        session.setAttribute("cartasJugador1", jugador1.getCartas());
//        session.setAttribute("cartasJugador2", jugador2.getCartas());
//        session.setAttribute("jugador1", jugador1);
//        session.setAttribute("jugador2", jugador2);
//        session.setAttribute("turnoJugador", servicioTruco.getTurnoJugador());
//        session.setAttribute("envidoValido", false);
//        session.setAttribute("tantoJ1", tantoJ1);
//        session.setAttribute("tantoJ2", tantoJ2);
//
//        // para ver como va
//        session.setAttribute("movimientos", servicioTruco.getMovimientosDeLaManoActual());
//        session.setAttribute("rondas", servicioTruco.getRondasDeLaManoActual());
//        session.setAttribute("nroRondas", servicioTruco.getNumeroDeRondasJugadasDeLaManoActual());
//
//        return new ModelAndView("redirect:/partida-truco");
//    }

//    @RequestMapping("/final-partida")
//    public ModelAndView finalizarPartida(Jugador jugador, @RequestParam("jugador") String jugadorNombre,
//                                         HttpSession session) {
//        ModelMap model = new ModelMap();
//        model.put("mensaje", "¡" + jugador.getNombre() + " ha ganado la partida!");
//
//        session.setAttribute("jugadas", servicioTruco.getRondasJugadas().size());
//        session.setAttribute("rondas", servicioTruco.getRondasJugadas());
//
//        return new ModelAndView("redirect:/home", model);
//    }


    private String saberAccionEnvido(String respuesta) {
        String respuestaDada = "";
        switch (Integer.parseInt(respuesta)) {
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
            default:
                return "";
        }
        return respuestaDada;
    }

//    private Carta getCartaDeLasCartasDelJugadorPorId(Long idCarta, Jugador jugador) {
//        for (Carta carta : jugador.getCartas()) {
//            if (carta.getId().equals(idCarta)) {
//                return carta;
//            }
//        }
//        return null;
//    }

    /*LOGICA TRUCO*/

    @PostMapping("/responderTruco")
    public ModelAndView responderTruco(
            @RequestParam("respuestaTruco") String respuesta,
            HttpSession session,
            @RequestParam("jugador") String jugadorNombre) {

        Jugador jugador1 = (Jugador) session.getAttribute("jugador1");
        Jugador jugador2 = (Jugador) session.getAttribute("jugador2");
        String respuestaDada = "";

        switch (Integer.parseInt(respuesta)) {
            case 0:
                respuestaDada = "NO QUIERO";
                break;
            case 1:
                respuestaDada = "QUIERO";
                break;
            case 2:
                respuestaDada = "RE TRUCO";
                break;
            case 3:
                respuestaDada = "VALE CUATRO";
                break;
            default:
                return new ModelAndView("redirect:/login");
        }

        return new ModelAndView("redirect:/partida-truco");
    }

    // Métodos para separar responsabilidades

    private Boolean esEnvido(String accion) {
        return accion.equalsIgnoreCase("ENVIDO") ||
                accion.equalsIgnoreCase("REAL ENVIDO") ||
                accion.equalsIgnoreCase("FALTA ENVIDO");
    }

    private Boolean esTruco(String accion) {
        return accion.equalsIgnoreCase("TRUCO") || accion.equalsIgnoreCase("RE TRUCO") || accion.equalsIgnoreCase("VALE CUATRO");
    }

    private Jugador saberJugadorPorNombre(String nroJugador, Jugador j1, Jugador j2) {
        if (j1.getNumero().toString().equals(nroJugador)) return j1;
        if (j2.getNumero().toString().equals(nroJugador)) return j2;
        return null;
    }


    // Métodos para desarrollo
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