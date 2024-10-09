package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.RepositorioUsuario;
import com.tallerwebi.dominio.ServicioAmistad;
import com.tallerwebi.dominio.ServicioUsuario;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.dominio.excepcion.AmistadesException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ControladorAmigos {

    @Autowired
    ServicioAmistad servicioAmistad;
    @Autowired
    ServicioUsuario servicioUsuario;

    public ControladorAmigos(ServicioAmistad servicioAmistad, ServicioUsuario servicioUsuario) {
        this.servicioAmistad = servicioAmistad;
        this.servicioUsuario = servicioUsuario;
    }

    @RequestMapping("/amigos")
    public ModelAndView irAChatAmigos(HttpServletRequest request) throws AmistadesException {
        ModelMap model = new ModelMap();
        Usuario usuarioSesion = (Usuario) request.getSession().getAttribute("usuarioActivo");
        List<Usuario> amigosDelUsuario = servicioAmistad.getAmigosDeUnUsuarioPorId(usuarioSesion.getId());
        if (amigosDelUsuario.isEmpty()) {
            List<Usuario> recomendados = servicioUsuario.getUsuariosRandom(2, usuarioSesion.getId());
            model.put("error", "No tenes amigos flaco");
            model.put("amigosSugeridos", recomendados);
        } else {
            model.put("amigos", amigosDelUsuario);
        }
        return new ModelAndView("amigos", model);

    }

}
