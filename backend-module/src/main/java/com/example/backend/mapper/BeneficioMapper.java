package com.example.backend.mapper;

import com.example.backend.dto.BeneficioDTO;
import com.example.model.Beneficio;
import org.springframework.stereotype.Component;

@Component
public class BeneficioMapper {

    public BeneficioDTO toDTO(Beneficio entity) {
        if (entity == null) {
            return null;
        }
        BeneficioDTO dto = new BeneficioDTO();
        dto.setId(entity.getId());
        dto.setNome(entity.getNome());
        dto.setValor(entity.getValor());
        return dto;
    }

    public Beneficio toEntity(BeneficioDTO dto) {
        if (dto == null) {
            return null;
        }
        Beneficio entity = new Beneficio();
        entity.setId(dto.getId());
        entity.setNome(dto.getNome());
        entity.setValor(dto.getValor());
        return entity;
    }
}
