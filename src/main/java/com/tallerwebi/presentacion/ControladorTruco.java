package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.*;
import com.tallerwebi.dominio.excepcion.TrucoException;
import com.tallerwebi.infraestructura.ServicioTrucoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;


@Controller
public class ControladorTruco {

    @Autowired
    private ServicioTruco servicioTruco;

    public ControladorTruco(ServicioTruco servicioTruco) {
        this.servicioTruco = servicioTruco;
    }

    @RequestMapping("/partida-truco")
    public ModelAndView irAPartidaTruco(HttpSession session) {
        // Si no hay usuario, lo echamos de acá
        if (session.getAttribute("usuarioActivo") == null) return new ModelAndView("redirect:/login");

        List<Carta> cartasJugador1 = (List<Carta>) session.getAttribute("cartasJugador1");
        List<Carta> cartasJugador2 = (List<Carta>) session.getAttribute("cartasJugador2");
        Boolean isLaManoTerminada = (Boolean)session.getAttribute("terminada");

        ModelMap model = new ModelMap();

        // Saber si se terminó la mano y renderizar contenido
        if (isLaManoTerminada != null) {
//            return new ModelAndView("redirect:/home");
        }

        model.put("cartasJugador1", cartasJugador1);
        model.put("cartasJugador2", cartasJugador2);
        model.put("jugador1", session.getAttribute("jugador1"));
        model.put("jugador2", session.getAttribute("jugador2"));
        model.put("cartasTiradasJ1", session.getAttribute("cartasTiradasJ1"));
        model.put("cartasTiradasJ2", session.getAttribute("cartasTiradasJ2"));
        model.put("turnoJugador", session.getAttribute("turnoJugador"));
        model.put("todasLasCartas", session.getAttribute("todasLasCartas"));
        model.put("partidaIniciada", session.getAttribute("partidaIniciada"));
        model.put("terminada", session.getAttribute("terminada"));
        model.put("mostrarRespuestasEnvidoJ1", session.getAttribute("mostrarRespuestasEnvidoJ1"));
        model.put("mostrarRespuestasEnvidoJ2", session.getAttribute("mostrarRespuestasEnvidoJ2"));
        model.put("mostrarRespuestasTrucoJ1", session.getAttribute("mostrarRespuestasTrucoJ1"));
        model.put("mostrarRespuestasTrucoJ2", session.getAttribute("mostrarRespuestasTrucoJ2"));
        model.put("mostrarRespuestasJ1", session.getAttribute("mostrarRespuestasJ1"));
        model.put("mostrarRespuestasJ2", session.getAttribute("mostrarRespuestasJ2"));

        // handle de envido
        model.put("responde", session.getAttribute("responde"));
        model.put("puntosEnJuego", session.getAttribute("puntosEnJuego"));
        model.put("reEnvido", session.getAttribute("reEnvido"));
        model.put("realEnvido", session.getAttribute("realEnvido"));

        // Para ver como va
        model.put("rondas", session.getAttribute("rondas"));
        model.put("manos", session.getAttribute("manos"));
        model.put("movimientos", session.getAttribute("movimientos"));
        model.put("nroRondas", session.getAttribute("nroRondas"));
        model.put("envidoValido", session.getAttribute("envidoValido"));
        model.put("tantoJ1", session.getAttribute("tantoJ1"));
        model.put("tantoJ2", session.getAttribute("tantoJ2"));
        model.put("acciones", session.getAttribute("acciones"));
        model.put("trucoValido", session.getAttribute("trucoValido"));
        model.put("puntosJ1", session.getAttribute("puntosJ1"));
        model.put("puntosJ2", session.getAttribute("puntosJ2"));

        return new ModelAndView("partida-truco", model);
    }

    @RequestMapping("/comenzar-truco")
    public ModelAndView comenzarTruco(HttpSession sesion, SessionStatus sessionStatus) {
        Usuario usuario = (Usuario) sesion.getAttribute("usuarioActivo");
        if (usuario == null) return new ModelAndView("redirect:/login");

        // Asignamos usuario como jugador
        Jugador jugador1 = new Jugador(usuario.getNombreUsuario());
        Jugador jugador2 = new Jugador("Juan ElComeChancho");
        System.out.println(jugador1.getNombre());
        System.out.println(jugador2.getNombre());

        List<Jugador> jugadores = new ArrayList<>();
        jugadores.add(jugador1);
        jugadores.add(jugador2);
//
        // Empezamos la partida
        servicioTruco.empezar(jugadores);

        // Obtenemos las cartas de los jugadores
        List<Carta> cartasJugador1 = jugador1.getCartas();
        List<Carta> cartasJugador2 = jugador2.getCartas();

        // SOLO PARA DESARROLLO
        List<Carta> todasLasCartas = new ArrayList<>();
        todasLasCartas.addAll(cartasJugador1);
        todasLasCartas.addAll(cartasJugador2);

        // Guardado de sesiones
        sesion.setAttribute("cartasRepartidas", true); // se repartió
        sesion.setAttribute("cartasJugador1", cartasJugador1);
        sesion.setAttribute("cartasJugador2", cartasJugador2);
        sesion.setAttribute("jugador1", jugador1);
        sesion.setAttribute("jugador2", jugador2);
        sesion.setAttribute("partidaIniciada", true);
        sesion.setAttribute("terminada", servicioTruco.saberSiLaManoEstaTerminada());
        sesion.setAttribute("mostrarRespuestasEnvidoJ1", false);
        sesion.setAttribute("mostrarRespuestasEnvidoJ2", false);
        sesion.setAttribute("mostrarRespuestasTrucoJ1", false);
        sesion.setAttribute("mostrarRespuestasTrucoJ2", false);
        sesion.setAttribute("mostrarRespuestasJ1", true);
        sesion.setAttribute("mostrarRespuestasJ2", true);

        // para ver
        sesion.setAttribute("todasLasCartas", todasLasCartas);
        sesion.setAttribute("movimientos", servicioTruco.getMovimientosDeLaManoActual());
        sesion.setAttribute("rondas", servicioTruco.getRondasDeLaManoActual());
        sesion.setAttribute("manos", servicioTruco.getManosJugadas());
        sesion.setAttribute("nroRondas", servicioTruco.getNumeroDeRondasJugadasDeLaManoActual());

        return new ModelAndView("redirect:/partida-truco");
    }


    @RequestMapping(path = "/accion-tirar", method = RequestMethod.POST)
    public ModelAndView accionTirarCarta(
            @RequestParam("cartaId") Long cartaId,
            @RequestParam("jugador") String jugadorNombre,
            HttpSession session) {

        ModelMap model = new ModelMap();

        //if que serviciotrucoSabersillegoa30
        /*if (servicioTruco.ganadorGeneral() != null){

        }*/

        // Obtener jugadores de la sesión
        Jugador jugador1 = (Jugador) session.getAttribute("jugador1");
        Jugador jugador2 = (Jugador) session.getAttribute("jugador2");

        // Redirigir si no existen
        if (jugador1 == null || jugador2 == null) return new ModelAndView("redirect:/home");

        // Determinamos que jugador va
        Jugador actual = jugadorNombre.equalsIgnoreCase(jugador1.getNombre()) ? jugador1 : jugador2;

        // Le damos control al que tiene el turno
        servicioTruco.cambiarTurno(actual);

        // Buscar la carta seleccionada en la mano del jugador
        Carta cartaSeleccionada = getCartaDeLasCartasDelJuegadorPorId(cartaId, actual);

        try {
            // Tirar carta del jugador actual
            servicioTruco.tirarCarta(actual, cartaSeleccionada);
            // Si ya se jugó una ronda, cambia el turno a el jugador que corresponda
            servicioTruco.determinarGanadorRonda(jugador1, jugador2);
        } catch (TrucoException e) {
            // TODO terminar
        }

        // Comprobar si ya no tienen más cartas para terminar la mano
//        if (servicioTruco.saberSiLaManoEstaTerminada()) {
//            servicioTruco.terminarMano();
//        } else {
//            // TODO terminar
//        }

        session.setAttribute("puntosJ1", servicioTruco.getPuntosDeJugador(jugador1));
        session.setAttribute("puntosJ2", servicioTruco.getPuntosDeJugador(jugador2));
        session.setAttribute("terminada", servicioTruco.saberSiLaManoEstaTerminada());

        // Actualizar los jugadores en la sesión para mantener el estado del juego
        session.setAttribute("jugador1", jugador1);
        session.setAttribute("jugador2", jugador2);
        session.setAttribute("cartasTiradasJ1", jugador1.getCartasTiradas());
        session.setAttribute("cartasTiradasJ2", jugador2.getCartasTiradas());
        session.setAttribute("cartasJugador1", jugador1.getCartas());
        session.setAttribute("cartasJugador2", jugador2.getCartas());
        session.setAttribute("jugador1", jugador1);
        session.setAttribute("jugador2", jugador2);
        session.setAttribute("turnoJugador", servicioTruco.getTurnoJugador());
        session.setAttribute("envidoValido", servicioTruco.esLaPrimerRonda());
        session.setAttribute("trucoValido", servicioTruco.esLaPrimerRonda());
        session.setAttribute("envidoValidoJ1", servicioTruco.esLaPrimerRonda());
        session.setAttribute("envidoValidoJ2", servicioTruco.esLaPrimerRonda());

        // Visualización de respuestas
        session.setAttribute("mostrarRespuestasJ1", true);
        session.setAttribute("mostrarRespuestasJ2", true);
        session.setAttribute("mostrarRespuestasEnvidoJ1", false);
        session.setAttribute("mostrarRespuestasEnvidoJ2", false);
        session.setAttribute("mostrarRespuestasTrucoJ1", false);
        session.setAttribute("mostrarRespuestasTrucoJ2", false);

        // para ver como va
        session.setAttribute("movimientos", servicioTruco.getMovimientosDeLaManoActual());
        session.setAttribute("rondas", servicioTruco.getRondasDeLaManoActual());
        session.setAttribute("nroRondas", servicioTruco.getNumeroDeRondasJugadasDeLaManoActual());

        return new ModelAndView("redirect:/partida-truco");
    }


    @RequestMapping(path = "/accion", method = RequestMethod.POST)
    public ModelAndView accion(
            @RequestParam("jugador") String actuadorNombre,
            @RequestParam("accion") String accionValue,
            HttpSession session
    ) {
        Jugador j1 = (Jugador) session.getAttribute("jugador1");
        Jugador j2 = (Jugador) session.getAttribute("jugador2");
        if (j1 == null || j2 == null) return new ModelAndView("redirect:/home");

        Jugador actuador = saberJugadorPorNombre(actuadorNombre, j1, j2);
        if (actuador == null) return new ModelAndView("redirect:/home");
        Jugador receptor = null;

        receptor = actuador.getNombre().equals(j1.getNombre()) ? j2 : j1;

        System.out.println("Recibi acción: " + accionValue);
        Integer nroAccion = servicioTruco.preguntar(accionValue, actuador, receptor);
        String saberAccion = servicioTruco.saberAccion(nroAccion);

        // Utiles
        session.setAttribute("envidoValido", false);
        if (saberAccion.equals("ENVIDO")) {
            Integer tantoJ1 = servicioTruco.calcularTantosDeCartasDeUnJugador(j1);
            Integer tantoJ2 = servicioTruco.calcularTantosDeCartasDeUnJugador(j2);
            session.setAttribute("tantoJ1", tantoJ1);
            session.setAttribute("tantoJ2", tantoJ2);

            // Mostrar respuesta -> quiero/no quiero
            if (actuadorNombre.equalsIgnoreCase(j1.getNombre())) {
                session.setAttribute("mostrarRespuestasJ2", false);
                session.setAttribute("mostrarRespuestasEnvidoJ2", true);
                session.setAttribute("mostrarRespuestasTrucoJ2", false);
            } else {
                session.setAttribute("mostrarRespuestasJ1", false);
                session.setAttribute("mostrarRespuestasEnvidoJ1", true);
                session.setAttribute("mostrarRespuestasTrucoJ1", false);
            }
        }

        if (saberAccion.equals("TRUCO")) {
            // Mostrar respuesta -> quiero/no quiero
            if (actuadorNombre.equalsIgnoreCase(j1.getNombre())) {
                session.setAttribute("mostrarRespuestasJ2", false);
                session.setAttribute("mostrarRespuestasEnvidoJ2", false);
                session.setAttribute("mostrarRespuestasTrucoJ2", true);
            } else {
                session.setAttribute("mostrarRespuestasJ1", false);
                session.setAttribute("mostrarRespuestasEnvidoJ1", false);
                session.setAttribute("mostrarRespuestasTrucoJ1", true);
            }
        }

        // SOLO PARA VER EN DESARROLLO
        session.setAttribute("acciones", servicioTruco.getAcciones());
        session.setAttribute("nroDeAccionAResponder", nroAccion);

        return new ModelAndView("redirect:/partida-truco");
    }

    @RequestMapping(path = "/responder", method = RequestMethod.POST)
    public ModelAndView responder(
            @RequestParam("jugador") String actuadorNombre,
            @RequestParam("respuesta") String respuesta,
            HttpSession session
    ) {
        Jugador j1 = (Jugador) session.getAttribute("jugador1");
        Jugador j2 = (Jugador) session.getAttribute("jugador2");

        if (j1 == null || j2 == null) return new ModelAndView("redirect:/home");

        Jugador actuador = saberJugadorPorNombre(actuadorNombre, j1, j2);
        Jugador receptor = null;
        Integer nroDeAccionAResponder = (int) session.getAttribute("nroDeAccionAResponder");

        receptor = actuador.getNombre().equals(j1.getNombre()) ? j2 : j1;

        // Desarrollo (despues borrar)
        String respuestaDada = saberAccion(respuesta);
        System.out.println("Respondió: '" + respuestaDada + "'.");

        Jugador leTocaResponder = servicioTruco.responder(respuesta, actuador, receptor, nroDeAccionAResponder);
        String saberAccion = servicioTruco.saberAccion(nroDeAccionAResponder);

        if (respuestaDada.equals("QUIERO") || respuestaDada.equals("NO QUIERO")) {
            session.setAttribute("mostrarRespuestasJ1", true);
            session.setAttribute("mostrarRespuestasJ2", true);
            session.setAttribute("mostrarRespuestasTrucoJ1", false);
            session.setAttribute("mostrarRespuestasTrucoJ2", false);
            session.setAttribute("mostrarRespuestasEnvidoJ1", false);
            session.setAttribute("mostrarRespuestasEnvidoJ2", false);
        } else {
            if (leTocaResponder.getNombre().equalsIgnoreCase(j1.getNombre())) {
                if (saberAccion.equals("ENVIDO") ||
                        saberAccion.equals("REAL ENVIDO") ||
                        saberAccion.equals("FALTA ENVIDO")) {
                    session.setAttribute("mostrarRespuestasEnvidoJ1", true);
                    session.setAttribute("mostrarRespuestasEnvidoJ2", false);
                } else if (saberAccion.equals("TRUCO") ||
                        saberAccion.equals("RE TRUCO") ||
                        saberAccion.equals("VALE 4")) {
                    session.setAttribute("mostrarRespuestasTrucoJ1", true);
                    session.setAttribute("mostrarRespuestasTrucoJ2", false);
                } else {
                    // no deberia entrar aca
                    System.out.println("No debí entrar aca, revisame.");
                }
            } else {
                if (saberAccion.equals("ENVIDO") ||
                        saberAccion.equals("REAL ENVIDO") ||
                        saberAccion.equals("FALTA ENVIDO")) {
                    session.setAttribute("mostrarRespuestasEnvidoJ2", true);
                    session.setAttribute("mostrarRespuestasEnvidoJ1", false);
                } else if (saberAccion.equals("TRUCO") ||
                        saberAccion.equals("RE TRUCO") ||
                        saberAccion.equals("VALE 4")) {
                    session.setAttribute("mostrarRespuestasTrucoJ2", true);
                    session.setAttribute("mostrarRespuestasTrucoJ1", false);
                } else {
                    // no deberia entrar aca
                    System.out.println("No debí entrar aca, revisame.");
                }
            }
        }

        // UTILES
        session.setAttribute("puntosJ1", servicioTruco.getPuntosDeJugador(j1));
        session.setAttribute("puntosJ2", servicioTruco.getPuntosDeJugador(j2));

        // SOLO PARA VER EN DESARROLLO
        session.setAttribute("acciones", servicioTruco.getAcciones());
        session.setAttribute("puntosEnJuego", servicioTruco.getPuntosEnJuegoDeAccion(nroDeAccionAResponder));

        return new ModelAndView("redirect:/partida-truco");
    }


    @RequestMapping(path = "/accion-envido", method = RequestMethod.POST)
    public ModelAndView cantarEnvido(
            @RequestParam("jugador") String jugadorNombre,
            @RequestParam("respuesta") String respuesta,
            HttpSession session) {
        Jugador jugador1 = (Jugador) session.getAttribute("jugador1");
        Jugador jugador2 = (Jugador) session.getAttribute("jugador2");
        Jugador cantador = null;
        Jugador receptor = null;

        if (jugador1 == null || jugador2 == null) return new ModelAndView("redirect:/home");

        // Mostrar respuesta -> quiero/no quiero
        if (jugadorNombre.equalsIgnoreCase(jugador1.getNombre())) {
            session.setAttribute("mostrarRespuestasEnvidoJ2", true);
            cantador = jugador1;
            receptor = jugador2;
        } else {
            session.setAttribute("mostrarRespuestasEnvidoJ1", true);
            cantador = jugador2;
            receptor = jugador1;
        }

        servicioTruco.accion("envido", cantador, receptor, 2);
        Integer tantoJ1 = servicioTruco.calcularTantosDeCartasDeUnJugador(jugador1);
        Integer tantoJ2 = servicioTruco.calcularTantosDeCartasDeUnJugador(jugador2);

        // Actualizar los jugadores en la sesión para mantener el estado del juego
        session.setAttribute("jugador1", jugador1);
        session.setAttribute("jugador2", jugador2);
        session.setAttribute("cartasTiradasJ1", jugador1.getCartasTiradas());
        session.setAttribute("cartasTiradasJ2", jugador2.getCartasTiradas());
        session.setAttribute("cartasJugador1", jugador1.getCartas());
        session.setAttribute("cartasJugador2", jugador2.getCartas());
        session.setAttribute("jugador1", jugador1);
        session.setAttribute("jugador2", jugador2);
        session.setAttribute("turnoJugador", servicioTruco.getTurnoJugador());
        session.setAttribute("envidoValido", false);
        session.setAttribute("tantoJ1", tantoJ1);
        session.setAttribute("tantoJ2", tantoJ2);

        // para ver como va
        session.setAttribute("movimientos", servicioTruco.getMovimientosDeLaManoActual());
        session.setAttribute("rondas", servicioTruco.getRondasDeLaManoActual());
        session.setAttribute("nroRondas", servicioTruco.getNumeroDeRondasJugadasDeLaManoActual());

        return new ModelAndView("redirect:/partida-truco");
    }

    @PostMapping("/responderEnvido")
    public ModelAndView responderEnvido(
            @RequestParam("respuesta") String respuesta,
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
                respuestaDada = "ENVIDO";
                break;
            case 3:
                respuestaDada = "REAL ENVIDO";
                break;
            case 4:
                respuestaDada = "FALTA ENVIDO";
                break;
            default:
                return new ModelAndView("redirect:/login");
        }

        return new ModelAndView("redirect:/partida-truco");
    }


    @RequestMapping("/final-partida")
    public ModelAndView finalizarPartida(Jugador jugador, @RequestParam("jugador") String jugadorNombre,
                                         HttpSession session) {
        ModelMap model = new ModelMap();
        model.put("mensaje", "¡" + jugador.getNombre() + " ha ganado la partida!");

        session.setAttribute("jugadas", servicioTruco.getRondasJugadas().size());
        session.setAttribute("rondas", servicioTruco.getRondasJugadas());

        return new ModelAndView("redirect:/home", model);
    }

    private Jugador saberJugadorPorNombre(String nombre, Jugador j1, Jugador j2) {
        if (j1.getNombre().equals(nombre)) return j1;
        if (j2.getNombre().equals(nombre)) return j2;
        return null;
    }

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

    private Carta getCartaDeLasCartasDelJuegadorPorId(Long idCarta, Jugador jugador) {
        for (Carta carta : jugador.getCartas()) {
            if (carta.getId().equals(idCarta)) {
                return carta;
            }
        }
        return null;
    }

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


//    @RequestMapping(path = "/accion-truco", method = RequestMethod.POST)
//    public ModelAndView cantarTruco(
//            @RequestParam("jugador") String jugadorNombre,
//            @RequestParam("respuestaTruco") String respuesta,
//            HttpSession session) {
//        Jugador jugador1 = (Jugador) session.getAttribute("jugador1");
//        Jugador jugador2 = (Jugador) session.getAttribute("jugador2");
//        Jugador cantador = null;
//        Jugador receptor = null;
//
//        if (jugador1 == null || jugador2 == null) return new ModelAndView("redirect:/home");
//
//        // Mostrar respuesta -> quiero/no quiero
//        if(jugadorNombre.equalsIgnoreCase(jugador1.getNombre())){
//            session.setAttribute("mostrarRespuestasTrucoJ2", true);
//            cantador = jugador1;
//            receptor = jugador2;
//        } else {
//            session.setAttribute("mostrarRespuestasTrucoJ1", true);
//            cantador = jugador2;
//            receptor = jugador1;
//        }
//
////        servicioTruco.accion("truco", cantador, receptor);
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
//
//    private String saberAccionTruco (String respuesta) {
//        String respuestaDada = "";
//        switch (Integer.parseInt(respuesta)) {
//            case 0:
//                respuestaDada = "NO QUIERO";
//                break;
//            case 1:
//                respuestaDada = "QUIERO";
//                break;
//            case 2:
//                respuestaDada = "RE TRUCO";
//                break;
//            case 3:
//                respuestaDada = "VALE CUATRO";
//                break;
//            default:
//                return "";
//        }
//        return respuestaDada;
//    }

//   @RequestMapping(path = "/accion-truco", method = RequestMethod.POST)
//    public ModelAndView accionCantarTruco( @RequestParam("jugador") String jugadorNombre,
//                                           HttpSession session) {
//
//        Jugador jugador1 = (Jugador) session.getAttribute("jugador1");
//        Jugador jugador2 = (Jugador) session.getAttribute("jugador2");
//
//        if (jugador1 == null || jugador2 == null) return new ModelAndView("redirect:/home");
//
//        //obtener el jugador que canto el truco
//        Jugador jugadorActual = jugador1.getNombre().equals(jugadorNombre) ? jugador1 : jugador2;
//
//        // Cantar Truco
//        Boolean trucoCantado = servicioTruco.cantarTruco();
//
//        // Actualizar el modelo con los datos de la partida
//        session.setAttribute("jugador1", jugador1);
//        session.setAttribute("jugador2", jugador2);
//        session.setAttribute("turnoJugador", servicioTruco.getTurnoJugador());
//        session.setAttribute("trucoCantado", trucoCantado);
//        session.setAttribute("trucoCantadoPor", jugadorActual.getNombre());
//
//        return new ModelAndView("redirect:/partida-truco");
//    }

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