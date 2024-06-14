package com.example.demo;

import com.example.demo.DTO.PlayerDTO;
import com.example.demo.DTO.SigningDTO;
import com.example.demo.DTO.TeamDTO;
import com.example.demo.assembler.SigningAssembler;
import com.example.demo.model.Player;
import com.example.demo.model.Signing;
import com.example.demo.model.Team;
import com.example.demo.repository.PlayerRepository;
import com.example.demo.repository.SigningsRepository;
import com.example.demo.repository.TeamRepository;
import com.example.demo.service.SigningsService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class SigningsServiceTests {
    @Mock
    private SigningsRepository signingsRepository;

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private SigningAssembler signingAssembler;

    @InjectMocks
    private SigningsService signingsService;

    @Test
    public void testGetAllSignings() {
        // Datos simulados
        Signing signing1 = new Signing();
        signing1.setSigningsId(1L);
        Signing signing2 = new Signing();
        signing2.setSigningsId(2L);

        // Simulación del repositorio para devolver los signings simulados
        when(signingsRepository.findAll()).thenReturn(List.of(signing1, signing2));

        // Simulación del assembler para convertir Signing a SigningDTO
        when(signingAssembler.toDTO(signing1)).thenReturn(new SigningDTO());
        when(signingAssembler.toDTO(signing2)).thenReturn(new SigningDTO());

        // Ejecución del método del servicio
        List<SigningDTO> result = signingsService.getAllSignings();

        // Verificación de que se llamó al método findAll del repositorio
        verify(signingsRepository, times(1)).findAll();

        // Verificación del tamaño de la lista resultante y contenido
        assertEquals(2, result.size());
        // Puedes agregar más aserciones según sea necesario para validar los DTOs retornados
    }

    @Test
    public void testFindById_ExistingSigning() {
        // Datos simulados
        Long signingId = 1L;
        Signing signing = new Signing();
        signing.setSigningsId(signingId);

        // Simulación del repositorio para devolver el signing simulado
        when(signingsRepository.findById(signingId)).thenReturn(Optional.of(signing));

        // Simulación del assembler para convertir Signing a SigningDTO
        SigningDTO signingDTO = new SigningDTO();
        signingDTO.setSigningsId(signingId);
        when(signingAssembler.toDTO(signing)).thenReturn(signingDTO);

        // Ejecución del método del servicio
        SigningDTO result = signingsService.findById(signingId);

        // Verificación de que se llamó al método findById del repositorio con el ID adecuado
        verify(signingsRepository, times(1)).findById(signingId);

        // Verificación del DTO retornado
        assertEquals(signingId, result.getSigningsId());
    }

    @Test
    public void testFindById_NonExistingSigning() {
        // Datos simulados con un ID que no existe
        Long nonExistingId = 100L;

        // Simulación del repositorio para devolver un Optional vacío
        when(signingsRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        // Ejecución del método y verificación de la excepción esperada
        RuntimeException exception = assertThrows(RuntimeException.class, () -> signingsService.findById(nonExistingId));
        assertEquals("Signing not found", exception.getMessage());

        // Verificación de que se llamó al método findById del repositorio con el ID adecuado
        verify(signingsRepository, times(1)).findById(nonExistingId);
    }


    @Test
    public void testFindById_NullId() {
        // Datos simulados con un ID nulo
        Long nullId = null;

        // Ejecución del método y verificación de la excepción esperada
        assertThrows(IllegalArgumentException.class, () -> signingsService.findById(nullId));

        // Verificación de que no se llamó al método findById del repositorio
        verify(signingsRepository, never()).findById(any());
    }

    @Test
    public void testGetAllSigningsByPlayerId() {
        // Datos simulados
        Long playerId = 1L;
        Player player = new Player();
        player.setPlayerId(playerId);

        // Simulación del repositorio para devolver el jugador por ID
        when(playerRepository.findById(playerId)).thenReturn(Optional.of(player));

        // Simulación del repositorio para devolver los contratos asociados al jugador
        List<Signing> signings = new ArrayList<>();
        Signing signing1 = new Signing();
        signing1.setSigningsId(1L);
        Signing signing2 = new Signing();
        signing2.setSigningsId(2L);
        signings.add(signing1);
        signings.add(signing2);
        when(signingsRepository.findByPlayer(player)).thenReturn(signings);

        // Simulación del assembler para convertir los contratos a DTOs
        SigningDTO signingDTO1 = new SigningDTO();
        signingDTO1.setSigningsId(1L);
        SigningDTO signingDTO2 = new SigningDTO();
        signingDTO2.setSigningsId(2L);
        when(signingAssembler.toDTO(signing1)).thenReturn(signingDTO1);
        when(signingAssembler.toDTO(signing2)).thenReturn(signingDTO2);

        // Ejecución del método del servicio
        List<SigningDTO> result = signingsService.getAllSigningsByPlayerId(playerId);

        // Verificación de que se devuelva la lista correcta de DTOs de contratos
        assertEquals(2, result.size());
        assertEquals(signingDTO1.getSigningsId(), result.get(0).getSigningsId());
        assertEquals(signingDTO2.getSigningsId(), result.get(1).getSigningsId());

        // Verificación de llamadas a métodos del repositorio
        verify(playerRepository, times(1)).findById(playerId);
        verify(signingsRepository, times(1)).findByPlayer(player);

        // Verificación de llamadas al assembler
        verify(signingAssembler, times(1)).toDTO(signing1);
        verify(signingAssembler, times(1)).toDTO(signing2);
    }

    @Test
    public void testCreateSigning() {
        // Datos simulados
        SigningDTO signingDTO = new SigningDTO();
        signingDTO.setSigningsId(1L);

        PlayerDTO playerDTO = new PlayerDTO();
        playerDTO.setPlayerId(1L);
        signingDTO.setPlayer(playerDTO);

        TeamDTO teamDTO = new TeamDTO();
        teamDTO.setTeamId(1L);
        signingDTO.setTeam(teamDTO);

        Signing signingEntity = new Signing();
        signingEntity.setSigningsId(1L);

        // Configuración de la simulación
        when(signingAssembler.toEntity(signingDTO)).thenReturn(signingEntity);
        when(playerRepository.findById(1L)).thenReturn(Optional.of(new Player()));
        when(teamRepository.findById(1L)).thenReturn(Optional.of(new Team()));
        when(signingsRepository.save(signingEntity)).thenReturn(signingEntity);
        when(signingAssembler.toDTO(signingEntity)).thenReturn(signingDTO);

        // Ejecución del método del servicio
        SigningDTO result = signingsService.createSigning(signingDTO);

        // Verificación de que se devuelva el DTO correcto del contrato guardado
        assertEquals(signingDTO.getSigningsId(), result.getSigningsId());

        // Verificación de llamadas a métodos del repositorio y del assembler
        verify(playerRepository, times(1)).findById(1L);
        verify(teamRepository, times(1)).findById(1L);
        verify(signingsRepository, times(1)).save(signingEntity);
        verify(signingAssembler, times(1)).toEntity(signingDTO);
        verify(signingAssembler, times(1)).toDTO(signingEntity);
    }

    @Test
    public void testUpdateSigning() {
        // Datos simulados
        Long signingId = 1L;
        SigningDTO signingDTO = new SigningDTO();
        signingDTO.setSigningsId(signingId);

        PlayerDTO playerDTO = new PlayerDTO();
        playerDTO.setPlayerId(1L);
        signingDTO.setPlayer(playerDTO);

        TeamDTO teamDTO = new TeamDTO();
        teamDTO.setTeamId(1L);
        signingDTO.setTeam(teamDTO);

        Signing signingEntity = new Signing();
        signingEntity.setSigningsId(signingId);

        // Simulación del repositorio para devolver el signing por ID
        when(signingsRepository.findById(signingId)).thenReturn(Optional.of(signingEntity));

        // Simulación del assembler para convertir SigningDTO a Signing y viceversa
        when(signingAssembler.toEntity(signingDTO, signingEntity)).thenReturn(signingEntity);
        when(signingAssembler.toDTO(signingEntity)).thenReturn(signingDTO);

        // Simulación del repositorio para devolver el jugador por ID
        when(playerRepository.findById(playerDTO.getPlayerId())).thenReturn(Optional.of(new Player()));

        // Simulación del repositorio para devolver el equipo por ID
        when(teamRepository.findById(teamDTO.getTeamId())).thenReturn(Optional.of(new Team()));

        // Simulación del repositorio para guardar el signing actualizado
        when(signingsRepository.save(signingEntity)).thenReturn(signingEntity);

        // Ejecución del método del servicio
        SigningDTO result = signingsService.updateSigning(signingId, signingDTO);

        // Verificación de que se llamaron los métodos correspondientes
        verify(signingsRepository, times(1)).findById(signingId);
        verify(signingAssembler, times(1)).toEntity(signingDTO, signingEntity);
        verify(playerRepository, times(1)).findById(playerDTO.getPlayerId());
        verify(teamRepository, times(1)).findById(teamDTO.getTeamId());
        verify(signingsRepository, times(1)).save(signingEntity);
        verify(signingAssembler, times(1)).toDTO(signingEntity);

        // Verificación de que el DTO retornado es el esperado
        assertEquals(signingDTO.getSigningsId(), result.getSigningsId());
    }

    @Test
    public void testDeleteSigning_ExistingSigning() {
        // Datos simulados
        Long signingId = 1L;
        Signing signing = new Signing();
        signing.setSigningsId(signingId);

        // Simulación del repositorio para devolver el signing por ID
        when(signingsRepository.findById(signingId)).thenReturn(Optional.of(signing));

        // Ejecución del método del servicio
        signingsService.deleteSigning(signingId);

        // Verificación de que se llamó al método delete del repositorio con el signing correcto
        verify(signingsRepository, times(1)).findById(signingId);
        verify(signingsRepository, times(1)).delete(signing);
    }

    @Test
    public void testDeleteSigning_NonExistingSigning() {
        // Datos simulados con un ID que no existe
        Long nonExistingId = 100L;

        // Simulación del repositorio para devolver un Optional vacío
        when(signingsRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        // Ejecución del método y verificación de la excepción esperada
        RuntimeException exception = assertThrows(RuntimeException.class, () -> signingsService.deleteSigning(nonExistingId));
        assertEquals("Signing not found", exception.getMessage());

        // Verificación de que se llamó al método findById del repositorio con el ID adecuado
        verify(signingsRepository, times(1)).findById(nonExistingId);

        // Verificación de que no se llamó al método delete del repositorio
        verify(signingsRepository, never()).delete(any());
    }
}
