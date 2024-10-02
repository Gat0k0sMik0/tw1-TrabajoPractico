package com.tallerwebi.dominio;

import com.tallerwebi.infraestructura.RepositorioUsuarioImpl;
import com.tallerwebi.infraestructura.ServicioUsuarioImpl;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

public class ServicioUsuarioTest {

    RepositorioUsuario repositorioUsuario = mock(RepositorioUsuarioImpl.class);
    ServicioUsuario servicioUsuario = new ServicioUsuarioImpl(repositorioUsuario);

    @Test
    public void queBuscarUnUsuarioQueNoExisteDeNull() {
        when(repositorioUsuario.buscarPorMail("prueba@prueba.com")).thenReturn(null);
        Usuario usuario = servicioUsuario.buscar("prueba@prueba.com");
        assertThat(usuario, nullValue());
    }

    @Test
    public void queEncuentreUnUsuarioYaRegistrado() {
        // given
        String email = "gonzalo@gonzalo.com";
        Usuario tester = new Usuario();
        tester.setEmail(email);
        tester.setPassword("");
        servicioUsuario.registrar(email, "", "carla");
        //when
        when(repositorioUsuario.buscarPorMail(email)).thenReturn(tester);
        Usuario buscado = servicioUsuario.buscar(email);
        //then
        assertThat(buscado.getEmail(), equalToIgnoringCase(email));

    }

    @Test
    public void siNoExisteOtroUsuarioConEseMailEntoncesSeRegistraCorrectamente() {
        // given
        String email = "gonzalo@gonzalo.com";
        // when
        Usuario nuevoUsuario = servicioUsuario.registrar(email, "", "carla");
        // then
        assertThat(nuevoUsuario, notNullValue());
    }

    @Test
    public void siExisteOtroUsuarioConEseMailEntoncesFalla() {
        servicioUsuario.registrar("gonzalo@gonzalo.com", "", "carla");
        when(repositorioUsuario.buscarPorMail("gonzalo@gonzalo.com")).thenReturn(new Usuario());
        Usuario nuevoCreado = servicioUsuario.registrar("gonzalo@gonzalo.com", "", "carla");
        assertThat(nuevoCreado, nullValue());
    }
}
