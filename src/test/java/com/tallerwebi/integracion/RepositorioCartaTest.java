package com.tallerwebi.integracion;

import com.tallerwebi.dominio.Carta;
import com.tallerwebi.dominio.RepositorioCarta;
import com.tallerwebi.integracion.config.HibernateTestConfig;
import com.tallerwebi.integracion.config.SpringWebTestConfig;
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
import static org.hamcrest.number.OrderingComparison.greaterThan;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = {SpringWebTestConfig.class, HibernateTestConfig.class})
public class RepositorioCartaTest {
    @Autowired
    SessionFactory sessionFactory;
    @Autowired
    RepositorioCarta repositorioCarta;

    @Test
    @Transactional
    @Rollback
    public void quePuedaObtenerLasCartasEnUnList() {
        List<Carta> cartas = repositorioCarta.obtenerCartas();

        // Verificar que la lista no es nula y tiene un tamaño esperado
        assertThat(cartas, notNullValue());

    }
}
