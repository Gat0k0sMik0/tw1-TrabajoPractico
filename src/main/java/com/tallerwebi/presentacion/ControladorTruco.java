package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.*;
import com.tallerwebi.dominio.excepcion.TrucoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.sound.midi.SysexMessage;
import java.awt.*;
import java.util.Random;


@Controller
public class ControladorTruco {

    private ServicioPartida servicioTruco;
    private ServicioMano servicioMano;
    private ServicioUsuario servicioUsuario;

    @Autowired
    public ControladorTruco(ServicioPartida servicioTruco,
                            ServicioMano servicioMano2,
                            ServicioUsuario servicioUsuario) {
        this.servicioTruco = servicioTruco;
        this.servicioMano = servicioMano2;
        this.servicioUsuario = servicioUsuario;
    }

    @GetMapping("/espera")
    public ModelAndView espera(
            @RequestParam("idJugador") String idJugador,
            @RequestParam("ptsmax") String puntosMaximos,
            HttpSession session
    ) {
        System.out.println("/espera: inicio");


        // Obtener el usuario activo
        Usuario usuarioActivo = servicioUsuario.buscarPorId(Long.parseLong(idJugador));
        if (usuarioActivo == null) return new ModelAndView("redirect:/login");

        Jugador jugador1 = new Jugador();
        jugador1.setNombre(usuarioActivo.getNombreUsuario());
        jugador1.setNumero(1);

        Partida truco = servicioTruco.preparar(jugador1, Integer.parseInt(puntosMaximos));

        System.out.println(truco);

        session.setAttribute("idPartida", truco.getId());

        System.out.println("/espera: fin");
        return new ModelAndView("redirect:/partida-truco");
    }

    @GetMapping("/unirse")
    public ModelAndView unirse(
            @RequestParam("idPartida") String idPartida,
            @RequestParam("idJugador") String idJugador,
            HttpSession session
    ) {
        // Obtener el usuario activo
        Usuario usuarioActivo = servicioUsuario.buscarPorId(Long.parseLong(idJugador));
        if (usuarioActivo == null) return new ModelAndView("redirect:/login");

        Partida truco = servicioTruco.obtenerPartidaPorId(Long.parseLong(idPartida));
        if (truco == null) return new ModelAndView("redirect:/home");

        Jugador jugador2 = new Jugador();
        jugador2.setNombre(usuarioActivo.getNombreUsuario());
        jugador2.setNumero(2);
        servicioTruco.agregarJugador(jugador2, truco);

        session.setAttribute("idPartida", truco.getId());

        return new ModelAndView("redirect:/partida-truco");
    }

    @RequestMapping("/partida-truco")
    public ModelAndView irAPartidaTruco(HttpSession session) {

        System.out.println("partida-truco: incio");
        ModelMap model = new ModelMap();
        Long partidaId = (Long) session.getAttribute("idPartida");

        System.out.println("Recibo id de partida: " + partidaId);

        if (partidaId != null) {
            Partida partida = servicioTruco.obtenerPartidaPorId(partidaId);
            System.out.println("Partida encontrada: ");
            System.out.println(partida);
            if (partida.getJ1() != null && partida.getJ2() != null) {
                if (partida.getPuedeEmpezar()) {
                    if (partida.getGanador() != null) {
                        model.put("ganador", partida.getGanador());
                        return new ModelAndView("partida-truco", model);
                    }

                    Mano mano = servicioMano.obtenerManoPorId(partidaId);

                    model.put("seTermino", mano.getEstaTerminada());

                    Jugador leTocaTirar = servicioMano.saberQuienTiraAhora();

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
                    model.put("partida", partida);
                    model.put("idPartida", partida.getId());
                    model.put("accionAResponder", session.getAttribute("accionAResponder"));

                    model.put("turnoJugador", leTocaTirar.getNumero());
                    model.put("leTocaResponder", mano.getRespondeAhora());
                }
                model.put("partidaIniciada", false);
                model.put("ganador", null);
                model.put("idPartida", partida.getId());
            }
        }
        System.out.println("partida-truco: fin");
        return new ModelAndView("partida-truco", model);
    }

    @GetMapping("/comenzar-truco")
    public ModelAndView comenzarTruco(
            @RequestParam("idPartida") String idPartida,
            HttpSession session) {
        System.out.println("/comenzar-truco: inicio");
        Partida truco = servicioTruco.obtenerPartidaPorId(Long.parseLong(idPartida));
        if (truco == null) return new ModelAndView("redirect:/home");

        System.out.println(truco);

        // Empezamos la partida
        this.servicioTruco.empezar(truco);

        // Empezamos mano
        this.servicioMano.empezar(truco);

        // Guardar IDs en la sesión
        session.setAttribute("idPartida", truco.getId());

        System.out.println("/comenzar-truco: fin");
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
        Mano mano = servicioMano.obtenerManoPorId(truco.getId());
        if (mano == null) return new ModelAndView("redirect:/home");

        // Tiramos carta
        servicioMano.tirarCarta(truco, mano, cartaId, nroJugador);

        // Para saber quien tira la proxima ronda. Si es null, hay parda
        servicioMano.determinarGanadorRonda(truco, mano);

        return new ModelAndView("redirect:/partida-truco");
    }

    @RequestMapping("/cambiar-mano")
    public ModelAndView cambiarMano(
            HttpSession session
    ) {
        Long idPartida = (Long) session.getAttribute("idPartida");
        ModelMap model = new ModelMap();

        Partida partida = servicioTruco.obtenerPartidaPorId(idPartida);
        Mano ultimaMano = servicioMano.obtenerManoPorId(idPartida);
        ultimaMano.getCartasTiradasJ1().clear();
        ultimaMano.getCartasTiradasJ2().clear();
        ultimaMano.setConfirmacionTerminada(true);
        servicioMano.guardar(ultimaMano);
        Mano mano = servicioMano.reset(partida);

        model.put("cartasJugador1", mano.getCartasJ1());
        model.put("cartasJugador2", mano.getCartasJ2());

        model.put("jugador1", partida.getJ1());
        model.put("jugador2", partida.getJ2());

        model.put("cartasTiradasJ1", mano.getCartasTiradasJ1());
        model.put("cartasTiradasJ2", mano.getCartasTiradasJ2());

        model.put("puntosJ1", partida.getPuntosJ1());
        model.put("puntosJ2", partida.getPuntosJ2());

        model.put("mostrarRespuestasEnvidoJ1", mano.getRespondeAhora() != null && mano.getRespondeAhora().getNumero() == 1);
        model.put("mostrarRespuestasEnvidoJ2", mano.getRespondeAhora() != null && mano.getRespondeAhora().getNumero() == 2);

        model.put("mostrarRespuestasTrucoJ1", mano.getRespondeAhora() != null && mano.getRespondeAhora().getNumero() == 1);
        model.put("mostrarRespuestasTrucoJ2", mano.getRespondeAhora() != null && mano.getRespondeAhora().getNumero() == 2);

        model.put("mostrarRespuestasJ1", mano.getRespondeAhora() != null && mano.getRespondeAhora().getNumero() == 1);
        model.put("mostrarRespuestasJ2", mano.getRespondeAhora() != null && mano.getRespondeAhora().getNumero() == 2);

        model.put("puntosParaGanar", partida.getPuntosParaGanar());
        model.put("mano", mano);
        model.put("partida", partida);
        model.put("partidaIniciada", true);
        model.put("accionAResponder", null);

        model.put("turnoJugador", 1);
        model.put("leTocaResponder", mano.getRespondeAhora());

        return new ModelAndView("partida-truco", model);
    }

    @RequestMapping("/salir")
    public ModelAndView cambiarMano() {
        // TODO agregar logica para sumar puntos o algo al que gano (estadistica)
        return new ModelAndView("redirect:/home");
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

    @GetMapping(path = "/crear-partida")
    public ModelAndView crearPartida() {
        return null;
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


}