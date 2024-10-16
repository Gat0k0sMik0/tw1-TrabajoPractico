package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.ServicioLogin;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.dominio.excepcion.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ControladorLogin {

    private ServicioLogin servicioLogin;

    @Autowired
    public ControladorLogin(ServicioLogin servicioLogin) {
        this.servicioLogin = servicioLogin;
    }

    // MÉTODOS PARA EL LOGIN
    @RequestMapping("/login")
    public ModelAndView irALogin() {
        ModelMap modelo = new ModelMap();
        modelo.put("datosLogin", new DatosLogin());
        return new ModelAndView("inicioDeSesion", modelo);
    }

    @RequestMapping(path = "/validar-login", method = RequestMethod.POST)
    public ModelAndView validarLogin(@ModelAttribute("datosLogin") DatosLogin datosLogin, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        Usuario usuarioBuscado = servicioLogin.consultarUsuario(datosLogin.getEmail(), datosLogin.getPassword());
        if (usuarioBuscado != null) {
            request.getSession().setAttribute("usuarioActivo", usuarioBuscado);
            redirectAttributes.addFlashAttribute("nombreUsuario", usuarioBuscado.getNombreUsuario());
            redirectAttributes.addFlashAttribute("email", usuarioBuscado.getEmail());
            return new ModelAndView("redirect:/home");
        } else {
            ModelMap model = new ModelMap();
            model.put("error", "El usuario o clave son incorrectos");
            return new ModelAndView("inicioDeSesion", model);
        }
    }

    @RequestMapping(path = "/nuevo-usuario", method = RequestMethod.GET)
    public ModelAndView nuevoUsuario() {
        ModelMap model = new ModelMap();
        model.put("nuevoUsuario", new Usuario());
        return new ModelAndView("registro", model);
    }

    // METODOS PARA EL REGISTRO
    @RequestMapping(path = "/registrarme", method = RequestMethod.POST)
    public ModelAndView registrarme(@ModelAttribute("nuevoUsuario") Usuario usuario) {
        ModelMap model = new ModelMap();

        // Manejo de excepciones
        try {
            servicioLogin.registrar(usuario);
        } catch (EmailInvalidoException e) {
            model.put("error", "El correo no es válido");
            return new ModelAndView("registro", model);
        } catch (MailExistenteException e) {
            model.put("error", "El correo ya existe");
            return new ModelAndView("registro", model);
        } catch (UsuarioInvalidoException e) {
            model.put("error", "El usuario debe contener entre 4 y 16 caracteres");
            return new ModelAndView("registro", model);
        } catch (UsuarioExistenteException e) {
            model.put("error", "El usuario ya existe");
            return new ModelAndView("registro", model);
        } catch (ContraseniaInvalidaException e) {
            model.put("error", "La contraseña debe contener entre 5 y 15 caracteres");
            return new ModelAndView("registro", model);
        } catch (ContraseniasDiferentesException e) {
            model.put("error", "Las contraseñas no coinciden");
            return new ModelAndView("registro", model);
        }

        return new ModelAndView("redirect:/login");
    }

    /*@RequestMapping(path = "/home", method = RequestMethod.GET)
    public ModelAndView irAHome() {
        return new ModelAndView("home");
    }*/

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public ModelAndView inicio() {
        return new ModelAndView("redirect:/login");
    }
}

