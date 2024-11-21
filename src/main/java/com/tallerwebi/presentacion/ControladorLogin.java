package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.ServicioEmail;
import com.tallerwebi.dominio.ServicioLogin;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.dominio.excepcion.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class ControladorLogin {

    private ServicioLogin servicioLogin;
    private ServicioEmail servicioEmail;

    @Autowired
    public ControladorLogin(ServicioLogin servicioLogin, ServicioEmail servicioEmail) {
        this.servicioLogin = servicioLogin;
        this.servicioEmail = servicioEmail;
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
            request.getSession().setAttribute("email", usuarioBuscado.getEmail());
            request.getSession().setAttribute("nombreUsuario", usuarioBuscado.getNombreUsuario());
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
        return new ModelAndView("nuevo-usuario", model);
    }

    // METODOS PARA EL REGISTRO
    @RequestMapping(path = "/registrarme", method = RequestMethod.POST)
    public ModelAndView registrarme(@ModelAttribute("nuevoUsuario") Usuario usuario, RedirectAttributes redirectAttributes, HttpSession sesion) {
        ModelMap model = new ModelMap();

        // Manejo de excepciones
        try {
            servicioLogin.registrar(usuario);
        } catch (EmailInvalidoException e) {
            model.put("error", "El correo no es válido");
            return new ModelAndView("nuevo-usuario", model);
        } catch (MailExistenteException e) {
            model.put("error", "El correo ya existe");
            return new ModelAndView("nuevo-usuario", model);
        } catch (UsuarioInvalidoException e) {
            model.put("error", "El usuario debe contener entre 4 y 16 caracteres");
            return new ModelAndView("nuevo-usuario", model);
        } catch (UsuarioExistenteException e) {
            model.put("error", "El usuario ya existe");
            return new ModelAndView("nuevo-usuario", model);
        } catch (ContraseniaInvalidaException e) {
            model.put("error", "La contraseña debe contener entre 5 y 15 caracteres");
            return new ModelAndView("nuevo-usuario", model);
        } catch (ContraseniasDiferentesException e) {
            model.put("error", "Las contraseñas no coinciden");
            return new ModelAndView("nuevo-usuario", model);
        }

        sesion.setAttribute("usuarioTemporal", usuario);
        redirectAttributes.addFlashAttribute("contrasenia", usuario.getPassword());
        redirectAttributes.addFlashAttribute("email", usuario.getEmail());
        return new ModelAndView("redirect:/verificacion-cuenta");
    }

    @PostMapping("/verificacion-cuenta")
    public ModelAndView enviarCodigoVerificacion(@RequestParam String email, @RequestParam String password, HttpSession sesion) {
        ModelMap model = new ModelMap();

        // Generar el código de validación de 6 dígitos
        String validationCode = servicioEmail.generateValidationCode();
        sesion.setAttribute("validationCode", validationCode);

        // Enviar el correo con el código de validación
        try {
            servicioEmail.sendValidationCode(email, validationCode);
        } catch (MessagingException e) {
            model.addAttribute("error", "No se pudo enviar el correo electrónico.");
            return new ModelAndView("nuevo-usuario", model);
        } catch (jakarta.mail.MessagingException e) {
            throw new RuntimeException(e);
        }

        // Mostrar la vista para que el usuario ingrese el código de validación
        model.addAttribute("email", email);  // Pasar el correo al siguiente paso
        return new ModelAndView("inicioDeSesion", model);
    }

    @PostMapping("/verificar")
    public ModelAndView verificarCodigo(@RequestParam String codigo, @RequestParam String email, RedirectAttributes redirectAttrs, HttpSession sesion) {
        String validationCode = (String) sesion.getAttribute("validationCode");
        Usuario usuario = (Usuario) sesion.getAttribute("usuarioTemporal");
        if (validationCode.equals(codigo)) {
            // Si el código es correcto, crear la cuenta
            servicioLogin.guardarUsuario(usuario); // Cambiar el proceso de creación de usuario según tu lógica
            redirectAttrs.addFlashAttribute("message", "Cuenta creada exitosamente!");
            return new ModelAndView("redirect:/login");
        } else {
            // Si el código es incorrecto
            redirectAttrs.addFlashAttribute("error", "Código de validación incorrecto.");
            redirectAttrs.addFlashAttribute("contrasenia", usuario.getPassword());
            redirectAttrs.addFlashAttribute("email", usuario.getEmail());
            return new ModelAndView("redirect:/verificacion-cuenta");
        }
    }

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public ModelAndView inicio() {
        return new ModelAndView("redirect:/login");
    }
}

