package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.dominio.excepcion.*;
import com.tallerwebi.infraestructura.ServicioUsuarioImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import com.tallerwebi.dominio.ServicioUsuario;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;

@Controller
public class ControladorPerfil {

    private final ServicioUsuario servicioUsuario;

    public ControladorPerfil(ServicioUsuario servicioUsuario) {
        this.servicioUsuario = servicioUsuario;
    }

    @RequestMapping("/perfil")
        public ModelAndView perfilUsuario() {
            ModelMap model = new ModelMap();
            model.put("fullname", "John Doe");
            model.put("username", "johndoe123");
            return new ModelAndView("perfil", model);
        }


    @RequestMapping("/modificar-perfil")
    public ModelAndView irAModificarPerfil(HttpServletRequest request) {
        HttpSession sesion = request.getSession();
        Usuario usuario = (Usuario) sesion.getAttribute("usuarioActivo");
        ModelMap model = new ModelMap();
        model.put("datosUsuario", new Usuario());
        if (usuario != null) {
            model.put("nombreUsuario", usuario.getNombreUsuario());
        }
        return new ModelAndView("modificar-perfil", model);
    }


    @PostMapping("/actualizar-perfil")
    public ModelAndView actualizarPerfil(@ModelAttribute("datosUsuario") Usuario usuarioNuevo,
                                         HttpServletRequest request) {
        ModelMap model = new ModelMap();
        try {
            servicioUsuario.verificarDatos(usuarioNuevo);
        } catch (EmailInvalidoException e) {
            model.put("error", "El correo no es válido");
            return new ModelAndView("modificar-perfil", model);
        } catch (MailExistenteException e) {
            model.put("error", "El correo ya existe");
            return new ModelAndView("modificar-perfil", model);
        } catch (UsuarioInvalidoException e) {
            model.put("error", "El usuario debe contener entre 4 y 16 caracteres");
            return new ModelAndView("modificar-perfil", model);
        } catch (UsuarioExistenteException e) {
            model.put("error", "El usuario ya existe");
            return new ModelAndView("modificar-perfil", model);
        } catch (ContraseniaInvalidaException e) {
            model.put("error", "La contraseña debe contener entre 5 y 15 caracteres");
            return new ModelAndView("modificar-perfil", model);
        } catch (ContraseniasDiferentesException e) {
            model.put("error", "Las contraseñas no coinciden");
            return new ModelAndView("modificar-perfil", model);
        }

        HttpSession sesion = request.getSession();
        Usuario usuarioLogueado = (Usuario) sesion.getAttribute("usuarioActivo");
        usuarioLogueado =  servicioUsuario.actualizarPerfil(usuarioLogueado, usuarioNuevo);
        sesion.setAttribute("usuarioActivo", usuarioLogueado);

        return new ModelAndView("perfil", "datosUsuario", usuarioLogueado);
    }

    @GetMapping("/volver-home")
    public ModelAndView volverHome(HttpServletRequest request) {
        HttpSession sesion = request.getSession();
        Usuario usuarioLogueado = (Usuario) sesion.getAttribute("usuarioActivo");
        ModelMap modelo = new ModelMap();
        modelo.addAttribute("email", usuarioLogueado.getEmail());
        return new ModelAndView("home", modelo);
    }


}