package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.*;
import com.tallerwebi.infraestructura.ServicioEstadisticasImpl;
import com.tallerwebi.infraestructura.ServicioPartidaImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
            @RequestParam("idJugador") Long idUsuario,
            @RequestParam("ptsmax") Integer puntosMaximos
    ) {
        // Obtener el usuario activo
        Usuario usuarioActivo = servicioUsuario.buscarPorId(idUsuario);
        if (usuarioActivo == null) return new ModelAndView("redirect:/login");

        // SE INSTANCIA LA PARTIDA CON LOS PUNTOS PARA GANAR
        Partida truco = servicioTruco.preparar(usuarioActivo, puntosMaximos);

        // Redirigir a la vista de la partida con el ID de la partida en la URL
        return new ModelAndView("redirect:/partida-truco?idPartida=" + truco.getId() + "&idUsuario=" + usuarioActivo.getId());
    }

    @GetMapping("/unirse")
    public ModelAndView unirse(
            @RequestParam("idPartida") Long idPartida,
            @RequestParam("idUsuario") Long idUsuario
    ) {
        // Obtener el usuario activo
        Usuario usuarioActivo = servicioUsuario.buscarPorId(idUsuario);
        if (usuarioActivo == null) return new ModelAndView("redirect:/login");

        // Buscar la partida
        Partida truco = servicioTruco.obtenerPartidaPorId(idPartida);
        if (truco == null) return new ModelAndView("redirect:/home");

        servicioTruco.agregarJugador(usuarioActivo, truco);

        // Redirigir a la vista de la partida con el ID de la partida en la URL
        return new ModelAndView("redirect:/partida-truco?idPartida=" + truco.getId() + "&idUsuario=" + usuarioActivo.getId());
    }

    @RequestMapping("/partida-truco")
    public ModelAndView irAPartidaTruco(
            @ModelAttribute("idPartida") Long partidaId,
            @ModelAttribute("idUsuario") Long idUsuario
    ) {
        ModelMap model = new ModelMap();

        // Obtener usuario
        Usuario usuarioActual = servicioUsuario.buscarPorId(idUsuario);
        if (usuarioActual == null) return new ModelAndView("redirect:/login");
        if (partidaId == null) return new ModelAndView("redirect:/home");
        model.put("usuarioActual", usuarioActual);

        // Obtener partida
        Partida partida = servicioTruco.obtenerPartidaPorId(partidaId);
        if (partida == null) return new ModelAndView("redirect:/home");
        model.put("partida", partida);

        // Obtenemos estadisticas de jugadores en caso de existir
        if (partida.getJ1() != null) {
            Estadistica estadisticasJ1 = servicioEstadistica.obtenerEstadisticasDeUnJugador(partida.getJ1().getUsuario());
            model.put("estadisticasJ1", estadisticasJ1);
        }
        if (partida.getJ2() != null) {
            Estadistica estadisticasJ2 = servicioEstadistica.obtenerEstadisticasDeUnJugador(partida.getJ2().getUsuario());
            model.put("estadisticasJ2", estadisticasJ2);
        }

        if (partida.getJ1() != null && partida.getJ2() != null) {
            // Comenzar partida
            if (partida.getPuedeEmpezar()) {
                Mano mano = servicioMano.obtenerManoPorId(partida.getId());

                // Verificar si hay ganador
                if (partida.getGanador() != null) {
                    model.put("ganador", partida.getGanador());
                    model.put("partidaFinalizada", true);
                    model.put("mano", mano);
                    if (!partida.getSeGuardo()) servicioEstadistica.guardarResultado(partida);
                    return new ModelAndView("partida-truco", model);
                }

                // Verificar que haya mano
                if (mano == null) {
                    setearDatosParaManoNoEmpezada(partida, model);
                    return new ModelAndView("partida-truco", model);
                }

                // Si el usuario actual es el J1, sino el J2
                if (usuarioActual.getId().equals(partida.getJ1().getUsuario().getId())) {
                    setearAtributosDeModeloParaUsuario1(partida, mano, model);
                    model.put("tengoFlor", servicioMano.tieneFlor(partida.getJ1(), mano));
                    model.put("accionesNormales", this.filtrarAccionesNormales(
                            servicioMano.tieneFlor(partida.getJ1(), mano),
                            mano.getIndicadorTruco(),
                            mano.getPuntosEnJuegoEnvido(),
                            mano.getEstaTerminada())
                    );
                } else {
                    setearAtributosDeModeloParaUsuario2(partida, mano, model);
                    model.put("tengoFlor", servicioMano.tieneFlor(partida.getJ2(), mano));
                    model.put("accionesNormales", this.filtrarAccionesNormales(
                                    servicioMano.tieneFlor(partida.getJ2(), mano),
                                    mano.getIndicadorTruco(),
                                    mano.getPuntosEnJuegoEnvido(),
                                    mano.getEstaTerminada()
                            )
                    );
                }

                // ACCIONES ENVIDO
                model.put("accionesEnvido", this.filtrarAccionesEnvido(
                        mano.getPuntosEnJuegoEnvido(),
                        servicioMano.esLaPrimerRonda(mano))
                );
                // ACCIONES TRUCO
                model.put("accionesTruco", this.filtrarAccionesTruco(mano.getIndicadorTruco(), mano.getHayQuiero()));
                model.put("respondoYo",
                        mano.getRespondeAhora() != null
                                && mano.getRespondeAhora().getUsuario().getId().equals(usuarioActual.getId())
                );
                // RESPUESTAS
                model.put("respondoEnvido", this.saberSiFueEnvido(mano.getUltimaAccionPreguntada()));
                model.put("respondoTruco", this.saberSiFueTruco(mano.getUltimaAccionPreguntada()));
                model.put("meTocaTirar", mano.getTiraAhora().getUsuario().getId().equals(usuarioActual.getId()));
                model.put("tiraAhora", mano.getTiraAhora());

                // Atributos independientes de que jugador es cual
                model.put("seTermino", mano.getEstaTerminada());
                model.put("puntosParaGanar", partida.getPuntosParaGanar());
                model.put("mano", mano);
                model.put("idPartida", partida.getId());
                model.put("accionAResponder", mano.getUltimaAccionPreguntada());

                model.put("partidaIniciada", partida.getPuedeEmpezar() != null ? partida.getPuedeEmpezar() : false);

                return new ModelAndView("partida-truco", model);
            }

            model.put("ganador", partida.getGanador());
            model.put("idPartida", partida.getId());
            model.put("idUsuario", idUsuario);

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


    @GetMapping("/comenzar-truco")
    public ModelAndView comenzarTruco(
            @ModelAttribute("idPartida") Long idPartida,
            @ModelAttribute("idUsuario") Long idUsuario
    ) {
        // Obtén la partida por su ID
        Partida partida = servicioTruco.obtenerPartidaPorId(idPartida);

        // Si no existe la partida, redirige a /home
        if (partida == null) return new ModelAndView("redirect:/home");

        // Empezamos la partida y la mano
        servicioTruco.empezar(partida);
        servicioMano.empezar(partida);

        return new ModelAndView("redirect:/partida-truco?idPartida=" + partida.getId() + "&idUsuario=" + idUsuario);
    }

    @GetMapping(path = "/accion-tirar")
    public ModelAndView accionTirarCarta(
            @RequestParam("idPartida") Long idPartida,
            @RequestParam("idUsuario") Long idUsuario,
            @RequestParam("cartaId") Long cartaId,
            @RequestParam("manoId") String manoId,
            @RequestParam("nroJugador") String nroJugador) {

        // Obtener partida
        Partida partida = servicioTruco.obtenerPartidaPorId(idPartida);
        if (partida == null) return new ModelAndView("redirect:/home");

        // Obtener usuario
        Usuario usuarioActual = servicioUsuario.buscarPorId(idUsuario);

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
            servicioMano.guardar(mano);
        } else if (partida.getPuntosJ2() >= partida.getPuntosParaGanar()) {
            servicioTruco.finalizarPartida(idPartida, partida.getJ2());
            mano.getCartasTiradasJ1().clear();
            mano.getCartasTiradasJ2().clear();
            mano.getCartasJ1().clear();
            mano.getCartasJ2().clear();
            servicioMano.guardar(mano);
        }

        return new ModelAndView("redirect:/partida-truco?idPartida=" + partida.getId() + "&idUsuario=" + usuarioActual.getId());
    }

    @RequestMapping("/cambiar-mano")
    public ModelAndView cambiarMano(
            @ModelAttribute("idPartida") Long idPartida,
            @ModelAttribute("idUsuario") Long idUsuario
    ) {
        ModelMap model = new ModelMap();
        model.put("idUsuario", idUsuario);

        Usuario usuarioActual = servicioUsuario.buscarPorId(idUsuario);
        model.put("usuarioActual", usuarioActual);

        // Obtener partida
        Partida partida = servicioTruco.obtenerPartidaPorId(idPartida);
        if (partida == null) return new ModelAndView("redirect:/home");
        model.put("partida", partida);

        // Obtenemos estadisticas de jugadores en caso de existir
        if (partida.getJ1() != null) {
            Estadistica estadisticasJ1 = servicioEstadistica.obtenerEstadisticasDeUnJugador(partida.getJ1().getUsuario());
            model.put("estadisticasJ1", estadisticasJ1);
        }
        if (partida.getJ2() != null) {
            Estadistica estadisticasJ2 = servicioEstadistica.obtenerEstadisticasDeUnJugador(partida.getJ2().getUsuario());
            model.put("estadisticasJ2", estadisticasJ2);
        }

        Mano ultimaMano = servicioMano.obtenerManoPorId(idPartida);
        if (!ultimaMano.getEstaTerminada()) {
            return new ModelAndView("redirect:/partida-truco?idPartida=" + partida.getId() + "&idUsuario=" + usuarioActual.getId());
        }
        ultimaMano.setConfirmacionTerminada(true);
        Mano mano = servicioMano.reset(partida);

        if (usuarioActual == null) return new ModelAndView("redirect:/login");
        if (idPartida == null) return new ModelAndView("redirect:/home");

        if (partida.getJ1() != null && partida.getJ2() != null) {

            if (partida.getPuedeEmpezar()) {

                // Verificar si hay ganador
                if (partida.getGanador() != null) {
                    model.put("ganador", partida.getGanador());
                    model.put("partidaFinalizada", true);
                    model.put("mano", mano);
                    if (!partida.getSeGuardo()) servicioEstadistica.guardarResultado(partida);
                    return new ModelAndView("partida-truco", model);
                }

                if (mano == null) {
                    this.setearDatosParaManoNoEmpezada(partida, model);
                    return new ModelAndView("partida-truco", model);
                }

                // Si el usuario actual es el J1, sino el J2
                if (usuarioActual.getId().equals(partida.getJ1().getUsuario().getId())) {
                    setearAtributosDeModeloParaUsuario1(partida, mano, model);
                    model.put("tengoFlor", servicioMano.tieneFlor(partida.getJ1(), mano));
                    model.put("accionesNormales", this.filtrarAccionesNormales(
                            servicioMano.tieneFlor(partida.getJ1(), mano),
                            mano.getIndicadorTruco(),
                            mano.getPuntosEnJuegoEnvido(),
                            mano.getEstaTerminada())
                    );
                } else {
                    setearAtributosDeModeloParaUsuario2(partida, mano, model);
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

    private void setearAtributosDeModeloParaUsuario2(Partida partida, Mano mano, ModelMap model) {
        model.put("yo", partida.getJ2());
        model.put("rival", partida.getJ1());
        model.put("misPuntos", partida.getPuntosJ2());
        model.put("puntosRival", partida.getPuntosJ1());
        model.put("misCartas", mano.getCartasJ2());
        model.put("misCartasTiradas", mano.getCartasTiradasJ2());
        model.put("cartasOponente", mano.getCartasJ1());
        model.put("cartasOponenteTiradas", mano.getCartasTiradasJ1());
        model.put("miNumero", partida.getJ2().getNumero());
    }

    private void setearAtributosDeModeloParaUsuario1(Partida partida, Mano mano, ModelMap model) {
        model.put("yo", partida.getJ1());
        model.put("rival", partida.getJ2());
        model.put("misPuntos", partida.getPuntosJ1());
        model.put("puntosRival", partida.getPuntosJ2());
        model.put("misCartas", mano.getCartasJ1());
        model.put("misCartasTiradas", mano.getCartasTiradasJ1());
        model.put("cartasOponente", mano.getCartasJ2());
        model.put("cartasOponenteTiradas", mano.getCartasTiradasJ2());
        model.put("miNumero", partida.getJ1().getNumero());
    }

    @RequestMapping("/abandonar")
    public ModelAndView abandonarPartida(
            @ModelAttribute("idPartida") Long partidaId,
            @ModelAttribute("idUsuario") Long idUsuario
    ) {
        ModelMap model = new ModelMap();

        // Obtener usuario
        Usuario usuarioActual = servicioUsuario.buscarPorId(idUsuario);
        if (usuarioActual == null) return new ModelAndView("redirect:/login");

        // Obtener partida
        Partida partida = servicioTruco.obtenerPartidaPorId(partidaId);
        if (partida == null) return new ModelAndView("redirect:/home");

        servicioTruco.jugadorAbandona(partidaId, idUsuario);

        return new ModelAndView("redirect:/partida-truco?idPartida=" + partida.getId() + "&idUsuario=" + usuarioActual.getId());
    }


    @GetMapping(path = "/accion")
    public ModelAndView accion(
            @RequestParam("idPartida") Long idPartida,
            @RequestParam("idUsuario") Long idUsuario,
            @RequestParam("accion") String accionValue,
            @RequestParam("jugador") String nroJugador,
            RedirectAttributes redirectAttributes
    ) {
        redirectAttributes.addFlashAttribute("accionAResponder", accionValue);

        // Obtener partida y mano
        Mano mano = servicioMano.obtenerManoPorId(idPartida);

        servicioMano.preguntar(mano, accionValue, Integer.parseInt(nroJugador));

        return new ModelAndView("redirect:/partida-truco?idPartida=" + idPartida + "&idUsuario=" + idUsuario);
    }


    @GetMapping(path = "/respuesta")
    public ModelAndView responder(
            @RequestParam("idPartida") Long idPartida,
            @RequestParam("idUsuario") Long idUsuario,
            @RequestParam("accion") String accionAlCualResponde,
            @RequestParam("respuesta") String nroRespuesta,
            @RequestParam("jugador") String nroJugador
    ) {

        Mano mano = servicioMano.obtenerManoPorId(idPartida);

        // Retorna jugador que le toca responder si es que responde algo que no sea quiero/no quiero. Si es así, da null;
        servicioMano.responder(mano, accionAlCualResponde, nroRespuesta, Integer.parseInt(nroJugador));

        return new ModelAndView("redirect:/partida-truco?idPartida=" + idPartida + "&idUsuario=" + idUsuario);
    }

    private void setearDatosParaManoNoEmpezada(Partida partida, ModelMap model) {
        model.put("respondoYo", false);
        model.put("respondoEnvido", false);
        model.put("respondoTruco", false);
        model.put("partidaIniciada", partida.getPuedeEmpezar() != null ? partida.getPuedeEmpezar() : false);
        model.put("ganador", partida.getGanador());
        model.put("idPartida", partida.getId());
    }

    private List<Accion> filtrarAccionesNormales(
            Boolean tengoFlor,
            Integer indicadorTruco,
            Integer puntosEnJuegoEnvido,
            Boolean terminoLaMano) {
        List<Integer> valoresEnvido = Arrays.asList(98, 99);
        return getAccionesNormales().stream()
                .filter(accion -> accion.getNro() != 5
                        || (indicadorTruco == 0 && valoresEnvido.contains(puntosEnJuegoEnvido)))
                .filter(accion -> accion.getNro() != 8 || tengoFlor && puntosEnJuegoEnvido != 98)
                .filter(accion -> accion.getNro() != 9 || !terminoLaMano)
                .collect(Collectors.toList());

    }

    private List<Accion> filtrarAccionesTruco(Integer indicadorTruco, Boolean hayQuiero) {
        List<Integer> valoresQuiero = Arrays.asList(1, 2, 3);
        List<Integer> valoresNoQuiero = Arrays.asList(1, 2, 3);
        return getAccionesTruco().stream()
                .filter(accion -> accion.getNro() != 0 || (valoresNoQuiero.contains(indicadorTruco) && !hayQuiero))
                .filter(accion -> accion.getNro() != 1 || (valoresQuiero.contains(indicadorTruco) && !hayQuiero))
                .filter(accion -> accion.getNro() != 6 || (!hayQuiero && indicadorTruco == 1))
                .filter(accion -> accion.getNro() != 7 || (!hayQuiero && indicadorTruco == 2))
                .collect(Collectors.toList());
    }

    private List<Accion> filtrarAccionesEnvido(Integer indicadorEnvido, Boolean puedoCantarEnvido) {
        List<Integer> valoresFaltaEnvido = Arrays.asList(2, 3, 4, 5, 7, 99);
        List<Integer> valoresRealEnvido = Arrays.asList(2, 4, 99);
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
        List<Integer> ve = Arrays.asList(2, 3, 5, 4, 99);
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