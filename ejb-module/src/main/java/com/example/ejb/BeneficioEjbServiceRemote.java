package com.example.ejb;

import com.example.model.Beneficio;
import jakarta.ejb.Remote;
import java.math.BigDecimal;
import java.util.List;

@Remote
public interface BeneficioEjbServiceRemote {
    Beneficio create(Beneficio beneficio);

    Beneficio findById(Long id);

    List<Beneficio> findAll();

    Beneficio update(Beneficio beneficio);

    void delete(Long id);

    void transfer(Long fromId, Long toId, BigDecimal amount);

    void doTransfer(Long fromId, Long toId, BigDecimal amount);
}
