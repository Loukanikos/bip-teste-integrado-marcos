package com.example.backend;

import com.example.backend.dto.BeneficioDTO;
import com.example.backend.mapper.BeneficioMapper;
import com.example.ejb.BeneficioEjbServiceLocal;
import com.example.model.Beneficio;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BeneficioController.class)
@Import(BeneficioMapper.class)
class BeneficioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BeneficioEjbAdapter beneficioEjbAdapter;

    @MockBean
    private BeneficioEjbServiceLocal beneficioEjbService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        when(beneficioEjbAdapter.getService()).thenReturn(beneficioEjbService);
    }

    @Test
    void testList() throws Exception {
        Beneficio b1 = new Beneficio();
        b1.setId(1L);
        b1.setNome("Vale Alimentação");

        Beneficio b2 = new Beneficio();
        b2.setId(2L);
        b2.setNome("Vale Refeição");

        when(beneficioEjbService.findAll()).thenReturn(Arrays.asList(b1, b2));

        mockMvc.perform(get("/api/v1/beneficios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].nome").value("Vale Alimentação"))
                .andExpect(jsonPath("$[1].nome").value("Vale Refeição"));
    }

    @Test
    void testGetById() throws Exception {
        Beneficio b = new Beneficio();
        b.setId(1L);
        b.setNome("Vale Alimentação");

        when(beneficioEjbService.findById(1L)).thenReturn(b);

        mockMvc.perform(get("/api/v1/beneficios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Vale Alimentação"));
    }

    @Test
    void testCreate() throws Exception {
        BeneficioDTO dto = new BeneficioDTO();
        dto.setNome("Novo Benefício");
        dto.setValor(new BigDecimal("100.00"));

        Beneficio created = new Beneficio();
        created.setId(1L);
        created.setNome("Novo Benefício");
        created.setValor(new BigDecimal("100.00"));

        when(beneficioEjbService.create(any(Beneficio.class))).thenReturn(created);

        mockMvc.perform(post("/api/v1/beneficios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Novo Benefício"));
    }

    @Test
    void testUpdate() throws Exception {
        BeneficioDTO dto = new BeneficioDTO();
        dto.setNome("Nome Atualizado");
        dto.setValor(new BigDecimal("200.00"));

        Beneficio updated = new Beneficio();
        updated.setId(1L);
        updated.setNome("Nome Atualizado");
        updated.setValor(new BigDecimal("200.00"));

        when(beneficioEjbService.update(any(Beneficio.class))).thenReturn(updated);

        mockMvc.perform(put("/api/v1/beneficios/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Nome Atualizado"));
    }

    @Test
    void testDelete() throws Exception {
        mockMvc.perform(delete("/api/v1/beneficios/1"))
                .andExpect(status().isNoContent());

        verify(beneficioEjbService, times(1)).delete(1L);
    }

    @Test
    void testTransfer() throws Exception {
        BeneficioController.TransferRequest request = new BeneficioController.TransferRequest();
        request.setFromId(1L);
        request.setToId(2L);
        request.setAmount(new BigDecimal("50.00"));

        mockMvc.perform(post("/api/v1/beneficios/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(beneficioEjbService, times(1)).transfer(eq(1L), eq(2L), any(BigDecimal.class));
    }
}
