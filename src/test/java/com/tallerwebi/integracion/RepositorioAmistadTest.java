package com.tallerwebi.integracion;

import com.tallerwebi.dominio.*;
import com.tallerwebi.dominio.excepcion.AmistadesException;
import com.tallerwebi.integracion.config.HibernateTestConfig;
import com.tallerwebi.integracion.config.SpringWebTestConfig;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("unchecked")
@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = {SpringWebTestConfig.class, HibernateTestConfig.class})
public class RepositorioAmistadTest {

    @Autowired
    SessionFactory sessionFactory;
    @Autowired
    RepositorioAmistad repositorioAmistad;

    @Test
    @Rollback
    @Transactional
    public void queSeAgregueUnAmigoCorrectamente() throws AmistadesException {
        Usuario usuario = new Usuario();

        Usuario amigo = new Usuario();
        Amistad amistad = new Amistad();

        amistad.setUsuario(usuario);
        amistad.setAmigo(amigo);

        sessionFactory.getCurrentSession().save(amistad);
        sessionFactory.getCurrentSession().save(usuario);
        sessionFactory.getCurrentSession().save(amigo);

        Amistad amistadBuscada = repositorioAmistad.buscarAmigoDeUsuario(usuario, amigo);

        assertThat(amistad.getId(), notNullValue());
        assertThat(amistadBuscada, notNullValue());
    }

    @Test
    @Rollback
    @Transactional
    public void queSeAgreguenDosAmigosCorrectamente() {
        // GIVEN
        Usuario usuario = new Usuario();
        Usuario amigo = new Usuario();
        Usuario amigo2 = new Usuario();
        Amistad amistad = new Amistad();
        Amistad amistad2 = new Amistad();
        // amistad primera - amigo 1
        amistad.setUsuario(usuario);
        amistad.setAmigo(amigo);
        // otra amistad con otro -  amigo 2
        amistad2.setUsuario(usuario);
        amistad2.setAmigo(amigo2);
        // WHEN
        sessionFactory.getCurrentSession().save(amistad);
        sessionFactory.getCurrentSession().save(amistad2);
        // THEN
        assertThat(amistad.getId(), notNullValue());
        assertThat(amistad2.getId(), notNullValue());
//        assertThat(repositorioAmistad.agregarAmigo(usuario, amigo), notNullValue());
//        assertThat(repositorioAmistad.agregarAmigo(usuario, amigo2), notNullValue());
    }

    @Test
    @Rollback
    @Transactional
    public void buscarUnaAmistadYQueNoSeaNula() {
        // GIVEN
        Usuario usuario = new Usuario();
        Usuario amigo1 = new Usuario();
        Amistad amistad1 = givenUnUsuarioAgregaAUnAmigo(usuario, amigo1);
        // WHEN
        Session session = sessionFactory.getCurrentSession();
        session.save(usuario);
        session.save(amigo1);
        session.save(amistad1);
        Amistad amistadBuscada = (Amistad) session.createCriteria(Amistad.class)
                .add(Restrictions.eq("id", amistad1.getId()))
                .uniqueResult();

        // THEN
        assertThat(amistadBuscada, notNullValue());
    }

    private Amistad givenUnUsuarioAgregaAUnAmigo(Usuario usuario, Usuario amigo) {
        Amistad amistad = new Amistad(usuario, amigo);
        return amistad;
    }

    @Test
    @Rollback
    @Transactional
    public void queSeElimineUnAmigoCorrectamente() {
        // GIVEN
        Usuario usuario = new Usuario();
        Usuario amigo1 = new Usuario();
        Amistad amistad1 = givenUnUsuarioAgregaAUnAmigo(usuario, amigo1);
        // WHEN
        Session session = sessionFactory.getCurrentSession();
        session.save(usuario);
        session.save(amigo1);
        session.save(amistad1);
        Amistad amistadBuscada = (Amistad) session.createCriteria(Amistad.class)
                .add(Restrictions.eq("id", amistad1.getId()))
                .uniqueResult();
        session.delete(amistadBuscada);
        amistadBuscada = (Amistad) session.createCriteria(Amistad.class)
                .add(Restrictions.eq("id", amistad1.getId()))
                .uniqueResult();
        // THEN
        assertThat(amistadBuscada, nullValue());
    }

    @Test
    @Rollback
    @Transactional
    public void obtenerListadoDeAmigosYQueAlMenosTengaUno() {
        // GIVEN
        Usuario usuario = new Usuario();
        Usuario amigo1 = new Usuario();
        Amistad amistad1 = givenUnUsuarioAgregaAUnAmigo(usuario, amigo1);
        Session session = sessionFactory.getCurrentSession();
        session.save(usuario);
        session.save(amigo1);
        session.save(amistad1);
        List<Amistad> encontradas = new ArrayList<>();
        encontradas = (List<Amistad>) sessionFactory.getCurrentSession()
                .createCriteria(Amistad.class)
                .add(Restrictions.eq("usuario.id", usuario.getId()))
                .list();
        assertEquals(1, encontradas.size());
        assertEquals(1, repositorioAmistad.verAmigos(usuario.getId()).size());
    }

    @Test
    @Rollback
    @Transactional
    public void queNoSePuedanAgregarDosVecesElMismoAmigo() throws AmistadesException {
        // GIVEN
        Usuario usuario = new Usuario();
        Usuario amigo1 = new Usuario();
        //  repositorioAmistad.agregarAmigo(usuario, amigo1);
        Amistad amistad = new Amistad(usuario, amigo1);
        sessionFactory.getCurrentSession().save(usuario);
        sessionFactory.getCurrentSession().save(amigo1);
        sessionFactory.getCurrentSession().save(amistad);
        assertThrows(AmistadesException.class, () -> {
            repositorioAmistad.agregarAmigo(amistad);

        });


    }

}
