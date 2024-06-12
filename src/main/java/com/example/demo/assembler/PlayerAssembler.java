package com.example.demo.assembler;

import com.example.demo.DTO.PlayerDTO;
import com.example.demo.model.Player;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class PlayerAssembler {
    // ModelMapper se utiliza para mapear entre entidades y DTOs
    private final ModelMapper modelMapper;

    // Constructor que recibe un ModelMapper como parámetro
    public PlayerAssembler(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    // Método para convertir una entidad Player a un DTO PlayerDTO
    public PlayerDTO toDTO(Player entity) {
        return modelMapper.map(entity, PlayerDTO.class);
    }

    // Método para convertir un DTO PlayerDTO a una entidad Player
    public Player toEntity(PlayerDTO dto) {
        return modelMapper.map(dto, Player.class);
    }
}
