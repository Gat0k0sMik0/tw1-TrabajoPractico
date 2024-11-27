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
    private ServicioPartida2 servicioTruco;
    @Autowired
    private ServicioMano servicioMano2;
    @Autowired
    private ServicioRonda2 servicioRonda2;


    public ControladorTruco(ServicioPartida2 servicioTruco,
                            ServicioMano servicioMano2,
                            ServicioRonda2 servicioRonda2) {
        this.servicioTruco = servicioTruco;
        this.servicioMano2 = servicioMano2;
        this.servicioRonda2 = servicioRonda2;
    }

    @RequestMapping("/partida-truco")
    public ModelAndView irAPartidaTruco(HttpSession session) {

        ModelMap model = new ModelMap();
        Long partidaId = (Long) session.getAttribute("idPartida");

        if (partidaId != null) {
            Truco2 partida = servicioTruco.obtenerPartidaPorId(partidaId);
            Mano mano = servicioMano2.obtenerManoPorId(partidaId);
            Jugador leTocaTirar = servicioMano2.saberQuienTiraAhora();
            Ronda ronda = new Ronda();
            ronda.setId(0L);

            //model.put("responde", servicioMano2.saberQuienResponde(j1, j2)); // TODO: terminar
            model.put("cartasJugador1", mano.getCartasJ1());
            model.put("cartasJugador2", mano.getCartasJ2());

            model.put("jugador1", partida.getJ1());
            model.put("jugador2", partida.getJ2());

            model.put("cartasTiradasJ1", mano.getCartasTiradasJ1());
            model.put("cartasTiradasJ2", mano.getCartasTiradasJ2());

            model.put("puntosJ1", partida.getPuntosJ1());
            model.put("puntosJ2", partida.getPuntosJ2());

            model.put("mostrarRespuestasEnvidoJ1", false);
            model.put("mostrarRespuestasEnvidoJ2", false);
            model.put("mostrarRespuestasTrucoJ1", false);
            model.put("mostrarRespuestasTrucoJ2", false);
            model.put("mostrarRespuestasJ1", true);
            model.put("mostrarRespuestasJ2", true);

            model.put("puntosParaGanar", partida.getPuntosParaGanar());
            model.put("mano", mano);
            model.put("ronda", ronda);
            model.put("partida", partida);
            model.put("partidaIniciada", true);

            model.put("turnoJugador", leTocaTirar != null ? leTocaTirar.getNumero() : 1);

            if (leTocaTirar != null) {
                System.out.println("Le toca tirar a: " + leTocaTirar.getNumero());

            }
        }

        return new ModelAndView("partida-truco", model);
    }

    @GetMapping("/comenzar-truco")
    public ModelAndView comenzarTruco(HttpSession sesion) {
        System.out.println("Empezado");
        Usuario usuario = (Usuario) sesion.getAttribute("usuarioActivo");
        if (usuario == null) return new ModelAndView("redirect:/login");

        // Asignamos usuario como jugador
        Jugador jugador1 = new Jugador();
        Jugador jugador2 = new Jugador();
        jugador1.setNombre(usuario.getNombreUsuario());
        jugador2.setNombre("Juancito");
        jugador1.setNumero(1);
        jugador2.setNumero(2);

        // Empezamos la partida
        Truco2 truco = this.servicioTruco.empezar(jugador1, jugador2);

        // Empezamos mano
        Mano m = servicioMano2.empezar(truco, jugador1, jugador2);

        // Guardar IDs en la sesión
        sesion.setAttribute("idPartida", truco.getId());

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
        Truco2 truco = servicioTruco.obtenerPartidaPorId(idPartida);
        if (truco == null) return new ModelAndView("redirect:/home");

        // Buscar id_mano de parametro en BD
        Mano mano = servicioMano2.obtenerManoPorId(Long.parseLong(manoId));
        if (mano == null) return new ModelAndView("redirect:/home");

        // Tiramos carta, retorna ronda creada
        Ronda ronda = servicioMano2.tirarCarta(truco, mano, cartaId, nroJugador);
        if (ronda == null) return new ModelAndView("redirect:/home");

        // Para saber quien tira la proxima ronda. Si es null, hay parda
        servicioMano2.determinarGanadorRonda(truco, mano);

        return new ModelAndView("redirect:/partida-truco");
    }


//    @GetMapping(path = "/accion")
//    public ModelAndView accion(
//            @RequestParam("mano") String manoId,
//            @RequestParam("ronda") String rondaNro,
//            @RequestParam("accion") String accionValue,
//            @RequestParam("jugador") String nroJugador,
//            HttpSession session,
//            RedirectAttributes redirectAttributes
//    ) {
//        Jugador j1 = (Jugador) session.getAttribute("jugador1");
//        Jugador j2 = (Jugador) session.getAttribute("jugador2");
//        Integer idPartida = (Integer)session.getAttribute("idPartida");
//        if (j1 == null || j2 == null) return new ModelAndView("redirect:/home");
//
//        Jugador actuador = j1.getNumero().toString().equals(nroJugador) ? j1 : j2;
//        Jugador receptor = actuador.getNombre().equals(j1.getNombre()) ? j2 : j1;
//
//        ModelMap model = new ModelMap();
//
//        // NUEVA LOGICA
//
//        // Obtener partida
//        Mano2 mano = servicioMano2.obtenerManoPorId(Long.getLong(manoId));
//
//        // Saber quien reponde -> null si se va al mazo
//        Jugador respondeAhora = servicioMano2.preguntar(mano, accionValue, actuador, receptor);
//
//        if (respondeAhora == null) {
//            redirectAttributes.addFlashAttribute("j1", j1);
//            redirectAttributes.addFlashAttribute("j2", j2);
//            return new ModelAndView("redirect:/mazo");
//        }
//
//        session.setAttribute("leTocaResponder", respondeAhora);
//
//
//
//        // FIN NUEVA LOGICA
//
//        // Acción de ir al mazo
//        if ("9".equals(accionValue)) {
//            receptor.ganarPuntosPorIrseAlMazo();
//
//            // Limpiar las cartas de ambos jugadores y sumar 2 puntos al receptor
//            j1.getCartas().clear();
//            j2.getCartas().clear();
//            receptor.setPuntosRonda(receptor.getPuntosRonda() + 2);
//            if (receptor.getNombre().equals(j1.getNombre())) {
//                j1.setPuntosRonda(receptor.getPuntosRonda());
//            } else {
//                j2.setPuntosRonda(receptor.getPuntosRonda());
//            }
//            // Llamar al servicio para terminar la mano
//            servicioTruco.terminarMano();
//
//            // Guardar el estado actualizado en la sesión
//            session.setAttribute("jugador1", j1);
//            session.setAttribute("jugador2", j2);
//            session.setAttribute("mensaje", actuador.getNombre() + " se fue al mazo. " +
//                    receptor.getNombre() + " gana 2 puntos.");
//            session.setAttribute("puntosJ1", servicioTruco.getPuntosDeJugador(j1));
//            session.setAttribute("puntosJ2", servicioTruco.getPuntosDeJugador(j2));
//            return new ModelAndView("redirect:/partida-truco");
//        }
//
//
//        // Utiles
//        session.setAttribute("envidoValido", false);
//        if (saberAccion.equals("ENVIDO")) {
//            Integer tantoJ1 = servicioTruco.calcularTantosDeCartasDeUnJugador(j1);
//            Integer tantoJ2 = servicioTruco.calcularTantosDeCartasDeUnJugador(j2);
//            session.setAttribute("tantoJ1", tantoJ1);
//            session.setAttribute("tantoJ2", tantoJ2);
//
//            // Mostrar respuesta -> quiero/no quiero
//            if (actuadorNombre.equalsIgnoreCase(j1.getNombre())) {
//                session.setAttribute("mostrarRespuestasJ2", false);
//                session.setAttribute("mostrarRespuestasEnvidoJ2", true);
//                session.setAttribute("mostrarRespuestasTrucoJ2", false);
//            } else {
//                session.setAttribute("mostrarRespuestasJ1", false);
//                session.setAttribute("mostrarRespuestasEnvidoJ1", true);
//                session.setAttribute("mostrarRespuestasTrucoJ1", false);
//            }
//        }
//
//        if (saberAccion.equals("TRUCO")) {
//            // Mostrar respuesta -> quiero/no quiero
//            if (actuadorNombre.equalsIgnoreCase(j1.getNombre())) {
//                session.setAttribute("mostrarRespuestasJ2", false);
//                session.setAttribute("mostrarRespuestasEnvidoJ2", false);
//                session.setAttribute("mostrarRespuestasTrucoJ2", true);
//            } else {
//                session.setAttribute("mostrarRespuestasJ1", false);
//                session.setAttribute("mostrarRespuestasEnvidoJ1", false);
//                session.setAttribute("mostrarRespuestasTrucoJ1", true);
//            }
//        }
//
//        // SOLO PARA VER EN DESARROLLO
//        session.setAttribute("acciones", servicioTruco.getAcciones());
//        session.setAttribute("nroDeAccionAResponder", nroAccion);
//
//        return new ModelAndView("redirect:/partida-truco");
//    }


//    @RequestMapping("/cambiar-mano")
//    public ModelAndView cambiarMano (
//            @ModelAttribute("j1") Jugador j1,
//            @ModelAttribute("j2") Jugador j2,
//            RedirectAttributes redirectAttributes,
//            HttpSession session
//    ) {
//        Long idPartida =  (Long) session.getAttribute("idPartida");
//        Truco2 truco = servicioTruco.obtenerPartidaPorId(idPartida);
//        servicioTruco.reset(j1, j2);
//        servicioMano2.reset(truco);
//        servicioRonda2.reset();
//        redirectAttributes.addFlashAttribute("partida", truco);
//        // hacer
//    }

//    @GetMapping(path = "/respuesta")
//    public ModelAndView responder(
//            @RequestParam("accion") String accionAlCualResponde,
//            @RequestParam("mano") String idMano,
//            @RequestParam("ronda") String idRonda,
//            @RequestParam("respuesta") String nroRespuesta,
//            @RequestParam("jugador") String nroJugador,
//            HttpSession session
//    ) {
//        Jugador j1 = (Jugador) session.getAttribute("jugador1");
//        Jugador j2 = (Jugador) session.getAttribute("jugador2");
//        Integer idPartida = (Integer)session.getAttribute("idPartida");
//        if (j1 == null || j2 == null) return new ModelAndView("redirect:/home");
//
//        Jugador actuador = saberJugadorPorNombre(nroJugador, j1, j2);
//        Jugador receptor = null;
//        Integer nroDeAccionAResponder = Integer.getInteger(accionAlCualResponde);
//
//        receptor = actuador.getNombre().equals(j1.getNombre()) ? j2 : j1;
//
//        // NUEVA LOGICA
//        Truco2 truco = servicioTruco.obtenerPartidaPorId(Long.getLong(idPartida.toString()));
//
//        // Retorna jugador que le toca responder si es que responde algo que no sea quiero/no quiero. Si es así, da null;
//        Jugador jugador = servicioMano2.responder(truco, accionAlCualResponde, nroRespuesta, actuador, receptor);
//
//        // FIN NUEVA LÓGICA
//
//        // Desarrollo (despues borrar)
//        String respuestaDada = saberAccion(nroRespuesta);
//
//        if (respuestaDada.equals("QUIERO") || respuestaDada.equals("NO QUIERO")) {
//            session.setAttribute("mostrarRespuestasJ1", true);
//            session.setAttribute("mostrarRespuestasJ2", true);
//            session.setAttribute("mostrarRespuestasTrucoJ1", false);
//            session.setAttribute("mostrarRespuestasTrucoJ2", false);
//            session.setAttribute("mostrarRespuestasEnvidoJ1", false);
//            session.setAttribute("mostrarRespuestasEnvidoJ2", false);
//        } else {
//            if (leTocaResponder.getNombre().equalsIgnoreCase(j1.getNombre())) {
//                session.setAttribute("mostrarRespuestasJ1", false);
//                if (esEnvido(saberAccion)) {
//                    session.setAttribute("mostrarRespuestasEnvidoJ1", true);
//                    session.setAttribute("mostrarRespuestasEnvidoJ2", false);
//                } else if (esTruco(saberAccion)) {
//                    session.setAttribute("mostrarRespuestasTrucoJ1", true);
//                    session.setAttribute("mostrarRespuestasTrucoJ2", false);
//                } else {
//                    System.out.println("No debí entrar aca, revisame.");
//                }
//            } else {
//                session.setAttribute("mostrarRespuestasJ2", false);
//                if (esEnvido(saberAccion)) {
//                    session.setAttribute("mostrarRespuestasEnvidoJ2", true);
//                    session.setAttribute("mostrarRespuestasEnvidoJ1", false);
//                } else if (esTruco(saberAccion)) {
//                    session.setAttribute("mostrarRespuestasTrucoJ2", true);
//                    session.setAttribute("mostrarRespuestasTrucoJ1", false);
//                } else {
//                    System.out.println("No debí entrar aca, revisame.");
//                }
//            }
//        }
//
//        // UTILES
//        session.setAttribute("puntosJ1", servicioTruco.getPuntosDeJugador(j1));
//        session.setAttribute("puntosJ2", servicioTruco.getPuntosDeJugador(j2));
//
//        // SOLO PARA VER EN DESARROLLO
//        session.setAttribute("acciones", servicioTruco.getAcciones());
//        session.setAttribute("puntosEnJuego", servicioTruco.getPuntosEnJuegoDeAccion(nroDeAccionAResponder));
//
//        return new ModelAndView("redirect:/partida-truco");
//    }


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