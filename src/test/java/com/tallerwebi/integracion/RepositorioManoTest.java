//package com.tallerwebi.integracion;
//
//import com.tallerwebi.dominio.*;
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
//public class RepositorioManoTest {
//
//    @Autowired
//    SessionFactory sessionFactory;
//    @Autowired
//    RepositorioMano repositorioMano;
//
//    @Test
//    @Transactional
//    @Rollback
//    public void queSeGuardeUnaMano() {
//        Mano m = new Mano();
//        sessionFactory.getCurrentSession().save(m);
//        assertThat(m.getId(), notNullValue());
//    }
//}
