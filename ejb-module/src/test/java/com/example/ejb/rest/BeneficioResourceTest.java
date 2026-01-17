package com.example.ejb.rest;

import com.example.ejb.BeneficioEjbService;
import com.example.ejb.model.Beneficio;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BeneficioResourceTest {

    @Mock
    private BeneficioEjbService service;

    @InjectMocks
    private BeneficioResource resource;

    private Beneficio beneficio;

    @BeforeEach
    void setUp() {
        beneficio = new Beneficio();
        beneficio.setId(1L);
        beneficio.setNome("Teste");
        beneficio.setValor(new BigDecimal("100.00"));
    }

    @Test
    void testCreate() {
        when(service.create(any(Beneficio.class))).thenReturn(beneficio);
        try {
            Response response = resource.create(beneficio);

            assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
            assertEquals(beneficio, response.getEntity());
            verify(service).create(beneficio);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testFindAll() {
        List<Beneficio> lista = Arrays.asList(beneficio);
        when(service.findAll()).thenReturn(lista);

        List<Beneficio> result = resource.findAll();

        assertEquals(1, result.size());
        assertEquals(beneficio, result.get(0));
        verify(service).findAll();
    }

    @Test
    void testFindById() {
        when(service.findById(1L)).thenReturn(beneficio);

        Beneficio result = resource.findById(1L);

        assertNotNull(result);
        assertEquals(beneficio, result);
        verify(service).findById(1L);
    }

    @Test
    void testUpdate() {
        when(service.update(any(Beneficio.class))).thenReturn(beneficio);

        Beneficio result = resource.update(1L, beneficio);

        assertEquals(1L, beneficio.getId()); // Verifica se o ID foi setado
        assertNotNull(result);
        verify(service).update(beneficio);
    }

    @Test
    void testDelete() {
        doNothing().when(service).delete(1L);

        Response response = resource.delete(1L);

        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
        verify(service).delete(1L);
    }

    @Test
    void testTransfer() {
        doNothing().when(service).transfer(anyLong(), anyLong(), any(BigDecimal.class));

        Response response = resource.transfer(1L, 2L, new BigDecimal("50.00"));

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals("Transferência realizada com sucesso", response.getEntity());
        verify(service).transfer(1L, 2L, new BigDecimal("50.00"));
    }
}
