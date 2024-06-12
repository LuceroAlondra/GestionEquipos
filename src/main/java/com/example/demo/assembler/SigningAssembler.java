package com.example.demo.assembler;

import com.example.demo.DTO.SigningDTO;
import com.example.demo.model.Signing;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class SigningAssembler {
    private final ModelMapper modelMapper;

    public SigningAssembler(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    // Metodo para convertir una entidad Signing a SigningDTO
    public SigningDTO toDTO(Signing entity) {
        return modelMapper.map(entity, SigningDTO.class);
    }

    // Metodo para convertir un SigningDTO a una entidad Signing
    public Signing toEntity(SigningDTO dto) {
        return modelMapper.map(dto, Signing.class);
    }

    // Método para actualizar una entidad Signing existente con los datos de un DTO SigningDTO
    public Signing toEntity(SigningDTO dto, Signing entity) {
        // Mapear los datos del DTO al objet de entidad existente
        modelMapper.map(dto, entity);
        return entity;
    }
}
