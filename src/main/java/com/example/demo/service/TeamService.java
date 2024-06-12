package com.example.demo.service;

import com.example.demo.DTO.TeamDTO;
import com.example.demo.assembler.TeamAssembler;
import com.example.demo.model.Team;
import com.example.demo.repository.TeamRepository;
import com.example.demo.utils.Validation;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeamService {
    private final TeamRepository teamRepository;
    private final TeamAssembler teamAssembler;

    public TeamService(TeamRepository teamRepository, TeamAssembler teamAssembler) {
        this.teamRepository = teamRepository;
        this.teamAssembler = teamAssembler;
    }

    // Obtiene todos los equipos y los convierte en una lista de DTOs
    public List<TeamDTO> getAllTeams() {
        List<Team> teams = teamRepository.findAll();
        return teams.stream().map(teamAssembler::toDTO).collect(Collectors.toList());
    }

    // Obtiene un equipo por su ID y lo convierte en un DTO
    public TeamDTO getTeamById(Long id) {
        // Verificar que el ID no sea null
        if (id == null) {
            throw new IllegalArgumentException("Team ID cannot be null");
        }

        // Buscar el jugador por el ID en el repositorio
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Team with ID " + id + " not found"));

        // Convertir el Team a un DTO y devolver
        return teamAssembler.toDTO(team);
    }

    // Crea un nuevo equipo a partir de un DTO y lo guarda en la base de datos
    public TeamDTO createTeam(TeamDTO teamDTO) {
        // Validar el email
        if (!Validation.isValidEmail(teamDTO.getTeamEmail())) {
            throw new IllegalArgumentException("Invalid email format");
        }
        // Convertir DTO de Equipo a una entidad Equipo
        Team team = teamAssembler.toEntity(teamDTO);
        // Guardar el equipo en el repositorio y devolver el DTO del equipo guardado
        Team savedTeam = teamRepository.save(team);
        return teamAssembler.toDTO(savedTeam);
    }

    public TeamDTO updateTeam(Long teamId, TeamDTO updatedTeamDTO) {
        // Busca el equipo existente por su ID
        Team existingTeam = teamRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException("Team not found with id: " + teamId));

        // Utiliza TeamAssembler para actualizar los datos del equipo existente con los datos del DTO
        teamAssembler.toEntity(updatedTeamDTO, existingTeam);

        // Guarda el equipo actualizado en la base de datos
        existingTeam = teamRepository.save(existingTeam);

        // Convierte el equipo actualizado a un DTO y lo retorna
        return teamAssembler.toDTO(existingTeam);
    }


    // Elimina un equipo por su ID
    public void deleteTeam(Long teamId) {
        teamRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException("Team with ID " + teamId + " not found"));
        teamRepository.deleteById(teamId);
    }
}
