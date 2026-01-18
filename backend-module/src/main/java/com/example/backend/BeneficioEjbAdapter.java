package com.example.backend;

import org.springframework.stereotype.Component;
import com.example.ejb.BeneficioEjbServiceLocal;

@Component
public class BeneficioEjbAdapter {

    private final BeneficioEjbServiceLocal service;

    public BeneficioEjbAdapter(BeneficioEjbServiceLocal service) {
        this.service = service;
    }

    public BeneficioEjbServiceLocal getService() {
        return service;
    }
}
