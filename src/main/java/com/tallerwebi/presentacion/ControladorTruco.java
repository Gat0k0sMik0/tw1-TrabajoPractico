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
        // Obtener el usuario activo
        Usuario usuarioActivo = servicioUsuario.buscarPorId(Long.parseLong(idJugador));
        if (usuarioActivo == null) return new ModelAndView("redirect:/login");

        // SE INSTANCIA LA PARTIDA CON LOS PUNTOS PARA GANAR
        Partida truco = servicioTruco.preparar(usuarioActivo, Integer.parseInt(puntosMaximos));

        session.setAttribute("idPartida", truco.getId());
        session.setAttribute("usuarioActual", usuarioActivo);

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

                Mano mano = servicioMano.obtenerManoPorId(partida.getId());

                if (partida.getGanador() != null) {
                    System.out.println("Hay ganador, guardando todo...");
                    System.out.println("Te muestro la maon para que la veas");
                    System.out.println(mano);
                    model.put("ganador", partida.getGanador());
                    model.put("partidaFinalizada", true);
                    model.put("mano", mano);
                    servicioEstadistica.guardarResultado(partida);
                    return new ModelAndView("partida-truco", model);
                }


                if (mano == null) {
                    System.out.println("La mano es nula...");
                    model.put("respondoYo", false);
                    model.put("respondoEnvido", false);
                    model.put("respondoTruco", false);
                    model.put("partidaIniciada", partida.getPuedeEmpezar() != null ? partida.getPuedeEmpezar() : false);
                    model.put("ganador", partida.getGanador());
                    model.put("idPartida", partida.getId());
                    return new ModelAndView("partida-truco", model);
                }

                // Si el usuario actual es el J1, sino el J2
                if (usuarioActual.getId().equals(partida.getJ1().getUsuario().getId())) {
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
                            mano.getIndicadorTruco(),
                            mano.getPuntosEnJuegoEnvido(),
                            mano.getEstaTerminada())
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
                                    mano.getIndicadorTruco(),
                                    mano.getPuntosEnJuegoEnvido(),
                                    mano.getEstaTerminada()
                            )
                    );
                }

                System.out.println("Respondo yo: ");
                System.out.println(mano.getRespondeAhora() != null
                        && mano.getRespondeAhora().getUsuario().getId().equals(usuarioActual.getId())
                );


                model.put("accionesEnvido", this.filtrarAccionesEnvido(
                        mano.getPuntosEnJuegoEnvido(),
                        servicioMano.esLaPrimerRonda(mano))
                );

                model.put("accionesTruco", this.filtrarAccionesTruco(mano.getIndicadorTruco(), mano.getHayQuiero()));
                model.put("respondoYo",
                        mano.getRespondeAhora() != null
                                && mano.getRespondeAhora().getUsuario().getId().equals(usuarioActual.getId())
                );
                model.put("respondoEnvido", this.saberSiFueEnvido(mano.getUltimaAccionPreguntada()));
                model.put("respondoTruco", this.saberSiFueTruco(mano.getUltimaAccionPreguntada()));
                model.put("meTocaTirar", mano.getTiraAhora().getUsuario().getId().equals(usuarioActual.getId()));
                model.put("tiraAhora", mano.getTiraAhora());

                // Atributos independientes de que jugador es cual
                model.put("seTermino", mano.getEstaTerminada());
                model.put("puntosParaGanar", partida.getPuntosParaGanar());
                model.put("mano", mano);
                model.put("partida", partida);
                model.put("idPartida", partida.getId());
                model.put("accionAResponder", mano.getUltimaAccionPreguntada());


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
        System.out.println("comenzar-truco: busco una partida con id: " + idPartida);
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
            mano.getCartasTiradasJ1().clear();
            mano.getCartasTiradasJ2().clear();
            mano.getCartasJ1().clear();
            mano.getCartasJ2().clear();
            System.out.println("Termino la partida, hay ganador, la mano quedá así: ");
            System.out.println(mano);
            servicioMano.guardar(mano);
        } else if (partida.getPuntosJ2() >= partida.getPuntosParaGanar()) {
            servicioTruco.finalizarPartida(idPartida, partida.getJ2());
            mano.getCartasTiradasJ1().clear();
            mano.getCartasTiradasJ2().clear();
            mano.getCartasJ1().clear();
            mano.getCartasJ2().clear();
            System.out.println("Termino la partida, hay ganador, la mano quedá así: ");
            System.out.println(mano);
            servicioMano.guardar(mano);
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
        servicioMano.limpiarMano(ultimaMano);
        Mano mano = servicioMano.reset(partida);

        Usuario usuarioActual = (Usuario) session.getAttribute("usuarioActual");

        if (usuarioActual == null) return new ModelAndView("redirect:/login");
        if (idPartida == null) return new ModelAndView("redirect:/home");
        if (partida == null) return new ModelAndView("redirect:/home");

        if (partida.getJ1() != null && partida.getJ2() != null) {

            if (partida.getPuedeEmpezar()) {

                if (partida.getGanador() != null) {
                    model.put("ganador", partida.getGanador());
                    model.put("partidaFinalizada", true);
                    servicioEstadistica.guardarResultado(partida);
                    mano.getCartasTiradasJ1().clear();
                    mano.getCartasTiradasJ2().clear();
                    mano.getCartasJ1().clear();
                    mano.getCartasJ2().clear();
                    mano.setConfirmacionTerminada(true);
                    System.out.println("Termino la partida, hay ganador, la mano quedá así: ");
                    System.out.println(mano);
                    servicioMano.guardar(mano);
                    return new ModelAndView("partida-truco", model);
                }

                if (mano == null) {
                    model.put("respondoYo", false);
                    model.put("respondoEnvido", false);
                    model.put("respondoTruco", false);
                    model.put("partidaIniciada", partida.getPuedeEmpezar() != null ? partida.getPuedeEmpezar() : false);
                    model.put("ganador", partida.getGanador());
                    model.put("idPartida", partida.getId());
                    return new ModelAndView("partida-truco", model);
                }

                // Si el usuario actual es el J1, sino el J2
                if (usuarioActual.getId().equals(partida.getJ1().getUsuario().getId())) {
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
                            mano.getIndicadorTruco(),
                            mano.getPuntosEnJuegoEnvido(),
                            mano.getEstaTerminada())
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
                                    mano.getIndicadorTruco(),
                                    mano.getPuntosEnJuegoEnvido(),
                                    mano.getEstaTerminada()
                            )
                    );
                }

                model.put("accionesEnvido", this.filtrarAccionesEnvido(
                        mano.getPuntosEnJuegoEnvido(),
                        servicioMano.esLaPrimerRonda(mano))
                );
                model.put("accionesTruco", this.filtrarAccionesTruco(mano.getIndicadorTruco(), mano.getHayQuiero()));
                model.put("respondoYo",
                        mano.getRespondeAhora() != null
                                && mano.getRespondeAhora().getUsuario().getId().equals(usuarioActual.getId())
                );
                model.put("respondoEnvido", this.saberSiFueEnvido(mano.getUltimaAccionPreguntada()));
                model.put("respondoTruco", this.saberSiFueTruco(mano.getUltimaAccionPreguntada()));
                model.put("meTocaTirar", mano.getTiraAhora().getUsuario().getId().equals(usuarioActual.getId()));
                model.put("tiraAhora", mano.getTiraAhora());


                // Atributos independientes de que jugador es cual
                model.put("seTermino", mano.getEstaTerminada());

                if (mano.getEstaTerminada()) {
                    model.put("respondoYo", false);
                }

                model.put("puntosParaGanar", partida.getPuntosParaGanar());
                model.put("mano", mano);
                model.put("partida", partida);
                model.put("idPartida", partida.getId());
                model.put("accionAResponder", mano.getUltimaAccionPreguntada());

                model.put("partidaIniciada", partida.getPuedeEmpezar() != null ? partida.getPuedeEmpezar() : false);

                return new ModelAndView("partida-truco", model);
            }

            model.put("ganador", partida.getGanador());
            model.put("idPartida", partida.getId());

            model.put("respondoYo", false);
            model.put("respondoEnvido", false);
            model.put("respondoTruco", false);
        }

        model.put("respondoYo", false);
        model.put("respondoEnvido", false);
        model.put("respondoTruco", false);
        model.put("partidaIniciada", partida.getPuedeEmpezar() != null ? partida.getPuedeEmpezar() : false);

        return new ModelAndView("partida-truco", model);
    }

    @RequestMapping("/salir")
    public ModelAndView salirDeLaPartida(
            @RequestParam("mano") String manoId
    ) {
        Mano mano = servicioMano.obtenerManoPorId(Long.parseLong(manoId));
        if (mano == null) return new ModelAndView("redirect:/home");
        System.out.println("Termino la partida, hay ganador, la mano quedá así antes de SALIR: ");
        System.out.println(mano);
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
        servicioMano.responder(mano, accionAlCualResponde, nroRespuesta, Integer.parseInt(nroJugador));


        return new ModelAndView("redirect:/partida-truco");
    }

    private List<Accion> filtrarAccionesNormales(
            Boolean tengoFlor,
            Integer indicadorTruco,
            Integer puntosEnJuegoEnvido,
            Boolean terminoLaMano) {
        System.out.println("---FiltrarAccionesNormales: INICIO");
        System.out.println("tengoFlor: " + tengoFlor);
        System.out.println("indicadorTruco: " + indicadorTruco);
        System.out.println("puntosEnJuegoEnvido: " + puntosEnJuegoEnvido);
        System.out.println("terminoLaMano: " + terminoLaMano);
        System.out.println("---FiltrarAccionesNormales: FIN");
        List<Integer> valoresEnvido = Arrays.asList(98, 99);
        return getAccionesNormales().stream()
                .filter(accion -> accion.getNro() != 5
                        || (indicadorTruco == 0 && valoresEnvido.contains(puntosEnJuegoEnvido)))
                .filter(accion -> accion.getNro() != 8 || tengoFlor && puntosEnJuegoEnvido != 98)
                .filter(accion -> accion.getNro() != 9 || !terminoLaMano)
                .collect(Collectors.toList());

    }

    private List<Accion> filtrarAccionesTruco(Integer indicadorTruco, Boolean hayQuiero) {
        System.out.println("---FiltrarAccionesTruco: INICIO");
        System.out.println("indicadorTruco: " + indicadorTruco);
        System.out.println("hayQuiero: " + hayQuiero);
        System.out.println("---FiltrarAccionesTruco: FIN");
        List<Integer> valoresQuiero = Arrays.asList(1, 2, 3);
        List<Integer> valoresNoQuiero = Arrays.asList(1, 2, 3);
        List<Integer> valoresReTruco = Arrays.asList(1, 3);
        return getAccionesTruco().stream()
                .filter(accion -> accion.getNro() != 0 || (valoresNoQuiero.contains(indicadorTruco) && !hayQuiero))
                .filter(accion -> accion.getNro() != 1 || (valoresQuiero.contains(indicadorTruco) && !hayQuiero))
                .filter(accion -> accion.getNro() != 6 || (hayQuiero || valoresReTruco.contains(indicadorTruco)))
                .filter(accion -> accion.getNro() != 7 || (!hayQuiero && indicadorTruco == 2))
                .collect(Collectors.toList());
    }

    private List<Accion> filtrarAccionesEnvido(Integer indicadorEnvido, Boolean puedoCantarEnvido) {
        System.out.println("---filtrarAccionesEnvido: INICIO");
        System.out.println("Me llegan puntos en juego del envido: " + indicadorEnvido);
        System.out.println("puedoCantarEnvido: " + puedoCantarEnvido);
        System.out.println("---filtrarAccionesEnvido: FIN");

        List<Integer> valoresFaltaEnvido = Arrays.asList(-1, 2, 3, 4, 5, 7, 99);
        List<Integer> valoresRealEnvido = Arrays.asList(2, 3, 4, 5, 99);
        List<Integer> valoresEnvido = Arrays.asList(2, 99);
        List<Integer> valoresQuieroNoQuiero = Arrays.asList(-1, 2, 3, 4, 5, 7);

        return getAccionesEnvido().stream()
                .filter(accion ->
                        accion.getNro() != 0 || valoresQuieroNoQuiero.contains(indicadorEnvido))
                .filter(accion ->
                        accion.getNro() != 1 || valoresQuieroNoQuiero.contains(indicadorEnvido))
                .filter(accion ->
                        accion.getNro() != 2 || (valoresEnvido.contains(indicadorEnvido) && puedoCantarEnvido))
                .filter(accion ->
                        accion.getNro() != 3 || (valoresRealEnvido.contains(indicadorEnvido) && puedoCantarEnvido))
                .filter(accion ->
                        accion.getNro() != 4 || (valoresFaltaEnvido.contains(indicadorEnvido) && puedoCantarEnvido))
                .collect(Collectors.toList());
    }

    private Boolean saberSiFueEnvido(Integer ultimaAccionPreguntada) {
        System.out.println("Saber si fue envido: " + ultimaAccionPreguntada);
        List<Integer> ve = Arrays.asList(2, 3, 4, 99);
        return ve.contains(ultimaAccionPreguntada);
    }

    private Boolean saberSiFueTruco(Integer ultimaAccionPreguntada) {
        List<Integer> ve = Arrays.asList(2, 5, 6, 7);
        return ve.contains(ultimaAccionPreguntada);
    }


    private List<Accion> getAccionesNormales() {
        List<Accion> acciones = new ArrayList<>();
        Accion truco = new Accion(5, "Truco");
        Accion flor = new Accion(8, "Flor");
        Accion mazo = new Accion(9, "Mazo");
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