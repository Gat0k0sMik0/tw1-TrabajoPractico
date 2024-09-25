package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.ServicioUsuario;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.infraestructura.ServicioUsuarioImpl;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ControladorHomeTest {

    ServicioUsuario servicioUsuario = mock(ServicioUsuarioImpl.class);
    ControladorHome controladorHome = new ControladorHome(servicioUsuario);

    @Test
    public void siEstaRegistradoYSeLogueoQueMuestreElHome () {
        String email = "pepito@pepito.com";
        Usuario creado = new Usuario();
        creado.setEmail(email);
        creado.setPassword("");
        when(servicioUsuario.registrar(creado.getEmail(),creado.getPassword())).thenReturn(creado);
        when(servicioUsuario.buscar(creado.getEmail())).thenReturn(creado);
        Usuario buscado = servicioUsuario.buscar(email);
        assertThat(buscado.getEmail(), equalToIgnoringCase(email));
        assertThat(controladorHome.irAlHome(email).getViewName(), equalToIgnoringCase("home"));
    }

    @Test
    public void queSiNoEstaLogueadoLoRedireccioneAlLogin () {
        String email = "pepito@pepito.com";
        assertThat(
                controladorHome.irAlHome(email).getViewName(),
                equalToIgnoringCase("redirect:/login")
        );
    }

}
