package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.ServicioAmistad;
import com.tallerwebi.dominio.ServicioUsuario;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.dominio.excepcion.AmistadesException;
import com.tallerwebi.infraestructura.ServicioAmistadImpl;
import com.tallerwebi.infraestructura.ServicioUsuarioImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ControladorAmigosTest {
    ServicioAmistad servicioAmistad = mock(ServicioAmistadImpl.class);
    ServicioUsuario servicioUsuario = mock(ServicioUsuarioImpl.class);
    ControladorAmigos controladorAmigos = new ControladorAmigos(servicioAmistad, servicioUsuario);

    @Test
    public void obtenerAmigosDeUsuarioPorId() throws AmistadesException {
        // Given - usuario de sesión
        when(servicioUsuario.buscar("gonzalo@gonzalo.com")).thenReturn(new Usuario());
        when(servicioUsuario.buscar("user1@user.com")).thenReturn(new Usuario());
        when(servicioUsuario.buscar("user2@user.com")).thenReturn(new Usuario());
        Usuario mainUser = servicioUsuario.buscar("gonzalo@gonzalo.com");
        Usuario a1 = servicioUsuario.buscar("user1@user.com");
        Usuario a2 = servicioUsuario.buscar("user2@user.com");
        servicioAmistad.agregarAmigo(mainUser, a1);
        servicioAmistad.agregarAmigo(mainUser, a2);
        // When
        when(servicioAmistad.getAmigosDeUnUsuarioPorId(mainUser.getId())).thenReturn(List.of(a1, a2));
        List<Usuario> amigos = servicioAmistad.getAmigosDeUnUsuarioPorId(mainUser.getId());
        // Then
        assertEquals(2, amigos.size());
    }
}
