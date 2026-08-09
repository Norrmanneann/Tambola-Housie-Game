package com.example.roomservice.service;

import com.example.roomservice.DTO.JoinRoomRequest;
import com.example.roomservice.entity.RoomEntity;
import com.example.roomservice.DTO.RoomRequest;
import com.example.roomservice.DTO.RoomResponse;
import com.example.roomservice.repository.RoomRepository;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@org.springframework.stereotype.Service
@AllArgsConstructor
public class RoomService {

    private final RoomRepository repository;

    public RoomResponse createRoom(RoomRequest request){
        String roomCode = "";
        do {
            roomCode = UUID.randomUUID().toString().replace("-", "").substring(0, 6).toUpperCase();
        } while (repository.existsByRoomCode(roomCode));

        Set<Long> set = new HashSet<>();
        set.add(request.getPlayerId());

        RoomEntity entity = new RoomEntity();
        entity.setMaxPlayers(request.getMaxPlayers());
        entity.setStatus(RoomEntity.Status.waiting);
        entity.setRoomCode(roomCode);
        entity.setPlayerIds(set);
        entity.setAvailableSlots(entity.getMaxPlayers()-1);
        entity.setCreatedBy(request.getPlayerId());

        RoomEntity roomEntity = repository.save(entity);

        return RoomResponse.fromEntity(roomEntity);
    }

    public RoomResponse joinRoom(JoinRoomRequest request){
        RoomEntity entity = repository.findByRoomCode(request.getRoomCode())
                .orElseThrow(() -> new RuntimeException("Not Found")) ;

        if(entity.getAvailableSlots()<=0) throw new RuntimeException("No free slots available");

        Set<Long> set = entity.getPlayerIds();
        if(set.contains(request.getPlayerId())) throw new RuntimeException("player already exists");
        set.add(request.getPlayerId());

        entity.setAvailableSlots(entity.getAvailableSlots()-1);

        if(entity.getAvailableSlots()==0){
            entity.setStatus(RoomEntity.Status.ongoing);
        }
        RoomEntity saved = repository.save(entity);

        return RoomResponse.fromEntity(saved);
    }
}
