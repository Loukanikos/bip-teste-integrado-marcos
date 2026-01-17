package com.example.backend;

import com.example.model.Beneficio;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping("/api/v1/beneficios")
public class BeneficioController {

    private final BeneficioEjbAdapter beneficioEjbAdapter;

    public BeneficioController(BeneficioEjbAdapter beneficioEjbAdapter) {
        this.beneficioEjbAdapter = beneficioEjbAdapter;
    }

    @GetMapping
    public List<Beneficio> list() {
        return beneficioEjbAdapter.getService().findAll();
    }

    @GetMapping("/{id}")
    public Beneficio getById(@PathVariable("id") Long id) {
        return beneficioEjbAdapter.getService().findById(id);
    }

    @PostMapping
    public Beneficio create(@RequestBody Beneficio beneficio) {
        return beneficioEjbAdapter.getService().create(beneficio);
    }

    @PutMapping("/{id}")
    public Beneficio update(@PathVariable("id") Long id, @RequestBody Beneficio beneficio) {
        beneficio.setId(id);
        return beneficioEjbAdapter.getService().update(beneficio);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        beneficioEjbAdapter.getService().delete(id);
    }

    @PostMapping("/transfer")
    public void transfer(@RequestBody TransferRequest request) {
        beneficioEjbAdapter.getService().transfer(
                request.getFromId(),
                request.getToId(),
                request.getAmount());
    }

    public static class TransferRequest {
        private Long fromId;
        private Long toId;
        private BigDecimal amount;

        public Long getFromId() {
            return fromId;
        }

        public void setFromId(Long fromId) {
            this.fromId = fromId;
        }

        public Long getToId() {
            return toId;
        }

        public void setToId(Long toId) {
            this.toId = toId;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }
    }
}
