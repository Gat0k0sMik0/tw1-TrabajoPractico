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
    @Autowired
    private ServicioTrucoImpl servicioTrucoImpl;

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

        // Para ver como va
        model.put("rondas", session.getAttribute("rondas"));
        model.put("manos", session.getAttribute("manos"));
        model.put("movimientos", session.getAttribute("movimientos"));
        model.put("nroRondas", session.getAttribute("nroRondas"));
        model.put("envidoValido", session.getAttribute("envidoValido"));

        return new ModelAndView("partida-truco", model);
    }

    @RequestMapping("/comenzar-truco")
    public ModelAndView comenzarTruco(HttpSession sesion, SessionStatus sessionStatus) {
        Usuario usuario = (Usuario) sesion.getAttribute("usuarioActivo");
        if (usuario == null) return new ModelAndView("redirect:/login");

        // Asignamos usuario como jugador
        Jugador jugador1 = new Jugador(usuario.getNombreUsuario());
        Jugador jugador2 = new Jugador("Juan ElComeChancho");

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
        sesion.setAttribute("todasLasCartas", todasLasCartas); // SOLO PARA DESARROLLO
        sesion.setAttribute("partidaIniciada", true);
        sesion.setAttribute("terminada", servicioTruco.saberSiLaManoEstaTerminada());
        sesion.setAttribute("mostrarRespuestasJ1", false);
        sesion.setAttribute("mostrarRespuestasJ2", false);

        // para ver
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

    private Carta getCartaDeLasCartasDelJuegadorPorId(Long idCarta, Jugador jugador) {
        for (Carta carta : jugador.getCartas()) {
            if (carta.getId().equals(idCarta)) {
                return carta;
            }
        }
        return null;
    }

    @RequestMapping(path = "/accion-envido", method = RequestMethod.POST)
    public ModelAndView cantarEnvido(
            @RequestParam("jugador") String jugadorNombre,
            HttpSession session) {
        Jugador jugador1 = (Jugador) session.getAttribute("jugador1");
        Jugador jugador2 = (Jugador) session.getAttribute("jugador2");

        if (jugador1 == null || jugador2 == null) return new ModelAndView("redirect:/home");

        if(jugadorNombre.equalsIgnoreCase(jugador1.getNombre())){
            session.setAttribute("mostrarRespuestasJ2", true);
        } else {
            session.setAttribute("mostrarRespuestasJ1", true);
        }

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

        // para ver como va
        session.setAttribute("movimientos", servicioTruco.getMovimientosDeLaManoActual());
        session.setAttribute("rondas", servicioTruco.getRondasDeLaManoActual());
        session.setAttribute("nroRondas", servicioTruco.getNumeroDeRondasJugadasDeLaManoActual());

        return new ModelAndView("redirect:/partida-truco");
    }

    @PostMapping("/responderEnvido")
    public ModelAndView responderEnvido(@RequestParam String respuesta, HttpSession session,
                                        @RequestParam("jugador") String jugadorNombre ) {

        Jugador jugador1 = (Jugador) session.getAttribute("jugador1");
        Jugador jugador2 = (Jugador) session.getAttribute("jugador2");

        Jugador actual = null;
        if(jugadorNombre.equalsIgnoreCase(jugador1.getNombre())){
            actual = jugador1;
        } else {
            actual = jugador2;
        }

        /*if (respuesta.equals("quiero")) {
            servicioTruco.verificarEnvido(jugador1, jugador2);
        } else {
            SumarPuntosJugadorContrario();
        }*/

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

        // para ver como va
        session.setAttribute("movimientos", servicioTruco.getMovimientosDeLaManoActual());
        session.setAttribute("rondas", servicioTruco.getRondasDeLaManoActual());
        session.setAttribute("nroRondas", servicioTruco.getNumeroDeRondasJugadasDeLaManoActual());

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


}