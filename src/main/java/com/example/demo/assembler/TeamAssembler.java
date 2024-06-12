package com.example.demo.assembler;

import com.example.demo.DTO.TeamDTO;
import com.example.demo.model.Team;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class TeamAssembler {
    // ModelMapper se utiliza para mapear entre entidades y DTOs
    private final ModelMapper modelMapper;

    // Constructor que recibe un ModelMapper como parámetro
    public TeamAssembler(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    // Método para convertir una entidad Team a un DTO TeamDTO
    public TeamDTO toDTO(Team entity) {
        return modelMapper.map(entity, TeamDTO.class);
    }

    // Método para convertir un DTO TeamDTO a una entidad Team
    public Team toEntity(TeamDTO dto) {
        return modelMapper.map(dto, Team.class);
    }

    // Método para actualizar una entidad Team existente con los datos de un DTO TeamDTO
    public Team toEntity(TeamDTO dto, Team entity) {
        // Mapear los datos del DTO al objeto de entidad existente
        modelMapper.map(dto, entity);
        return entity;
    }
}

