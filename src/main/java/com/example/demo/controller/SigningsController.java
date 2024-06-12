package com.example.demo.controller;

import com.example.demo.DTO.SigningDTO;
import com.example.demo.service.SigningsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/signing")
public class SigningsController {
    @Autowired
    private final SigningsService signingsService;

    public SigningsController(SigningsService signingService) {
        this.signingsService = signingService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<SigningDTO>> getAllSignings() {
        List<SigningDTO> allSignings = signingsService.getAllSignings();
        return ResponseEntity.ok(allSignings);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SigningDTO> getSigningById(@PathVariable Long id) {
        return ResponseEntity.ok(signingsService.findById(id));
    }

    @PostMapping("/create")
    public SigningDTO createSigning(@RequestBody SigningDTO signingDTO) {
        return signingsService.createSigning(signingDTO);
    }

    @PutMapping("/signings/{id}")
    public ResponseEntity<SigningDTO> updateSigning(@PathVariable Long id, @RequestBody SigningDTO signingDTO) {
        SigningDTO updatedSigningDTO = signingsService.updateSigning(id, signingDTO);
        return ResponseEntity.ok(updatedSigningDTO);
    }

    @DeleteMapping("/signings/{id}")
    public ResponseEntity<Void> deleteSigning(@PathVariable Long id) {
        // Elimina el signing con el ID proporcionado utilizando el servicio de signings
        signingsService.deleteSigning(id);
        // Devuelve una respuesta HTTP 200 OK sin contenido
        return ResponseEntity.ok().build();
    }

    @GetMapping("/player/{playerId}")
    public ResponseEntity<List<SigningDTO>> getAllSigningsByPlayerId(@PathVariable Long playerId) {
        List<SigningDTO> signings = signingsService.getAllSigningsByPlayerId(playerId);
        return ResponseEntity.ok(signings);
    }

}
