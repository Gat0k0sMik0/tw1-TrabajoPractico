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

        ModelMap model = new ModelMap();
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
        model.put("mostrarRespuestasJ1", session.getAttribute("mostrarRespuestasJ1"));
        model.put("mostrarRespuestasJ2", session.getAttribute("mostrarRespuestasJ2"));
        // handle de envido
        model.put("responde", session.getAttribute("responde"));

        // Para ver como va
        model.put("rondas", session.getAttribute("rondas"));
        model.put("manos", session.getAttribute("manos"));
        model.put("movimientos", session.getAttribute("movimientos"));
        model.put("nroRondas", session.getAttribute("nroRondas"));
        model.put("envidoValido", session.getAttribute("envidoValido"));
        model.put("tantoJ1", session.getAttribute("tantoJ1"));
        model.put("tantoJ2", session.getAttribute("tantoJ2"));
        model.put("acciones", session.getAttribute("acciones"));

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
        sesion.setAttribute("mostrarRespuestasJ1", false);
        sesion.setAttribute("mostrarRespuestasJ2", false);

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
        servicioTruco.cambiarTurno(actual); // le damos control al que tiene el turno

        // Buscar la carta seleccionada en la mano del jugador
        Carta cartaSeleccionada = getCartaDeLasCartasDelJuegadorPorId(cartaId, actual);

        // Tirar carta del jugador actual
        servicioTruco.tirarCarta(actual, cartaSeleccionada);

        // Si ya se jugó una ronda, cambia el turno a el jugador que corresponda
        servicioTruco.determinarGanadorRonda(jugador1, jugador2);

        try  {
            servicioTruco.tirarCarta(actual, cartaSeleccionada);
            session.setAttribute("terminada", servicioTruco.saberSiLaManoEstaTerminada());
        } catch (TrucoException e) {
            session.setAttribute("terminada", servicioTruco.saberSiLaManoEstaTerminada());
        }

        // Inyección al modelo
//        model.put("rondas", servicioTruco.getRondasDeLaManoActual());
//        model.put("movimientos", servicioTruco.getMovimientosDeLaManoActual());

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
        session.setAttribute("mostrarRespuestasJ1", false);
        session.setAttribute("mostrarRespuestasJ2", false);

        // para ver como va
        session.setAttribute("movimientos", servicioTruco.getMovimientosDeLaManoActual());
        session.setAttribute("rondas", servicioTruco.getRondasDeLaManoActual());
        session.setAttribute("nroRondas", servicioTruco.getNumeroDeRondasJugadasDeLaManoActual());

        return new ModelAndView("redirect:/partida-truco");
    }



    @RequestMapping(path = "/accion", method = RequestMethod.POST)
    public ModelAndView accion (
            @RequestParam("jugador") String actuadorNombre,
            @RequestParam("accion") String accionValue,
            HttpSession session
    ) {
        Jugador j1 = (Jugador) session.getAttribute("jugador1");
        Jugador j2 = (Jugador) session.getAttribute("jugador2");
        if (j1 == null || j2 == null) return new ModelAndView("redirect:/home");

        Jugador actuador = saberJugadorPorNombre(actuadorNombre, j1, j2);
        Jugador receptor = null;

        if (actuador.getNombre().equals(j1.getNombre())) {
            receptor = j2;
        } else {
            receptor = j1;
        }

        servicioTruco.accion(accionValue, actuador, receptor);

        if (accionValue.equals("ENVIDO")) {
            Integer tantoJ1 = servicioTruco.calcularTantosDeCartasDeUnJugador(j1);
            Integer tantoJ2 = servicioTruco.calcularTantosDeCartasDeUnJugador(j2);
            session.setAttribute("tantoJ1", tantoJ1);
            session.setAttribute("tantoJ2", tantoJ2);

            // Mostrar respuesta -> quiero/no quiero
            if(actuadorNombre.equalsIgnoreCase(j1.getNombre())){
                session.setAttribute("mostrarRespuestasJ2", true);
            } else {
                session.setAttribute("mostrarRespuestasJ1", true);
            }
        }

        session.setAttribute("envidoValido", false);


        // SOLO PARA VER EN DESARROLLO
        session.setAttribute("acciones", servicioTruco.getAcciones());

        return new ModelAndView("redirect:/partida-truco");
    }

    @RequestMapping(path = "/responder", method = RequestMethod.POST)
    public ModelAndView responder (
            @RequestParam("jugador") String actuadorNombre,
            @RequestParam("accion") String accionValue,
            HttpSession session
    ) {
        Jugador j1 = (Jugador) session.getAttribute("jugador1");
        Jugador j2 = (Jugador) session.getAttribute("jugador2");

        if (j1 == null || j2 == null) return new ModelAndView("redirect:/home");

        String accion = saberAccionEnvido(accionValue);
        Jugador actuador = saberJugadorPorNombre(actuadorNombre, j1, j2);
        Jugador receptor = null;

        if (actuador.getNombre().equals(j1.getNombre())) {
            receptor = j2;
        } else {
            receptor = j1;
        }

        servicioTruco.accion(accion, actuador, receptor);




        // SOLO PARA VER EN DESARROLLO
        session.setAttribute("acciones", servicioTruco.getAcciones());

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
        if(jugadorNombre.equalsIgnoreCase(jugador1.getNombre())){
            session.setAttribute("mostrarRespuestasJ2", true);
            cantador = jugador1;
            receptor = jugador2;
        } else {
            session.setAttribute("mostrarRespuestasJ1", true);
            cantador = jugador2;
            receptor = jugador1;
        }

        servicioTruco.accion("envido", cantador, receptor);
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
            @RequestParam("jugador") String jugadorNombre ) {

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

    private Jugador saberJugadorPorNombre (String nombre, Jugador j1, Jugador j2) {
        if (j1.getNombre().equals(nombre)) return j1;
        if (j2.getNombre().equals(nombre)) return j2;
        return null;
    }
    private String saberAccionEnvido (String respuesta) {
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



}