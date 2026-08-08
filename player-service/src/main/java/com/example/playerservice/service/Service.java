package com.example.playerservice.service;

import com.example.playerservice.entity.PlayerEntity;
import com.example.playerservice.entity.PlayerRequest;
import com.example.playerservice.entity.PlayerResponse;
import com.example.playerservice.repository.Repository;
import lombok.AllArgsConstructor;

import java.util.List;

@org.springframework.stereotype.Service
@AllArgsConstructor
public class Service {
    private final Repository repository;

    public Long createPlayer(PlayerRequest request){
        PlayerEntity playerEntity = new PlayerEntity();
        playerEntity.setName(request.getName());
        playerEntity.setEmail(request.getEmail());

        PlayerEntity entity = repository.save(playerEntity);

        return entity.getId();
    }

    public PlayerResponse getPlayer(Long id){
        PlayerEntity entity = repository.getById(id);

        return PlayerResponse.fromEntity(entity);
    }

    public List<PlayerResponse> getAllPlayers(){
        return repository.findAll()
                .stream()
                .map(PlayerResponse::fromEntity)
                .toList();
    }
}
