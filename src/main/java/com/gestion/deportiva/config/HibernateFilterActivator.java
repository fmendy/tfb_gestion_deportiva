package com.gestion.deportiva.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.hibernate.Session;
import org.hibernate.Filter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class HibernateFilterActivator {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void enableActivoFilter() {
        Session session = entityManager.unwrap(Session.class);
        Filter filter = session.enableFilter("activoFilter");
        filter.setParameter("activo", true);
    }
}
