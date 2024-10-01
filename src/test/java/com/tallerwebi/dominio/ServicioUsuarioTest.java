package com.tallerwebi.dominio;

import com.tallerwebi.infraestructura.RepositorioUsuarioImpl;
import com.tallerwebi.infraestructura.ServicioUsuarioImpl;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ServicioUsuarioTest {

    RepositorioUsuario repositorioUsuario = mock(RepositorioUsuarioImpl.class);
    ServicioUsuario servicioUsuario = new ServicioUsuarioImpl(repositorioUsuario);

    @Test
    public void queBuscarUnUsuarioQueNoExisteDeNull() {
        when(repositorioUsuario.buscar("prueba@prueba.com")).thenReturn(null);
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
        servicioUsuario.registrar(email, "", "coco");
        //when
        when(repositorioUsuario.buscar(email)).thenReturn(tester);
        Usuario buscado = servicioUsuario.buscar(email);
        //then
        assertThat(buscado.getEmail(), equalToIgnoringCase(email));

    }

    @Test
    public void siNoExisteOtroUsuarioConEseMailEntoncesSeRegistraCorrectamente() {
        // given
        String email = "gonzalo@gonzalo.com";
        // when
        Usuario nuevoUsuario = servicioUsuario.registrar(email, "", "coco");
        // then
        assertThat(nuevoUsuario, notNullValue());
    }

    @Test
    public void siExisteOtroUsuarioConEseMailEntoncesFalla() {
        servicioUsuario.registrar("gonzalo@gonzalo.com", "", "coco");
        when(repositorioUsuario.buscar("gonzalo@gonzalo.com")).thenReturn(new Usuario());
        Usuario nuevoCreado = servicioUsuario.registrar("gonzalo@gonzalo.com", "", "coco");
        assertThat(nuevoCreado, nullValue());
    }

    @Test
    public void queSePuedaModificarLaContrasena() {
        // given
        Usuario nuevoUsuario = servicioUsuario.registrar("vickycroce@gmail.com", "hola", "Carla");
        String nuevaContra = "gato";

        //when
        when(repositorioUsuario.buscar("vickycroce@gmail.com")).thenReturn(nuevoUsuario);
        servicioUsuario.modificarContra( "vickycroce@gmail.com",  nuevaContra);
        // Simulamos la búsqueda en el repositorio después de cambiar la contraseña
        when(repositorioUsuario.buscar("vickycroce@gmail.com")).thenReturn(nuevoUsuario);

        // then
        Usuario usuarioModificado = repositorioUsuario.buscar("vickycroce@gmail.com");
        assertEquals(nuevaContra, usuarioModificado.getPassword());
    }

    @Test
    public void queSePuedaModificarElEmail() {
        // given
        Usuario nuevoUsuario = servicioUsuario.registrar("vickycroce@gmail.com", "hola", "Carla");
        String nuevoMail = "gato@gmail.com";

        //when
        when(repositorioUsuario.buscar("vickycroce@gmail.com")).thenReturn(nuevoUsuario);
        servicioUsuario.modificarEmail("vickycroce@gmail.com", nuevoMail);
        // Simulamos la búsqueda en el repositorio después de cambiar la contraseña
        when(repositorioUsuario.buscar(nuevoMail)).thenReturn(nuevoUsuario);
        Usuario usuarioBuscado = repositorioUsuario.buscar(nuevoMail);

        // then
        Usuario usuarioModificado = repositorioUsuario.buscar(nuevoMail);
        assertEquals(nuevoMail, usuarioModificado.getEmail());
        assertThat(usuarioBuscado, notNullValue());
    }
}
