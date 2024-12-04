//package com.tallerwebi.integracion;
//
//import com.tallerwebi.dominio.Jugador;
//import com.tallerwebi.dominio.RepositorioTruco;
//import com.tallerwebi.dominio.Partida;
//import com.tallerwebi.integracion.config.HibernateTestConfig;
//import com.tallerwebi.integracion.config.SpringWebTestConfig;
//import org.hibernate.SessionFactory;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.annotation.Rollback;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.context.web.WebAppConfiguration;
//
//import javax.transaction.Transactional;
//
//import static org.hamcrest.MatcherAssert.assertThat;
//import static org.hamcrest.Matchers.notNullValue;
//
//@ExtendWith(SpringExtension.class)
//@WebAppConfiguration
//@ContextConfiguration(classes = {SpringWebTestConfig.class, HibernateTestConfig.class})
//public class RepositorioPartidaTest {
//
//    @Autowired
//    SessionFactory sessionFactory;
//    @Autowired
//    RepositorioTruco repositorioTruco;
//
//    @Test
//    @Transactional
//    @Rollback
//    public void queSeGuardeLaPartida() {
//        Partida t = new Partida();
//        t.setJ1(new Jugador());
//        t.setJ2(new Jugador());
//
//        sessionFactory.getCurrentSession().save(t);
//
//        assertThat(t.getId(), notNullValue());
//    }
//}
