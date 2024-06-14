package com.example.demo;

import com.example.demo.DTO.TeamDTO;
import com.example.demo.assembler.TeamAssembler;
import com.example.demo.model.Team;
import com.example.demo.repository.TeamRepository;
import com.example.demo.service.TeamService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class TeamServiceTests {

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private TeamAssembler teamAssembler;

    @InjectMocks
    private TeamService teamService;

    @Test
    public void testGetAllTeams() {
        // Datos simulados
        Team team1 = new Team();
        team1.setTeamId(1L);
        Team team2 = new Team();
        team2.setTeamId(2L);

        // Simulación del repositorio para devolver los equipos simulados
        when(teamRepository.findAll()).thenReturn(List.of(team1, team2));

        // Simulación del assembler para convertir Team a TeamDTO
        when(teamAssembler.toDTO(team1)).thenReturn(new TeamDTO());
        when(teamAssembler.toDTO(team2)).thenReturn(new TeamDTO());

        // Ejecución del método del servicio
        List<TeamDTO> result = teamService.getAllTeams();

        // Verificación de que se llamó al método findAll del repositorio
        verify(teamRepository, times(1)).findAll();

        // Verificación del tamaño de la lista resultante
        assertEquals(2, result.size());
    }

    @Test
    public void testGetTeamById_ExistingTeam() {
        // Datos simulados
        Long teamId = 1L;
        Team team = new Team();
        team.setTeamId(teamId);

        // Simulación del repositorio para devolver el equipo simulado
        when(teamRepository.findById(teamId)).thenReturn(Optional.of(team));

        // Simulación del assembler para convertir Team a TeamDTO
        TeamDTO teamDTO = new TeamDTO();
        teamDTO.setTeamId(teamId);
        when(teamAssembler.toDTO(team)).thenReturn(teamDTO);

        // Ejecución del método del servicio
        TeamDTO result = teamService.getTeamById(teamId);

        // Verificación de que se llamó al método findById del repositorio
        verify(teamRepository, times(1)).findById(teamId);

        // Verificación del DTO retornado
        assertEquals(teamId, result.getTeamId());
    }

    @Test
    public void testGetTeamById_NonExistingTeam() {
        // Datos simulados con un ID que no existe
        Long nonExistingId = 100L;

        // Simulación del repositorio para devolver un Optional vacío
        when(teamRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        // Ejecución del método y verificación de la excepción esperada
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> teamService.getTeamById(nonExistingId));
        assertEquals("Team with ID " + nonExistingId + " not found", exception.getMessage());

        // Verificación de que se llamó al método findById del repositorio
        verify(teamRepository, times(1)).findById(nonExistingId);
    }

    @Test
    public void testGetTeamById_NullId() {
        // Datos simulados con un ID nulo
        Long nullId = null;

        // Ejecución del método y verificación de la excepción esperada
        assertThrows(IllegalArgumentException.class, () -> teamService.getTeamById(nullId));

        // Verificación de que no se llamó al método findById del repositorio
        verify(teamRepository, never()).findById(any());
    }

    @Test
    public void testCreateTeam() {
        // Datos simulados
        TeamDTO teamDTO = new TeamDTO();
        teamDTO.setTeamId(1L);
        teamDTO.setTeamEmail("test@example.com");

        Team teamEntity = new Team();
        teamEntity.setTeamId(1L);

        // Configuración de la simulación
        when(teamAssembler.toEntity(teamDTO)).thenReturn(teamEntity);
        when(teamRepository.save(teamEntity)).thenReturn(teamEntity);
        when(teamAssembler.toDTO(teamEntity)).thenReturn(teamDTO);

        // Ejecución del método del servicio
        TeamDTO result = teamService.createTeam(teamDTO);

        // Verificación de que se devuelva el DTO correcto del equipo guardado
        assertEquals(teamDTO.getTeamId(), result.getTeamId());

        // Verificación de llamadas a métodos del repositorio y del assembler
        verify(teamRepository, times(1)).save(teamEntity);
        verify(teamAssembler, times(1)).toEntity(teamDTO);
        verify(teamAssembler, times(1)).toDTO(teamEntity);
    }

    @Test
    public void testUpdateTeam() {
        // Datos simulados
        Long teamId = 1L;
        Team existingTeam = new Team();
        existingTeam.setTeamId(teamId);

        TeamDTO updatedTeamDTO = new TeamDTO();
        updatedTeamDTO.setTeamId(teamId);

        // Configuración de la simulación
        when(teamRepository.findById(teamId)).thenReturn(Optional.of(existingTeam));
        doAnswer(invocation -> {
            Team team = invocation.getArgument(1);
            team.setTeamEmail(updatedTeamDTO.getTeamEmail());
            return null;
        }).when(teamAssembler).toEntity(updatedTeamDTO, existingTeam);
        when(teamRepository.save(existingTeam)).thenReturn(existingTeam);
        when(teamAssembler.toDTO(existingTeam)).thenReturn(updatedTeamDTO);

        // Ejecución del método del servicio
        TeamDTO result = teamService.updateTeam(teamId, updatedTeamDTO);

        // Verificación de que se devuelva el DTO correcto del equipo actualizado
        assertEquals(updatedTeamDTO.getTeamId(), result.getTeamId());

        // Verificación de llamadas a métodos del repositorio y del assembler
        verify(teamRepository, times(1)).findById(teamId);
        verify(teamRepository, times(1)).save(existingTeam);
        verify(teamAssembler, times(1)).toEntity(updatedTeamDTO, existingTeam);
        verify(teamAssembler, times(1)).toDTO(existingTeam);
    }

    @Test
    public void testDeleteTeam() {
        // Datos simulados
        Long teamId = 1L;
        Team team = new Team();
        team.setTeamId(teamId);

        // Configuración de la simulación
        when(teamRepository.findById(teamId)).thenReturn(Optional.of(team));

        // Ejecución del método del servicio
        teamService.deleteTeam(teamId);

        // Verificación de llamadas a métodos del repositorio
        verify(teamRepository, times(1)).findById(teamId);
        verify(teamRepository, times(1)).deleteById(teamId);
    }

    @Test
    public void testDeleteTeam_NonExistingTeam() {
        // Datos simulados con un ID que no existe
        Long nonExistingId = 100L;

        // Simulación del repositorio para devolver un Optional vacío
        when(teamRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        // Ejecución del método y verificación de la excepción esperada
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> teamService.deleteTeam(nonExistingId));
        assertEquals("Team with ID " + nonExistingId + " not found", exception.getMessage());

        // Verificación de que se llamó al método findById del repositorio
        verify(teamRepository, times(1)).findById(nonExistingId);
    }
}
