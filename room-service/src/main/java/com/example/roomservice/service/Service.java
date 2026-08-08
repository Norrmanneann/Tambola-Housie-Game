package com.example.roomservice.service;

import com.example.roomservice.entity.RoomEntity;
import com.example.roomservice.entity.RoomRequest;
import com.example.roomservice.entity.RoomResponse;
import com.example.roomservice.repository.Repository;
import lombok.AllArgsConstructor;

import java.util.Optional;
import java.util.UUID;

@org.springframework.stereotype.Service
@AllArgsConstructor
public class Service {

    private final Repository repository;

    public RoomResponse createRoom(RoomRequest request){
        String roomCode = "";
        do {
            roomCode = UUID.randomUUID().toString().replace("-", "").substring(0, 6).toUpperCase();
        } while (repository.existsByRoomCode(roomCode));

        RoomEntity entity = new RoomEntity();
        entity.setMaxPlayers(request.getMaxPlayers());
        entity.setStatus(RoomEntity.Status.waiting);
        entity.setRoomCode(roomCode);
        entity.setAvailableSlots(entity.getMaxPlayers()-1);

        RoomEntity roomEntity = repository.save(entity);

        return RoomResponse.fromEntity(roomEntity);
    }

    public RoomResponse joinRoom(String roomCode){
        RoomEntity entity = repository.findByRoomCode(roomCode)
                .orElseThrow(() -> new RuntimeException("Not Found")) ;

        if(entity.getAvailableSlots()<=0) throw new RuntimeException("No free slots available");

        entity.setAvailableSlots(entity.getAvailableSlots()-1);

        if(entity.getAvailableSlots()==0){
            entity.setStatus(RoomEntity.Status.ongoing);
        }
        RoomEntity saved = repository.save(entity);

        return RoomResponse.fromEntity(saved);
    }
}
