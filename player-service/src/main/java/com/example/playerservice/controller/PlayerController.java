package com.example.playerservice.controller;

import com.example.playerservice.DTO.PlayerIdsRequest;
import com.example.playerservice.DTO.PlayerRequest;
import com.example.playerservice.DTO.PlayerResponse;
import com.example.playerservice.service.PlayerService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("player/api")
@AllArgsConstructor
public class PlayerController {
    private final PlayerService service;

    @PostMapping
    public ResponseEntity<PlayerResponse> createPlayer(@Valid @RequestBody PlayerRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createPlayer(request));
    }

    @GetMapping("{id}")
    public ResponseEntity<PlayerResponse> getPlayer(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(service.getPlayer(id));
    }

    @PostMapping("/ids")
    public ResponseEntity<List<PlayerResponse>> getAllPlayersById(@RequestBody PlayerIdsRequest request){
        List<PlayerResponse> responses = service.getAllPlayersByIds(request);
        return ResponseEntity.status(HttpStatus.FOUND).body(responses);
    }
}
