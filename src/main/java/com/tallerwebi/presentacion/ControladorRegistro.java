package com.tallerwebi.presentacion;
import com.tallerwebi.dominio.ServicioRegistro;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.dominio.excepcion.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ControladorRegistro {

    private final ServicioRegistro servicioRegistro;

    @Autowired
    public ControladorRegistro(ServicioRegistro servicioRegistro) {
        this.servicioRegistro = servicioRegistro;
    }

    @RequestMapping("/registro")
    public ModelAndView IrAlRegistro() {
        ModelMap model = new ModelMap();
        model.put("datosRegistro", new Usuario());
        return new ModelAndView("registro", model);
    }

    @RequestMapping(path = "/validarRegistro", method = RequestMethod.POST)
    public ModelAndView validarRegistro(@RequestParam("datosRegistro") Usuario datosUsuario) {
        ModelMap model = new ModelMap();

        try {
            servicioRegistro.validarDatos(datosUsuario);
        } catch (CamposVaciosException | NombreInvalidoException | EmailInvalidoException |
                 ContraseniasDiferentesException | ContraseniaInvalidaException e) {
            String error = e.getMessage();
            model.put("error", error);
            return new ModelAndView("registro", model);
        }
        return new ModelAndView("redirect:/InicioDeSesion");
    }


}
