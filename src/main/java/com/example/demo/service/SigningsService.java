package com.example.demo.service;

import com.example.demo.DTO.SigningDTO;
import com.example.demo.assembler.SigningAssembler;
import com.example.demo.model.Player;
import com.example.demo.model.Signing;
import com.example.demo.model.Team;
import com.example.demo.repository.PlayerRepository;
import com.example.demo.repository.SigningsRepository;
import com.example.demo.repository.TeamRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SigningsService {
    private final SigningsRepository signingsRepository;
    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;
    private final SigningAssembler signingAssembler;

    public SigningsService(SigningsRepository signingsRepository, PlayerRepository playerRepository,
                           TeamRepository teamRepository, SigningAssembler signingAssembler) {
        this.signingsRepository = signingsRepository;
        this.playerRepository = playerRepository;
        this.teamRepository = teamRepository;
        this.signingAssembler = signingAssembler;
    }

    public List<SigningDTO> getAllSignings() {
        List<Signing> allSignings = signingsRepository.findAll();
        return allSignings.stream().map(signingAssembler::toDTO).collect(Collectors.toList());
    }

    public SigningDTO findById(Long id) {
        // Verificar si no es nilo
        if (id == null) {
            throw new IllegalArgumentException("Signing ID cannot be null");
        }

        // Buscar el contrato por su id en el repositorio
        Signing signing = signingsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Signing not found"));

        // Convertir el contrato a un DTO y devolverlo
        return signingAssembler.toDTO(signing);
    }

    // Método para obtener todos los contratos asociados a un jugador por su ID
    public List<SigningDTO> getAllSigningsByPlayerId(Long playerId) {
        // Buscar el jugador por su ID en el repositorio
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new RuntimeException("Player not found"));

        // Obtener todos los contratos asociados a ese jugador
        List<Signing> signings = signingsRepository.findByPlayer(player);

        // Mapear los contratos a DTOs y devolver la lista de DTOs de contratos
        return signings.stream().map(signingAssembler::toDTO).collect(Collectors.toList());
    }

    public SigningDTO createSigning(SigningDTO signingDTO) {
        // Convertir el DTO de contrato a una entidad Signing
        Signing signing = signingAssembler.toEntity(signingDTO);

        // Buscar el jugador asociado al contrato por su ID en el repositorio
        Player player = playerRepository.findById(signingDTO.getPlayer().getPlayerId())
                .orElseThrow(() -> new RuntimeException("Player not found"));

        // Buscar el equipo asociado al contrato por su ID en el repositorio
        Team team = teamRepository.findById(signingDTO.getTeam().getTeamId())
                .orElseThrow(() -> new RuntimeException("Team not found"));

        // Establecer el jugador y el equipo en el contrato
        signing.setPlayer(player);
        signing.setTeam(team);

        // Guardar el contrato en el repositorio y devolver el DTO del contrato guardado
        Signing savedSigning = signingsRepository.save(signing);
        return signingAssembler.toDTO(savedSigning);
    }

    public SigningDTO updateSigning(Long id, SigningDTO signingDTO) {
        // Buscar el contrato por su ID en el repositorio de contratos
        Signing signing = signingsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Signing not found"));

        // Actualizar los datos del contrato utilizando el assembler
        signingAssembler.toEntity(signingDTO, signing);

        // Obtener el jugador asociado al contrato por su ID
        Player player = playerRepository.findById(signingDTO.getPlayer().getPlayerId())
                .orElseThrow(() -> new RuntimeException("Player not found"));

        // Obtener el equipo asociado al contrato por su ID
        Team team = teamRepository.findById(signingDTO.getTeam().getTeamId())
                .orElseThrow(() -> new RuntimeException("Team not found"));

        // Establecer el jugador y el equipo asociados al contrato
        signing.setPlayer(player);
        signing.setTeam(team);

        // Guardar los cambios en el contrato y devolver el DTO actualizado
        Signing updatedSigning = signingsRepository.save(signing);
        return signingAssembler.toDTO(updatedSigning);
    }


    public void deleteSigning(Long id) {
        Signing signing = signingsRepository.findById(id).orElseThrow(() -> new RuntimeException("Signing not found"));
        signingsRepository.delete(signing);
    }
}