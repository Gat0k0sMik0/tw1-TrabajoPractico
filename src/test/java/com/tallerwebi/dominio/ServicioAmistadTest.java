package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.AmistadesException;
import com.tallerwebi.infraestructura.RepositorioAmistadImpl;
import com.tallerwebi.infraestructura.RepositorioUsuarioImpl;
import com.tallerwebi.infraestructura.ServicioAmistadImpl;
import com.tallerwebi.infraestructura.ServicioUsuarioImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class ServicioAmistadTest {

    RepositorioAmistad repositorioAmistad = mock(RepositorioAmistadImpl.class);
    RepositorioUsuario repositorioUsuario = mock(RepositorioUsuario.class);
    ServicioAmistadImpl servicioAmistad = new ServicioAmistadImpl(repositorioAmistad, repositorioUsuario);


    @Test
    public void queSeAgregueUnAmigo() throws AmistadesException {
        Usuario usuario = new Usuario();
        Usuario amigo = new Usuario();
        Amistad amistad = new Amistad(usuario, amigo);
        repositorioAmistad.agregarAmigo(amistad);
        when(repositorioAmistad.buscarAmigoDeUsuario(usuario,amigo)).thenReturn(new Amistad());
        Amistad b = repositorioAmistad.buscarAmigoDeUsuario(usuario,amigo);
        assertThat(b, notNullValue());
    }

    @Test
    public void ObtenerUnaRelacionEntreUsuarios () throws AmistadesException {
        Usuario usuarioPrincipal = new Usuario();
        usuarioPrincipal.setEmail("gonzalo@gonzalo.com");
        Usuario amigo = new Usuario();
        amigo.setEmail("leonel@leonel.com");
        Amistad amistad = new Amistad(usuarioPrincipal, amigo);
        when(repositorioAmistad.buscarAmigoDeUsuario(usuarioPrincipal, amigo)).thenReturn(amistad);
        Amistad encontrada = repositorioAmistad.buscarAmigoDeUsuario(usuarioPrincipal, amigo);
        assertEquals(amistad, encontrada);
    }

    @Test
    public void queLosUsuariosRecomendadosParaAgregarNoEstenSusAmigos() throws AmistadesException {
        Usuario usuarioPrincipal = new Usuario();
        Usuario u1 = new Usuario();
        Usuario u2 = new Usuario();
        List<Usuario> noConocidos = List.of(u1, u2);
        // When
        when(servicioAmistad.obtenerRecomendacionesQueNoSeanSusAmigos(usuarioPrincipal.getId())).thenReturn(noConocidos);
        List<Usuario> filtrados = servicioAmistad.obtenerRecomendacionesQueNoSeanSusAmigos(usuarioPrincipal.getId());
        // Then
        assertEquals(2, filtrados.size());
    }
}
