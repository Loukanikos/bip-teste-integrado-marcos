package com.example.backend;

import org.springframework.stereotype.Component;
import com.example.ejb.BeneficioEjbServiceLocal;

import javax.naming.InitialContext;

@Component
public class BeneficioEjbAdapter {

    private final BeneficioEjbServiceLocal service;

    public BeneficioEjbAdapter() {
        try {
            InitialContext ctx = new InitialContext();
            this.service = (BeneficioEjbServiceLocal) ctx.lookup(
                    "java:module/BeneficioEjbService!com.example.ejb.BeneficioEjbServiceLocal");
        } catch (Exception e) {
            throw new RuntimeException("Erro ao localizar EJB", e);
        }
    }

    public BeneficioEjbServiceLocal getService() {
        return service;
    }
}
