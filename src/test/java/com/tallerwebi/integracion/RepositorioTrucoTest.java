package com.tallerwebi.integracion;

import com.tallerwebi.dominio.RepositorioTruco;
import com.tallerwebi.dominio.Partida;
import com.tallerwebi.integracion.config.HibernateTestConfig;
import com.tallerwebi.integracion.config.SpringWebTestConfig;
import com.tallerwebi.integracion.config.WebSocketConfig;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.transaction.Transactional;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(
        classes = {SpringWebTestConfig.class,
                HibernateTestConfig.class,
                WebSocketConfig.class})
public class RepositorioTrucoTest {
    @Autowired
    SessionFactory sessionFactory;
    @Autowired
    RepositorioTruco repositorioTruco;

    @Test
    @Transactional
    @Rollback
    public void queSeGuardeUnaPartida() {
        Partida truco = new Partida();
        sessionFactory.getCurrentSession().save(truco);
//        repositorioTruco.guardarPartida(truco);
        assertThat(truco.getId(), notNullValue());

    }
}
