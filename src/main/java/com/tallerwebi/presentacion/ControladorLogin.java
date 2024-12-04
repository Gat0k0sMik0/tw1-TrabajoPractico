package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.*;
import com.tallerwebi.dominio.excepcion.*;
import com.tallerwebi.infraestructura.ServicioUsuarioImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Controller
public class ControladorLogin {

    private ServicioUsuario servicioUsuario;
    private ServicioLogin servicioLogin;
    private ServicioEmail servicioEmail;

    @Autowired
    public ControladorLogin(ServicioLogin servicioLogin, ServicioEmail servicioEmail, ServicioUsuario servicioUsuario) {
        this.servicioLogin = servicioLogin;
        this.servicioEmail = servicioEmail;
        this.servicioUsuario = servicioUsuario;
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
    public ModelAndView registrarme(@ModelAttribute("nuevoUsuario") Usuario usuario,
                                    RedirectAttributes redirectAttributes, HttpSession sesion) {
        ModelMap model = new ModelMap();

        // Manejo de excepciones
        try {
            servicioLogin.registrar(usuario);
        } catch (ActualizarUsuarioException e) {
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
        redirectAttributes.addFlashAttribute("email", usuario.getEmail());  // Usar addFlashAttribute para el email
        return new ModelAndView("redirect:/verificacion-correo");
    }

    @RequestMapping("/verificacion-correo")
    public ModelAndView enviarCodigoVerificacion(@ModelAttribute("email") String email, HttpSession sesion) {
        ModelMap model = new ModelMap();
        model.addAttribute("email", email);

        // Generar el código de validación de 6 dígitos
        String validationCode = servicioEmail.generateValidationCode();
        sesion.setAttribute("validationCode", validationCode);

        // Enviar el correo con el código de validación
        try {
            servicioEmail.sendValidationCode(email, validationCode);
        } catch (jakarta.mail.MessagingException e) {
            model.addAttribute("error", "No se pudo enviar el correo electrónico.");
            return new ModelAndView("nuevo-usuario", model);
        }

        return new ModelAndView("verificacionCorreo", model);  // Asegúrate de que redirige a la página de verificación
    }

    @RequestMapping("/verificar-correo")
    public ModelAndView verificarCodigo(@RequestParam String codigo1, @RequestParam String codigo2,
                                        @RequestParam String codigo3, @RequestParam String codigo4,
                                        @RequestParam String codigo5, @RequestParam String codigo6,
                                        @RequestParam String email,
                                        RedirectAttributes redirectAttrs, HttpSession sesion) {

        String codigo = codigo1 + codigo2 + codigo3 + codigo4 + codigo5 + codigo6;
        // Recuperar el código de validación y el usuario temporal de la sesión
        String validationCode = (String) sesion.getAttribute("validationCode");
        Usuario usuario = (Usuario) sesion.getAttribute("usuarioTemporal");

        System.out.println("Código ingresado: " + codigo);
        System.out.println("Código esperado: " + validationCode);

        // Verificar si el código ingresado es correcto
        if (codigo.trim().equals(validationCode.trim())) {
            // Si el código es correcto, guardar al usuario
            servicioLogin.guardarUsuario(usuario); // Lógica para guardar al usuario
            ModelMap model = new ModelMap();
            model.addAttribute(usuario);
            return new ModelAndView("subir-foto", model);
        } else {
            // Si el código es incorrecto, redirigir con mensaje de error
            redirectAttrs.addFlashAttribute("error", "Código de validación incorrecto.");
            redirectAttrs.addFlashAttribute("contrasenia", usuario.getPassword());
            redirectAttrs.addFlashAttribute("email", email);  // Usar el email recibido en el formulario
            return new ModelAndView("redirect:/nuevo-usuario");
        }
    }

    @RequestMapping("/subir-foto")
    public ModelAndView verificarFoto(@ModelAttribute("idUsuario") Long idUsuario,
                                      @RequestParam("fotoPerfil") MultipartFile fotoPerfil,
                                      RedirectAttributes redirectAttrs) throws IOException {
        if (!fotoPerfil.isEmpty()) {
            // Guardar el usuario y la foto en la base de datos
            servicioLogin.agregarFotoPerfil(servicioUsuario.buscarPorId(idUsuario), fotoPerfil);
        } else {
            // Si no sube nada, le asignamos foto por default
            servicioLogin.asignarFotoDefault(servicioUsuario.buscarPorId(idUsuario));
        }

        redirectAttrs.addFlashAttribute("message", "!Cuenta creada exitosamente!");
        return new ModelAndView("redirect:/login");
    }

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public ModelAndView inicio() {
        return new ModelAndView("redirect:/login");
    }
}

