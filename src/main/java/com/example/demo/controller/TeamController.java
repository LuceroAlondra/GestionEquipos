package com.example.demo.controller;

import com.example.demo.DTO.TeamDTO;
import com.example.demo.service.TeamService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/team")
public class TeamController {
    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @GetMapping("/all")
    public List<TeamDTO> getAllTeams() {
        return teamService.getAllTeams();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeamDTO> getTeamById(@PathVariable Long id) {
        return ResponseEntity.ok(teamService.getTeamById(id));
    }


    @PostMapping("/create")
    public TeamDTO createTeam(@RequestBody TeamDTO teamDTO) {
        return teamService.createTeam(teamDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TeamDTO> updateTeam(@PathVariable Long id, @RequestBody TeamDTO updatedTeamDTO) {
        // Llama al método updateTeam del servicio para realizar la actualización
        TeamDTO updatedTeam = teamService.updateTeam(id, updatedTeamDTO);
        // Devuelve la respuesta con el DTO actualizado y el código de estado
        // correspondiente
        return ResponseEntity.ok(updatedTeam);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeam(@PathVariable Long id) {
        // Elimina el equipo con el ID proporcionado utilizando el servicio de equipo.
        teamService.deleteTeam(id);
        // Devuelve una respuesta HTTP 200 OK sin contenido.
        return ResponseEntity.ok().build();
    }

}
