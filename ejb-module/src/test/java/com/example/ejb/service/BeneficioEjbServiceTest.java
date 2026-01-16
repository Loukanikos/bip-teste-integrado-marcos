package com.example.ejb.service;

import com.example.ejb.BeneficioEjbService;
import com.example.ejb.exception.BeneficioException;
import com.example.ejb.model.Beneficio;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BeneficioEjbServiceTest {

    private EntityManager em;
    private BeneficioEjbService service;

    @BeforeEach
    void setup() {
        em = Mockito.mock(EntityManager.class);
        service = new BeneficioEjbService();
        service.setEntityManager(em); // ✅ injeta o mock
    }

    // CREATE
    @Test
    void testCreateBeneficio() {
        Beneficio beneficio = new Beneficio(null, "Vale Alimentação", new BigDecimal("500.00"));
        service.create(beneficio);
        verify(em, times(1)).persist(beneficio);
    }

    // READ - findById
    @Test
    void testFindById() {
        Beneficio beneficio = new Beneficio(1L, "Vale Transporte", new BigDecimal("300.00"));
        when(em.find(Beneficio.class, 1L)).thenReturn(beneficio);

        Beneficio result = service.findById(1L);

        assertNotNull(result);
        assertEquals("Vale Transporte", result.getNome());
    }

    // READ - findAll
    @Test
    void testFindAll() {
        Beneficio b1 = new Beneficio(1L, "Vale Alimentação", new BigDecimal("500.00"));
        Beneficio b2 = new Beneficio(2L, "Vale Transporte", new BigDecimal("300.00"));

        TypedQuery<Beneficio> query = Mockito.mock(TypedQuery.class);
        when(em.createQuery("SELECT b FROM Beneficio b", Beneficio.class)).thenReturn(query);
        when(query.getResultList()).thenReturn(Arrays.asList(b1, b2));

        List<Beneficio> result = service.findAll();

        assertEquals(2, result.size());
        assertEquals("Vale Alimentação", result.get(0).getNome());
    }

    // UPDATE
    @Test
    void testUpdateBeneficio() {
        Beneficio beneficio = new Beneficio(1L, "Vale Alimentação", new BigDecimal("500.00"));
        when(em.merge(beneficio)).thenReturn(beneficio);

        Beneficio updated = service.update(beneficio);

        assertNotNull(updated);
        verify(em, times(1)).merge(beneficio);
    }

    // DELETE
    @Test
    void testDeleteBeneficio() {
        Beneficio beneficio = new Beneficio(1L, "Vale Transporte", new BigDecimal("300.00"));
        when(em.find(Beneficio.class, 1L)).thenReturn(beneficio);

        service.delete(1L);

        verify(em, times(1)).remove(beneficio);
    }

    // DELETE - not found
    @Test
    void testDeleteBeneficioNotFound() {
        when(em.find(Beneficio.class, 99L)).thenReturn(null);

        assertThrows(BeneficioException.class, () -> service.delete(99L));
    }


}