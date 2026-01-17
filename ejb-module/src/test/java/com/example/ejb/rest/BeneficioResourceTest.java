package com.example.ejb.rest;

import com.example.ejb.BeneficioEjbService;
import com.example.model.Beneficio;
import com.example.rest.BeneficioResource;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class BeneficioResourceTest {

    private BeneficioResource resource;
    private BeneficioEjbService service;

    @BeforeEach
    void setup() throws Exception {
        service = Mockito.mock(BeneficioEjbService.class);
        resource = new BeneficioResource();

        // injeta o mock manualmente (simulando @EJB)
        Field field = BeneficioResource.class.getDeclaredField("service");
        field.setAccessible(true);
        field.set(resource, service);
    }

    @Test
    void testDoCreate() {
        Beneficio beneficio = new Beneficio();
        beneficio.setId(1L);
        beneficio.setValor(BigDecimal.TEN);

        when(service.create(beneficio)).thenReturn(beneficio);

        Beneficio result = resource.doCreate(beneficio);

        assertEquals(1L, result.getId());
        assertEquals(BigDecimal.TEN, result.getValor());
        verify(service).create(beneficio);
    }

    @Test
    void testDoFindAll() {
        Beneficio b1 = new Beneficio();
        b1.setId(1L);
        Beneficio b2 = new Beneficio();
        b2.setId(2L);

        when(service.findAll()).thenReturn(Arrays.asList(b1, b2));

        List<Beneficio> result = resource.findAll();

        assertEquals(2, result.size());
        verify(service).findAll();
    }

    @Test
    void testDoFindById() {
        Beneficio b = new Beneficio();
        b.setId(1L);

        when(service.findById(1L)).thenReturn(b);

        Beneficio result = resource.findById(1L);

        assertEquals(1L, result.getId());
        verify(service).findById(1L);
    }

    @Test
    void testDoUpdate() {
        Beneficio b = new Beneficio();
        b.setId(1L);

        when(service.update(b)).thenReturn(b);

        Beneficio result = resource.update(1L, b);

        assertEquals(1L, result.getId());
        verify(service).update(b);
    }

    @Test
    void testDoDelete() {
        doNothing().when(service).delete(1L);

        resource.doDelete(1L);

        verify(service).delete(1L);
    }

    @Test
    void testDoTransfer() {
        doNothing().when(service).transfer(1L, 2L, BigDecimal.TEN);

        resource.doTransfer(1L, 2L, BigDecimal.TEN);

        verify(service).transfer(1L, 2L, BigDecimal.TEN);
    }

}