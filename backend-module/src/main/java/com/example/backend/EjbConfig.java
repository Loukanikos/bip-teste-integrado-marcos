package com.example.backend;

import com.example.ejb.BeneficioEjbService;
import com.example.ejb.BeneficioEjbServiceLocal;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Configuration
public class EjbConfig {

    @PersistenceContext
    private EntityManager entityManager;

    @Bean
    public BeneficioEjbServiceLocal beneficioEjbService() {
        return new BeneficioEjbService(entityManager);
    }
}
