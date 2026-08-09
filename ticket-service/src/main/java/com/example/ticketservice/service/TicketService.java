package com.example.ticketservice.service;

import com.example.ticketservice.DTO.TicketRequest;
import com.example.ticketservice.DTO.TicketResponse;
import com.example.ticketservice.entity.TicketEntity;
import com.example.ticketservice.generator.TicketGenerator;
import com.example.ticketservice.repository.TicketRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@Service
@AllArgsConstructor
public class TicketService {
    private final TicketRepository repository;

    public TicketResponse generateTicket(TicketRequest request){
        ObjectMapper mapper = new ObjectMapper();

        String json = mapper.writeValueAsString(TicketGenerator.generate());
        while(repository.existsByTicketAndRoomCode(json, request.getRoomCode())){
            json = mapper.writeValueAsString(TicketGenerator.generate());
        }

        TicketEntity entity = new TicketEntity();
        entity.setPlayerId(request.getPlayerId());
        entity.setRoomCode(request.getRoomCode());
        entity.setTicket(json);

        return TicketResponse.fromEntity(repository.save(entity));
    }

    public TicketResponse getTicket(Long ticketId){
        TicketEntity entity = repository.findByTicketId(ticketId)
                .orElseThrow(()->new RuntimeException("Ticket not found"));

        return TicketResponse.fromEntity(entity);
    }

    public List<TicketResponse> getTicketByRoomCode(String roomCode){
        List<TicketEntity> entities = repository.findAllByRoomCode(roomCode);
        return entities.stream()
                .map(room->TicketResponse.fromEntity(room))
                .toList();
    }
}
