package com.example.playerservice.controller;

import com.example.playerservice.entity.PlayerRequest;
import com.example.playerservice.entity.PlayerResponse;
import com.example.playerservice.service.Service;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("player/api")
@AllArgsConstructor
public class Controller {
    private final Service service;

    @PostMapping
    public ResponseEntity<Long> createPlayer(@Valid @RequestBody PlayerRequest request){
        return ResponseEntity.ok(service.createPlayer(request));
    }

    @GetMapping("{id}")
    public ResponseEntity<PlayerResponse> getPlayer(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.FOUND).body(service.getPlayer(id));
    }

    @GetMapping
    public ResponseEntity<List<PlayerResponse>> getAllPlayers(){
        return ResponseEntity.status(HttpStatus.FOUND).body(service.getAllPlayers());
    }
}
