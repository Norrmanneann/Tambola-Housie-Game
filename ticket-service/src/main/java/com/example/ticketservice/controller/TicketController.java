package com.example.ticketservice.controller;

import com.example.ticketservice.DTO.TicketRequest;
import com.example.ticketservice.DTO.TicketResponse;
import com.example.ticketservice.service.TicketService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ticket/api")
@AllArgsConstructor
public class TicketController {
    private final TicketService ticketService;

    @PostMapping
    public ResponseEntity<TicketResponse> generateTicket(@Valid @RequestBody TicketRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(ticketService.generateTicket(request));
    }

    @GetMapping("/{ticketId}")
    public ResponseEntity<TicketResponse> getByTicketId(@PathVariable Long ticketId){
        try{
            return ResponseEntity.status(HttpStatus.OK).body(ticketService.getTicket(ticketId));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/room/{roomCode}")
    public ResponseEntity<List<TicketResponse>> getByRoomCode(@PathVariable String roomCode){
        return ResponseEntity.status(HttpStatus.OK).body(ticketService.getTicketByRoomCode(roomCode));
    }
}
