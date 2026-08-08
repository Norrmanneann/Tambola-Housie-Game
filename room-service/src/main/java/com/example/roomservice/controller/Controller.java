package com.example.roomservice.controller;

import com.example.roomservice.entity.RoomRequest;
import com.example.roomservice.entity.RoomResponse;
import com.example.roomservice.service.Service;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("room/api")
@AllArgsConstructor
public class Controller {

    private final Service service;

    @PostMapping
    public ResponseEntity<RoomResponse> createRoom(@RequestBody RoomRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createRoom(request));
    }

    @PostMapping("{roomCode}")
    public ResponseEntity<RoomResponse> joinRoom(@PathVariable String roomCode){
        try{
            return ResponseEntity.status(HttpStatus.FOUND).body(service.joinRoom(roomCode));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
