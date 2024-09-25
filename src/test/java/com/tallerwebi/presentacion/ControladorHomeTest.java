package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.ServicioUsuario;
import com.tallerwebi.infraestructura.ServicioUsuarioImpl;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.mockito.Mockito.mock;

public class ControladorHomeTest {

    ServicioUsuario servicioUsuario = mock(ServicioUsuarioImpl.class);
    ControladorHome controladorHome = new ControladorHome(servicioUsuario);

    @Test
    public void vistaExitoso () {
        assertThat(controladorHome.irAlHome().getViewName(), equalToIgnoringCase("home"));
    }

    @Test
    public void queCargueAlgunCorreoDeUsuario () {
        String email = "pepito@pepito.com";
        assertThat(
                controladorHome.irAlHome().getModel().get("email").toString(),
                equalToIgnoringCase(email)
        );
    }

}
