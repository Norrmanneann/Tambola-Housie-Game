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

    public List<PlayerResponse> getAllPlayersByIds(PlayerIdsRequest ids){
        return repository.findAllById(ids.getIds())
                .stream()
                .map(player->PlayerResponse.fromEntity(player))
                .toList();
    }
}
