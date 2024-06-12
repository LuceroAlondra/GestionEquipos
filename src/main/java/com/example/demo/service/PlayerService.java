package com.example.demo.service;

import com.example.demo.DTO.PlayerDTO;
import com.example.demo.assembler.PlayerAssembler;
import com.example.demo.model.Player;
import com.example.demo.model.Signing;
import com.example.demo.model.Team;
import com.example.demo.repository.PlayerRepository;
import com.example.demo.repository.TeamRepository;
import com.example.demo.utils.Validation;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlayerService {
    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;
    private final PlayerAssembler playerAssembler;

    public PlayerService(PlayerRepository playerRepository, TeamRepository teamRepository, PlayerAssembler playerAssembler) {
        this.playerRepository = playerRepository;
        this.teamRepository = teamRepository;
        this.playerAssembler = playerAssembler;
    }

    public List<PlayerDTO> getAllPlayers() {
        List<Player> players = playerRepository.findAll();
        return players.stream().map(playerAssembler::toDTO).collect(Collectors.toList());
    }

    public PlayerDTO getPlayerById(Long id) {
        // Verificar que el ID no sea nulo
        if (id == null) {
            throw new IllegalArgumentException("Player ID cannot be null");
        }

        // Buscar el jugador por su ID en el repositorio
        Player player = playerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Player with ID " + id + " not found"));

        // Convertir el jugador a un DTO y devolverlo
        return playerAssembler.toDTO(player);
    }

    //Cuando se manipulan datos en cadena 
    @Transactional
    public List<PlayerDTO> getAllPlayersByTeamId(Long teamId) {
        // Buscar el equipo por su ID en el repositorio
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found"));

        // Obtener todos los contratos del equipo
        List<Signing> teamSignings = team.getSignings();

        // Crear una lista para almacenar los jugadores del equipo
        List<Player> teamPlayers = new ArrayList<>();
        // Iterar sobre los contratos y agregar los jugadores asociados a la lista
        for (Signing signing : teamSignings) {
            teamPlayers.add(signing.getPlayer());
        }

        // Convertir los jugadores a DTOs y devolver la lista de DTOs de jugadores
        return teamPlayers.stream()
                .map(playerAssembler::toDTO)
                .collect(Collectors.toList());
    }

    public PlayerDTO createPlayer(PlayerDTO playerDTO) {
        // Validar el formato del correo electrónico del jugador
        if (!Validation.isValidEmail(playerDTO.getEmailPlayer())) {
            throw new IllegalArgumentException("Invalid email format");
        }
        // Convertir el DTO de jugador a una entidad Player
        Player player = playerAssembler.toEntity(playerDTO);
        // Guardar el jugador en el repositorio y devolver el DTO del jugador guardado
        Player savedPlayer = playerRepository.save(player);
        return playerAssembler.toDTO(savedPlayer);
    }

    public PlayerDTO updatePlayer(Long id, PlayerDTO playerDTO) {
        // Busca el jugador en la base de datos
        Player player = playerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Player not found"));

        // Utiliza PlayerAssembler para mapear playerDTO a una entidad Player
        player = playerAssembler.toEntity(playerDTO);

        // Actualiza el ID del jugador con el ID recuperado de la base de datos
        player.setPlayerId(id);

        // Guarda la entidad Player actualizada en la base de datos
        player = playerRepository.save(player);

        // Convierte la entidad actualizada de vuelta a un PlayerDTO y lo retorna
        return playerAssembler.toDTO(player);
    }

    public void deletePlayer(Long playerId) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new EntityNotFoundException("Player with ID " + playerId + " not found"));
        playerRepository.delete(player);
    }
}
