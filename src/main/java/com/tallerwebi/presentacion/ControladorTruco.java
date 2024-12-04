package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.stream.Collectors;


@Controller
public class ControladorTruco {

    private final ServicioPartida servicioTruco;
    private final ServicioMano servicioMano;
    private final ServicioUsuario servicioUsuario;
    private final ServicioEstadisticas servicioEstadistica;

    @Autowired
    public ControladorTruco(ServicioPartida servicioTruco,
                            ServicioMano servicioMano,
                            ServicioUsuario servicioUsuario,
                            ServicioEstadisticas servicioEstadistica) {
        this.servicioTruco = servicioTruco;
        this.servicioMano = servicioMano;
        this.servicioUsuario = servicioUsuario;
        this.servicioEstadistica = servicioEstadistica;
    }

    @GetMapping("/espera")
    public ModelAndView espera(
            @RequestParam("idJugador") String idJugador,
            @RequestParam("ptsmax") String puntosMaximos,
            HttpSession session
    ) {
        System.out.println("/espera: ejecutado");

        // Obtener el usuario activo
        Usuario usuarioActivo = servicioUsuario.buscarPorId(Long.parseLong(idJugador));
        if (usuarioActivo == null) return new ModelAndView("redirect:/login");

        // SE INSTANCIA LA PARTIDA CON LOS PUNTOS PARA GANAR
        Partida truco = servicioTruco.preparar(usuarioActivo, Integer.parseInt(puntosMaximos));

        session.setAttribute("idPartida", truco.getId());
        session.setAttribute("usuarioActual", usuarioActivo);

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

        servicioTruco.agregarJugador(usuarioActivo, truco);

        session.setAttribute("idPartida", truco.getId());

        return new ModelAndView("redirect:/partida-truco");
    }

    @RequestMapping("/partida-truco")
    public ModelAndView irAPartidaTruco(HttpSession session) {

        System.out.println("partida-truco: ejecutado");
        ModelMap model = new ModelMap();

        Long partidaId = (Long) session.getAttribute("idPartida");
        Usuario usuarioActual = (Usuario) session.getAttribute("usuarioActual");

        System.out.println("partida-truco: usuarioActual " + usuarioActual);
        System.out.println("partida-truco: idPartida de session " + partidaId);

        if (usuarioActual == null) return new ModelAndView("redirect:/login");
        if (partidaId == null) return new ModelAndView("redirect:/home");

        System.out.println("partida-truco: Recibo id de partida: " + partidaId);
        Partida partida = servicioTruco.obtenerPartidaPorId(partidaId);
        if (partida == null) return new ModelAndView("redirect:/home");

        System.out.println("partida-truco: Partida encontrada: ");
        System.out.println(partida);

        if (partida.getJ1() != null && partida.getJ2() != null) {

            if (partida.getPuedeEmpezar()) {

                if (partida.getGanador() != null) {
                    model.put("ganador", partida.getGanador());
                    model.put("partidaFinalizada", true);
                    servicioEstadistica.guardarResultado(partida);
                    return new ModelAndView("partida-truco", model);
                }

                Mano mano = servicioMano.obtenerManoPorId(partida.getId());
                System.out.println("partida-truco: esta es la mano actual");
                System.out.println(mano);

                if (mano == null) {
                    model.put("respondoYo", false);
                    model.put("respondoEnvido", false);
                    model.put("respondoTruco", false);
                    System.out.println("Partida puede empezar? " + (partida.getPuedeEmpezar() ? "Sí" : "Nó"));
                    model.put("partidaIniciada", partida.getPuedeEmpezar() != null ? partida.getPuedeEmpezar() : false);
                    model.put("ganador", partida.getGanador());
                    model.put("idPartida", partida.getId());
                    return new ModelAndView("partida-truco", model);
                }

                // Si el usuario actual es el J1, sino el J2
                if (usuarioActual.getId().equals(partida.getJ1().getId())) {
                    model.put("yo", partida.getJ1());
                    model.put("rival", partida.getJ2());
                    model.put("misPuntos", partida.getPuntosJ1());
                    model.put("puntosRival", partida.getPuntosJ2());
                    model.put("misCartas", mano.getCartasJ1());
                    model.put("misCartasTiradas", mano.getCartasTiradasJ1());
                    model.put("cartasOponente", mano.getCartasJ2());
                    model.put("cartasOponenteTiradas", mano.getCartasTiradasJ2());
                    model.put("miNumero", partida.getJ1().getNumero());
                    model.put("tengoFlor", servicioMano.tieneFlor(partida.getJ1(), mano));
                    model.put("accionesNormales", this.filtrarAccionesNormales(
                            servicioMano.tieneFlor(partida.getJ1(), mano),
                            servicioMano.esLaPrimerRonda(mano),
                            mano.getIndicadorTruco())
                    );
                } else {
                    model.put("yo", partida.getJ2());
                    model.put("rival", partida.getJ1());
                    model.put("misPuntos", partida.getPuntosJ2());
                    model.put("puntosRival", partida.getPuntosJ1());
                    model.put("misCartas", mano.getCartasJ2());
                    model.put("misCartasTiradas", mano.getCartasTiradasJ2());
                    model.put("cartasOponente", mano.getCartasJ1());
                    model.put("cartasOponenteTiradas", mano.getCartasTiradasJ1());
                    model.put("miNumero", partida.getJ2().getNumero());
                    model.put("tengoFlor", servicioMano.tieneFlor(partida.getJ2(), mano));
                    model.put("accionesNormales", this.filtrarAccionesNormales(
                            servicioMano.tieneFlor(partida.getJ2(), mano),
                            servicioMano.esLaPrimerRonda(mano),
                            mano.getIndicadorTruco())
                    );
                }

                // Manejo de botones
                System.out.println("Puntos en juego del envido: " + mano.getPuntosEnJuegoEnvido());
                model.put("accionesEnvido", this.filtrarAccionesEnvido(mano.getPuntosEnJuegoEnvido()));
                model.put("accionesTruco", this.filtrarAccionesTruco(mano.getIndicadorTruco()));

                model.put("respondoYo", mano.getRespondeAhora() != null && mano.getRespondeAhora().getId().equals(usuarioActual.getId()));
                model.put("respondoEnvido", this.saberSiFueEnvido(mano.getUltimaAccionPreguntada()));
                model.put("respondoTruco", this.saberSiFueTruco(mano.getUltimaAccionPreguntada()));
                // ---

                model.put("meTocaTirar", mano.getTiraAhora().getId().equals(usuarioActual.getId()));
                model.put("tiraAhora", mano.getTiraAhora());

                // Atributos independientes de que jugador es cual
                model.put("seTermino", mano.getEstaTerminada());
                model.put("puntosParaGanar", partida.getPuntosParaGanar());
                model.put("mano", mano);
                model.put("partida", partida);
                model.put("idPartida", partida.getId());
                model.put("accionAResponder", mano.getUltimaAccionPreguntada());

                System.out.println("Info de mano: " + mano);
                System.out.println("Usuario actual: " + usuarioActual.getId());



                model.put("partidaIniciada", partida.getPuedeEmpezar() != null ? partida.getPuedeEmpezar() : false);

                return new ModelAndView("partida-truco", model);
            }

            model.put("ganador", partida.getGanador());
            model.put("idPartida", partida.getId());

            model.put("respondoYo", false);
            model.put("respondoEnvido", false);
            model.put("respondoTruco", false);
            System.out.println("Partida puede empezar? " + (partida.getPuedeEmpezar() ? "Sí" : "Nó"));
        }

        model.put("respondoYo", false);
        model.put("respondoEnvido", false);
        model.put("respondoTruco", false);
        model.put("partidaIniciada", partida.getPuedeEmpezar() != null ? partida.getPuedeEmpezar() : false);

        System.out.println("partida-truco: fin");
        return new ModelAndView("partida-truco", model);
    }


    @GetMapping("/comenzar-truco")
    public ModelAndView comenzarTruco(
            @RequestParam("idPartida") String idPartida,
            HttpSession session) {
        System.out.println("/comenzar-truco: inicio");

        // Obtén la partida por su ID
        Partida partida = servicioTruco.obtenerPartidaPorId(Long.parseLong(idPartida));

        // Si no existe la partida, redirige a /home
        if (partida == null) {
            System.out.println("Partida no encontrada");
            return new ModelAndView("redirect:/home");
        }

        // Empezamos la partida y la mano
        servicioTruco.empezar(partida);
        servicioMano.empezar(partida);

        // Guarda el ID de la partida en la sesión
        session.setAttribute("idPartida", partida.getId());

        System.out.println("Partida iniciada: " + partida);
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
        Partida partida = servicioTruco.obtenerPartidaPorId(idPartida);
        if (partida == null) return new ModelAndView("redirect:/home");

        // Buscar id_mano de parametro en BD
        Mano mano = servicioMano.obtenerManoPorId(partida.getId());
        if (mano == null) return new ModelAndView("redirect:/home");

        // Tiramos carta
        servicioMano.tirarCarta(partida, mano, cartaId, nroJugador);

        // Para saber quien tira la proxima ronda. Si es null, hay parda
        servicioMano.determinarGanadorRonda(partida, mano);

        // Verificar si debe finalizarse
        if (partida.getPuntosJ1() >= partida.getPuntosParaGanar()) {
            servicioTruco.finalizarPartida(idPartida, partida.getJ1());
        } else if (partida.getPuntosJ2() >= partida.getPuntosParaGanar()) {
            servicioTruco.finalizarPartida(idPartida, partida.getJ2());
        }

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

        model.put("mostrarRespuestasEnvidoJ1", mano.getRespondeAhora());
        model.put("mostrarRespuestasEnvidoJ2", mano.getRespondeAhora());

        model.put("mostrarRespuestasTrucoJ1", mano.getRespondeAhora());
        model.put("mostrarRespuestasTrucoJ2", mano.getRespondeAhora());

        model.put("mostrarAccionesJ1", mano.getRespondeAhora() != null && mano.getRespondeAhora().getNumero() == 1);
        model.put("mostrarAccionesJ2", mano.getRespondeAhora() != null && mano.getRespondeAhora().getNumero() == 2);

        model.put("florValidaJ1", servicioMano.tieneFlor(partida.getJ1(), mano));
        model.put("florValidaJ2", servicioMano.tieneFlor(partida.getJ2(), mano));
        model.put("envidoValido", servicioMano.esLaPrimerRonda(mano));

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

        servicioMano.preguntar(mano, accionValue, Integer.parseInt(nroJugador));

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

    private List<Accion> filtrarAccionesNormales(Boolean tengoFlor, Boolean puedoCantarEnvido, Integer indicadorTruco) {
        return getAccionesNormales().stream()
                .filter(accion -> accion.getNro() != 8 || tengoFlor)
                .filter(accion -> accion.getNro() != 2 || puedoCantarEnvido)
                .filter(accion -> accion.getNro() != 5 || indicadorTruco == 0)
                .collect(Collectors.toList());

    }

    private List<Accion> filtrarAccionesTruco(Integer indicadorTruco) {
        return getAccionesTruco().stream()
                .filter(accion -> accion.getNro() != 6 || indicadorTruco == 1)
                .filter(accion -> accion.getNro() != 7 || indicadorTruco == 2)
                .collect(Collectors.toList());
    }

    private List<Accion> filtrarAccionesEnvido(Integer indicadorEnvido) {
        List<Integer> valoresRealEnvido = Arrays.asList(3, 5, 7);
        List<Integer> valoresEnvido = Arrays.asList(2, 4);
        return getAccionesEnvido().stream()
                .filter(accion -> accion.getNro() == 3 && valoresRealEnvido.contains(indicadorEnvido))
                .filter(accion -> accion.getNro() == 2 && valoresEnvido.contains(indicadorEnvido))
                .collect(Collectors.toList());
    }

    private Boolean saberSiFueEnvido(Integer ultimaAccionPreguntada) {
        System.out.println("Saber si fue envido: " + ultimaAccionPreguntada);
        List<Integer> ve = Arrays.asList(2, 3, 4);
        return ve.contains(ultimaAccionPreguntada);
    }

    private Boolean saberSiFueTruco(Integer ultimaAccionPreguntada) {
        List<Integer> ve = Arrays.asList(5, 6, 7);
        return ve.contains(ultimaAccionPreguntada);
    }


    private List<Accion> getAccionesNormales() {
        List<Accion> acciones = new ArrayList<>();
        Accion envido = new Accion(2, "Envido");
        Accion truco = new Accion(5, "Truco");
        Accion flor = new Accion(8, "Flor");
        Accion mazo = new Accion(9, "Mazo");
        acciones.add(envido);
        acciones.add(truco);
        acciones.add(flor);
        acciones.add(mazo);
        return acciones;
    }

    private List<Accion> getAccionesEnvido() {
        List<Accion> acciones = new ArrayList<>();
        Accion q = new Accion(1, "Quiero");
        Accion nq = new Accion(0, "No quiero");
        Accion e = new Accion(2, "Envido");
        Accion re = new Accion(3, "Real envido");
        Accion fe = new Accion(4, "Falta envido");
        acciones.add(q);
        acciones.add(nq);
        acciones.add(e);
        acciones.add(re);
        acciones.add(fe);
        return acciones;
    }

    private List<Accion> getAccionesTruco() {
        List<Accion> acciones = new ArrayList<>();
        Accion q = new Accion(1, "Quiero");
        Accion nq = new Accion(0, "No quiero");
        Accion rt = new Accion(6, "Re truco");
        Accion v4 = new Accion(7, "Vale 4");
        acciones.add(q);
        acciones.add(nq);
        acciones.add(rt);
        acciones.add(v4);
        return acciones;
    }

}