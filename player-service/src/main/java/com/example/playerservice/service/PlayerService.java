package com.example.playerservice.service;

import com.example.playerservice.DTO.PlayerIdsRequest;
import com.example.playerservice.entity.PlayerEntity;
import com.example.playerservice.DTO.PlayerRequest;
import com.example.playerservice.DTO.PlayerResponse;
import com.example.playerservice.repository.PlayerRepository;
import lombok.AllArgsConstructor;

import java.util.List;

@org.springframework.stereotype.Service
@AllArgsConstructor
public class PlayerService {
    private final PlayerRepository repository;

    public PlayerResponse createPlayer(PlayerRequest request){
        PlayerEntity playerEntity = new PlayerEntity();
        playerEntity.setName(request.getName());

        PlayerEntity entity = repository.save(playerEntity);

        return PlayerResponse.fromEntity(entity);
    }

    public PlayerResponse getPlayer(Long id){
        PlayerEntity entity = repository.findById(id)
                .orElseThrow(()->new RuntimeException("Id not found"));

        return PlayerResponse.fromEntity(entity);
    }

    public List<PlayerResponse> getAllPlayersByIds(PlayerIdsRequest ids){
        return repository.findAllById(ids.getIds())
                .stream()
                .map(player->PlayerResponse.fromEntity(player))
                .toList();
    }
}
