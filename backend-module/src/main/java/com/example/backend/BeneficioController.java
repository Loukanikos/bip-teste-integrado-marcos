package com.example.backend;

import com.example.backend.dto.BeneficioDTO;
import com.example.backend.mapper.BeneficioMapper;
import com.example.model.Beneficio;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/beneficios")
@Tag(name = "Benefícios", description = "Endpoints para gerenciamento de benefícios")
public class BeneficioController {

    private final BeneficioEjbAdapter beneficioEjbAdapter;
    private final BeneficioMapper beneficioMapper;

    public BeneficioController(@Lazy BeneficioEjbAdapter beneficioEjbAdapter, BeneficioMapper beneficioMapper) {
        this.beneficioEjbAdapter = beneficioEjbAdapter;
        this.beneficioMapper = beneficioMapper;
    }

    @GetMapping
    @Operation(summary = "Listar todos os benefícios")
    public List<BeneficioDTO> list() {
        return beneficioEjbAdapter.getService().findAll().stream()
                .map(beneficioMapper::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar benefício por ID")
    public BeneficioDTO getById(@PathVariable("id") Long id) {
        Beneficio beneficio = beneficioEjbAdapter.getService().findById(id);
        return beneficioMapper.toDTO(beneficio);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Criar um novo benefício")
    public BeneficioDTO create(@Valid @RequestBody BeneficioDTO beneficioDTO) {
        Beneficio beneficio = beneficioMapper.toEntity(beneficioDTO);
        Beneficio created = beneficioEjbAdapter.getService().create(beneficio);
        return beneficioMapper.toDTO(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar um benefício existente")
    public BeneficioDTO update(@PathVariable("id") Long id, @Valid @RequestBody BeneficioDTO beneficioDTO) {
        beneficioDTO.setId(id);
        Beneficio beneficio = beneficioMapper.toEntity(beneficioDTO);
        Beneficio updated = beneficioEjbAdapter.getService().update(beneficio);
        return beneficioMapper.toDTO(updated);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Excluír um benefício")
    public void delete(@PathVariable("id") Long id) {
        beneficioEjbAdapter.getService().delete(id);
    }

    @PostMapping("/transfer")
    @Operation(summary = "Transferir saldo entre benefícios")
    public void transfer(@Valid @RequestBody TransferRequest request) {
        beneficioEjbAdapter.getService().transfer(
                request.getFromId(),
                request.getToId(),
                request.getAmount());
    }

    public static class TransferRequest {
        @NotNull(message = "ID de origem é obrigatório")
        private Long fromId;

        @NotNull(message = "ID de destino é obrigatório")
        private Long toId;

        @NotNull(message = "Valor é obrigatório")
        @Positive(message = "Valor deve ser positivo")
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
