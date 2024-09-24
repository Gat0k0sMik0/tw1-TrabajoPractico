package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.RepositorioUsuario;
import com.tallerwebi.dominio.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ControladorAmigos {
    @RequestMapping("/Amigos")
    public ModelAndView irAChatAmigos() {
        return new ModelAndView("Amigos");
    }

    @Autowired
    private RepositorioUsuario repositorioUsuario;

    // Mostrar la vista de lista de amigos
    @GetMapping("/amigos")
    public String mostrarAmigos(Model model) {
        List<Usuario> listaAmigos = repositorioUsuario.findAll();  // Obtenemos todos los amigos de la base de datos
            model.addAttribute("amigos", listaAmigos);
            return "listaAmigos";  // Redirige a la plantilla "listaAmigos.html"

    }

    // Mostrar los detalles de un amigo específico
    @GetMapping("/amigos/{nombre}")
    public String mostrarDetallesAmigo(@PathVariable String nombre, Model model) {
        Usuario amigo = repositorioUsuario.buscarPorNombre(nombre);
        if (amigo != null) {
            model.addAttribute("amigo", amigo);
            return "detallesAmigo";  // Redirige a la plantilla "detallesAmigo.html"
        } else {
            return "redirect:/amigos";  // Si no se encuentra el amigo, redirige a la lista de amigos
        }
    }
}

