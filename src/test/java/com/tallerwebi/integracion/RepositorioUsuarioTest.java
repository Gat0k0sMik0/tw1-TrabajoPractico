package com.tallerwebi.integracion;

import com.tallerwebi.dominio.RepositorioUsuario;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.integracion.config.HibernateTestConfig;
import com.tallerwebi.integracion.config.SpringWebTestConfig;
import org.hamcrest.MatcherAssert;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.transaction.Transactional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = {SpringWebTestConfig.class, HibernateTestConfig.class})
public class RepositorioUsuarioTest {

    @Autowired
    SessionFactory sessionFactory;
    @Autowired
    RepositorioUsuario repositorioUsuario;

    @Test
    @Transactional
    @Rollback
    public void queBuscarMeTraigaAlgo () {
        assertThat(repositorioUsuario.buscar(""), nullValue());
    }

    @Test
    @Transactional
    @Rollback
    public void siNoExisteElEmailQueSeaNulo () {
        assertThat(repositorioUsuario.buscar("carcajada@carcajada.com"), nullValue());
    }

    @Test
    @Transactional
    @Rollback
    public void queSePuedaGuardarUnUsuario() {
        // given
        Usuario usuario = new Usuario();
        usuario.setEmail("gonzalo@gonzalo.com");
        usuario.setPassword("gonzalo");
        // when
        repositorioUsuario.guardar(usuario);
        // then
        assertThat(usuario.getId(), notNullValue());
    }


    @Test
    @Transactional
    @Rollback
    public void puedoBuscarUsuarioPorMail(){
        // given
        Usuario usuario1 = new Usuario();
        usuario1.setEmail("flor1@flor.com");
        usuario1.setPassword("123456");
        sessionFactory.getCurrentSession().save(usuario1);

        // given 2
        Usuario usuario2 = new Usuario();
        usuario2.setEmail("flor2@flor.com");
        usuario2.setPassword("1234567");
        sessionFactory.getCurrentSession().save(usuario2);

        //when
        Usuario usuarioBuscado = repositorioUsuario.buscar("flor1@flor.com");
        // then
        assertThat(usuarioBuscado, notNullValue());
        assertThat(usuarioBuscado.getPassword(), equalToIgnoringCase("123456"));
    }

    @Test
    @Transactional
    @Rollback
    public void queEncuentreElUsuarioBuscado(){
        // given
        Usuario usuario1 = new Usuario();
        usuario1.setEmail("gonzalo@gonzalo.com");
        usuario1.setPassword("123456");
        sessionFactory.getCurrentSession().save(usuario1);
        //when
        Usuario buscado = repositorioUsuario.buscar("gonzalo@gonzalo.com");
        // then
        assertThat(buscado, notNullValue());
        assertThat(buscado.getPassword(), equalToIgnoringCase("123456"));
    }

    @Test
    @Transactional
    @Rollback
    public void queElUsuarioBuscadoDeNullPorqueNoExiste(){
        // given
        Usuario usuario1 = new Usuario();
        usuario1.setEmail("atajo@atajo.com");
        usuario1.setPassword("123456");
        sessionFactory.getCurrentSession().save(usuario1);
        //when
        Usuario buscado = repositorioUsuario.buscar("atajo@atajo.com");
        // then
        assertThat(buscado, notNullValue());
    }

    // GUARDAR, MODIFICAR, BUSCAR

}
