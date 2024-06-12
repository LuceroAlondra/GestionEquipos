package com.example.demo.controller;

import com.example.demo.DTO.PlayerDTO;
import com.example.demo.service.PlayerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/player")
public class PlayerController {
    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping("/all")
    public List<PlayerDTO> getAllPlayers() {
        return playerService.getAllPlayers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlayerDTO> getPlayerById(@PathVariable Long id) {
        return ResponseEntity.ok(playerService.getPlayerById(id));
    }

    @PostMapping("/create")
    public PlayerDTO createPlayer(@RequestBody PlayerDTO playerDTO) {
        return playerService.createPlayer(playerDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlayerDTO> updatePlayer(@PathVariable Long id, @RequestBody PlayerDTO updatedPlayerDTO) {
        PlayerDTO updatedPlayer = playerService.updatePlayer(id, updatedPlayerDTO);
        return ResponseEntity.ok(updatedPlayer);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePlayer(@PathVariable Long id) {
        // Elimina el jugador con el ID proporcionado utilizando el servicio de jugador.
        playerService.deletePlayer(id);
        // Devuelve una respuesta HTTP ok con un mensaje indicando que el
        // jugador ha sido eliminado.
        return ResponseEntity.ok().build();
    }

    @GetMapping("/teams/{teamId}/players")
    public ResponseEntity<List<PlayerDTO>> getAllPlayersByTeamId(@PathVariable Long teamId) {
        List<PlayerDTO> teamPlayersDTO = playerService.getAllPlayersByTeamId(teamId);
        return ResponseEntity.ok().body(teamPlayersDTO);
    }
}
