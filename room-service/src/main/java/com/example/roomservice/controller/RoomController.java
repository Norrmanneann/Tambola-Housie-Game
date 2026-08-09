package com.example.roomservice.controller;

import com.example.roomservice.DTO.JoinRoomRequest;
import com.example.roomservice.DTO.RoomRequest;
import com.example.roomservice.DTO.RoomResponse;
import com.example.roomservice.service.RoomService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("room/api")
@AllArgsConstructor
public class RoomController {

    private final RoomService service;

    @PostMapping
    public ResponseEntity<RoomResponse> createRoom(@RequestBody RoomRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createRoom(request));
    }

    @PostMapping("/joinRoom")
    public ResponseEntity<RoomResponse> joinRoom(@Valid @RequestBody JoinRoomRequest request){
        try{
            return ResponseEntity.status(HttpStatus.OK).body(service.joinRoom(request));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
