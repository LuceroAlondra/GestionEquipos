package com.example.demo;

import com.example.demo.DTO.PlayerDTO;
import com.example.demo.assembler.PlayerAssembler;
import com.example.demo.model.Player;
import com.example.demo.repository.PlayerRepository;
import com.example.demo.repository.TeamRepository;
import com.example.demo.service.PlayerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
class GestionEquipos1ApplicationTests {

    private PlayerRepository playerRepository;
    private TeamRepository teamRepository;
    private PlayerAssembler playerAssembler;
    private PlayerService playerService;

    @BeforeEach
    public void setUp() {
        playerRepository = mock(PlayerRepository.class);
        teamRepository = mock(TeamRepository.class);
        playerAssembler = mock(PlayerAssembler.class);
        playerService = new PlayerService(playerRepository, teamRepository, playerAssembler);
    }

    @Test
    public void testGetAllPlayers() {
        // Simula la respuesta del repositorio
        List<Player> players = new ArrayList<>();
        players.add(new Player());
        players.add(new Player());
        when(playerRepository.findAll()).thenReturn(players);

        // Simula la conversión del jugador a DTO
        when(playerAssembler.toDTO(any(Player.class))).thenReturn(new PlayerDTO());

        // Ejecuta el método del servicio
        List<PlayerDTO> playerDTOs = playerService.getAllPlayers();

        // Verifica que el método devuelva la lista correcta de DTOs de jugadores
        assertEquals(players.size(), playerDTOs.size());
    }

    @Test
    public void testGetPlayerById() {
        // Simula el jugador en el repositorio
        Player player = new Player();
        player.setPlayerId(1L);
        when(playerRepository.findById(1L)).thenReturn(Optional.of(player));

        // Simula la conversión del jugador a DTO
        when(playerAssembler.toDTO(any(Player.class))).thenReturn(new PlayerDTO());

        // Ejecuta el método del servicio
        PlayerDTO playerDTO = playerService.getPlayerById(1L);

        // Verifica que el método devuelva el jugador correcto
        assertEquals(player.getPlayerId(), playerDTO.getPlayerId());
    }

}
