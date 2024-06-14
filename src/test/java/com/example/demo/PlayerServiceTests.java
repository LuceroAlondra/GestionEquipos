package com.example.demo;

import com.example.demo.DTO.PlayerDTO;
import com.example.demo.assembler.PlayerAssembler;
import com.example.demo.model.Player;
import com.example.demo.model.Signing;
import com.example.demo.model.Team;
import com.example.demo.repository.PlayerRepository;
import com.example.demo.repository.TeamRepository;
import com.example.demo.service.PlayerService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class PlayerServiceTests {

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private PlayerAssembler playerAssembler;

    @InjectMocks
    private PlayerService playerService;

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

        // Verifica que el método devuelva la lista correcta de DTOs de jugadores, las aserciones se usan para comparar el resultado obtenido durante la ejecucion con el resultado esperado
        assertEquals(players.size(), playerDTOs.size());
    }

    @Test
    public void testGetPlayerById() {
        // Simula el jugador en el repositorio
        Player player = new Player();
        player.setPlayerId(1L);
        when(playerRepository.findById(1L)).thenReturn(Optional.of(player));

        // Simula la conversión del jugador a DTO usando PlayerAssembler
        PlayerDTO playerDTO = new PlayerDTO();
        playerDTO.setPlayerId(1L);
        when(playerAssembler.toDTO(any(Player.class))).thenReturn(playerDTO);

        // Ejecuta el método del servicio
        PlayerDTO result = playerService.getPlayerById(1L);

        // Verifica que el método devuelva el jugador correcto
        assertNotNull(result);
        assertEquals(player.getPlayerId(), result.getPlayerId());
    }

    @Test
    public void testGetAllPlayersByTeamId() {
        // Preparación de datos simulados
        Long teamId = 1L;
        Team team = new Team();
        team.setTeamId(teamId);

        // Simula la búsqueda del equipo por ID en el repositorio
        when(teamRepository.findById(teamId)).thenReturn(Optional.of(team));

        // Simula los jugadores asociados al equipo
        List<Signing> signings = new ArrayList<>();
        Player player1 = new Player();
        player1.setPlayerId(1L);
        Player player2 = new Player();
        player2.setPlayerId(2L);
        signings.add(new Signing(player1));
        signings.add(new Signing(player2));
        team.setSignings(signings);

        // Simula la conversión del jugador a DTO usando PlayerAssembler
        PlayerDTO playerDTO1 = new PlayerDTO();
        playerDTO1.setPlayerId(1L);
        PlayerDTO playerDTO2 = new PlayerDTO();
        playerDTO2.setPlayerId(2L);
        when(playerAssembler.toDTO(player1)).thenReturn(playerDTO1);
        when(playerAssembler.toDTO(player2)).thenReturn(playerDTO2);

        // Ejecuta el método del servicio
        List<PlayerDTO> result = playerService.getAllPlayersByTeamId(teamId);

        // Verifica que el método devuelva la lista correcta de DTOs de jugadores
        assertEquals(2, result.size());
        assertEquals(player1.getPlayerId(), result.get(0).getPlayerId());
        assertEquals(player2.getPlayerId(), result.get(1).getPlayerId());
    }

    @Test
    public void testCreatePlayer_ValidEmail() {
        // Datos simulados
        PlayerDTO playerDTO = new PlayerDTO();
        playerDTO.setEmailPlayer("example@example.com");

        // Simulación de la conversión de DTO a entidad y viceversa
        Player playerEntity = new Player();
        playerEntity.setPlayerId(1L);
        when(playerAssembler.toEntity(playerDTO)).thenReturn(playerEntity);

        Player savedPlayerEntity = new Player();
        savedPlayerEntity.setPlayerId(1L);
        when(playerRepository.save(playerEntity)).thenReturn(savedPlayerEntity);

        PlayerDTO savedPlayerDTO = new PlayerDTO();
        savedPlayerDTO.setPlayerId(1L);
        when(playerAssembler.toDTO(savedPlayerEntity)).thenReturn(savedPlayerDTO);

        // Ejecución del método
        PlayerDTO result = playerService.createPlayer(playerDTO);

        // Verificación de la llamada al repositorio
        verify(playerRepository, times(1)).save(playerEntity);

        // Verificación de la conversión de entidad a DTO
        assertEquals(savedPlayerDTO, result);
    }

    @Test
    public void testCreatePlayer_InvalidEmail() {
        // Datos simulados con un correo electrónico inválido
        PlayerDTO playerDTO = new PlayerDTO();
        playerDTO.setEmailPlayer("invalidemail");

        // Ejecución del método y verificación de la excepción esperada
        assertThrows(IllegalArgumentException.class, () -> playerService.createPlayer(playerDTO));

        // Verificación de que no se llame al repositorio si hay un correo electrónico inválido
        verify(playerRepository, never()).save(any(Player.class));
    }

    @Test
    public void testUpdatePlayer() {
        // Datos simulados
        Long playerId = 1L;

        // DTO del jugador con los datos actualizados
        PlayerDTO updatedPlayerDTO = new PlayerDTO();
        updatedPlayerDTO.setPlayerId(playerId);
        updatedPlayerDTO.setFirstName("Updated First Name");
        updatedPlayerDTO.setLastName("Updated Last Name");
        updatedPlayerDTO.setEmailPlayer("updated@example.com");
        updatedPlayerDTO.setBirthdate(new Date());
        updatedPlayerDTO.setPosition("Forward");
        updatedPlayerDTO.setGender("Male");
        updatedPlayerDTO.setWeight(75.0);
        updatedPlayerDTO.setHigh(180.0);
        updatedPlayerDTO.setImc(23.1);
        updatedPlayerDTO.setFat(15.5);

        // Simulación de la búsqueda del jugador por ID en el repositorio
        Player existingPlayer = new Player();
        existingPlayer.setPlayerId(playerId);
        existingPlayer.setFirstName("Old First Name");
        existingPlayer.setLastName("Old Last Name");
        existingPlayer.setEmailPlayer("old@example.com");
        existingPlayer.setBirthdate(new Date());
        existingPlayer.setPosition("Midfielder");
        existingPlayer.setGender("Female");
        existingPlayer.setWeight(70.0);
        existingPlayer.setHigh(175.0);
        existingPlayer.setImc(22.5);
        existingPlayer.setFat(16.0);

        when(playerRepository.findById(playerId)).thenReturn(Optional.of(existingPlayer));

        // Simulación de la conversión de DTO a entidad y viceversa
        Player updatedPlayerEntity = new Player();
        updatedPlayerEntity.setPlayerId(playerId);
        updatedPlayerEntity.setFirstName(updatedPlayerDTO.getFirstName());
        updatedPlayerEntity.setLastName(updatedPlayerDTO.getLastName());
        updatedPlayerEntity.setEmailPlayer(updatedPlayerDTO.getEmailPlayer());
        updatedPlayerEntity.setBirthdate(updatedPlayerDTO.getBirthdate());
        updatedPlayerEntity.setPosition(updatedPlayerDTO.getPosition());
        updatedPlayerEntity.setGender(updatedPlayerDTO.getGender());
        updatedPlayerEntity.setWeight(updatedPlayerDTO.getWeight());
        updatedPlayerEntity.setHigh(updatedPlayerDTO.getHigh());
        updatedPlayerEntity.setImc(updatedPlayerDTO.getImc());
        updatedPlayerEntity.setFat(updatedPlayerDTO.getFat());

        when(playerAssembler.toEntity(updatedPlayerDTO)).thenReturn(updatedPlayerEntity);

        Player savedPlayerEntity = new Player();
        savedPlayerEntity.setPlayerId(playerId);
        savedPlayerEntity.setFirstName(updatedPlayerDTO.getFirstName());
        savedPlayerEntity.setLastName(updatedPlayerDTO.getLastName());
        savedPlayerEntity.setEmailPlayer(updatedPlayerDTO.getEmailPlayer());
        savedPlayerEntity.setBirthdate(updatedPlayerDTO.getBirthdate());
        savedPlayerEntity.setPosition(updatedPlayerDTO.getPosition());
        savedPlayerEntity.setGender(updatedPlayerDTO.getGender());
        savedPlayerEntity.setWeight(updatedPlayerDTO.getWeight());
        savedPlayerEntity.setHigh(updatedPlayerDTO.getHigh());
        savedPlayerEntity.setImc(updatedPlayerDTO.getImc());
        savedPlayerEntity.setFat(updatedPlayerDTO.getFat());

        when(playerRepository.save(updatedPlayerEntity)).thenReturn(savedPlayerEntity);

        PlayerDTO savedPlayerDTO = new PlayerDTO();
        savedPlayerDTO.setPlayerId(playerId);
        savedPlayerDTO.setFirstName(updatedPlayerDTO.getFirstName());
        savedPlayerDTO.setLastName(updatedPlayerDTO.getLastName());
        savedPlayerDTO.setEmailPlayer(updatedPlayerDTO.getEmailPlayer());
        savedPlayerDTO.setBirthdate(updatedPlayerDTO.getBirthdate());
        savedPlayerDTO.setPosition(updatedPlayerDTO.getPosition());
        savedPlayerDTO.setGender(updatedPlayerDTO.getGender());
        savedPlayerDTO.setWeight(updatedPlayerDTO.getWeight());
        savedPlayerDTO.setHigh(updatedPlayerDTO.getHigh());
        savedPlayerDTO.setImc(updatedPlayerDTO.getImc());
        savedPlayerDTO.setFat(updatedPlayerDTO.getFat());

        when(playerAssembler.toDTO(savedPlayerEntity)).thenReturn(savedPlayerDTO);

        // Ejecución del método
        PlayerDTO result = playerService.updatePlayer(playerId, updatedPlayerDTO);

        // Verificación de la llamada al repositorio
        verify(playerRepository, times(1)).save(updatedPlayerEntity);

        // Verificación de la conversión de entidad a DTO
        assertEquals(savedPlayerDTO, result);
    }

    @Test
    public void testUpdatePlayer_PlayerNotFound() {
        // Datos simulados con un jugador que no existe
        Long playerId = 1L;
        PlayerDTO updatedPlayerDTO = new PlayerDTO();
        updatedPlayerDTO.setPlayerId(playerId);
        updatedPlayerDTO.setFirstName("Updated First Name");

        // Simulación de que no se encuentra el jugador por ID en el repositorio
        when(playerRepository.findById(playerId)).thenReturn(Optional.empty());

        // Ejecución del método y verificación de la excepción esperada
        assertThrows(RuntimeException.class, () -> playerService.updatePlayer(playerId, updatedPlayerDTO));

        // Verificación de que no se llama al repositorio si el jugador no existe
        verify(playerRepository, never()).save(any(Player.class));
    }

    @Test
    public void testDeletePlayer() {
        // Datos simulados
        Long playerId = 1L;

        // Simulación de la búsqueda del jugador por ID en el repositorio
        Player player = new Player();
        player.setPlayerId(playerId);

        when(playerRepository.findById(playerId)).thenReturn(Optional.of(player));

        // Ejecución del método
        playerService.deletePlayer(playerId);

        // Verificación de la llamada al método delete del repositorio
        verify(playerRepository, times(1)).delete(player);
    }

    @Test
    public void testDeletePlayer_PlayerNotFound() {
        // Datos simulados con un jugador que no existe
        Long playerId = 1L;

        // Simulación de que no se encuentra el jugador por ID en el repositorio
        when(playerRepository.findById(playerId)).thenReturn(Optional.empty());

        // Ejecución del método y verificación de la excepción esperada
        assertThrows(EntityNotFoundException.class, () -> playerService.deletePlayer(playerId));

        // Verificación de que no se llama al método delete del repositorio si el jugador no existe
        verify(playerRepository, never()).delete(any(Player.class));
    }
}
