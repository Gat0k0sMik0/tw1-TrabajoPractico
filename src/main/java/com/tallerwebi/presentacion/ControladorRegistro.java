package com.tallerwebi.presentacion;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ControladorRegistro {

    @GetMapping("/registro")
    public String mostrarFormulario(Model model) {
        return "registro";
    }

    @PostMapping("/registro")
    public String procesarFormulario(
            @RequestParam("nombre") String nombre,
            @RequestParam("apellido") String apellido,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("password2") String password2,
            Model model) {

        if (!password.equals(password2)) {
            model.addAttribute("error", "Las contraseñas no coinciden");
            return "registro";
        }
        else {
            model.addAttribute("mensaje", "¡Registro Exitoso!");
        }


        return "registro";
    }


}
