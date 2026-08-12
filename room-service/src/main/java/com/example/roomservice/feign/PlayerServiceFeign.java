package com.example.roomservice.feign;

import com.example.roomservice.DTO.PlayerRequest;
import com.example.roomservice.DTO.PlayerResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@FeignClient(name = "player-service")
public interface PlayerServiceFeign {
    @GetMapping("/player/api/{id}")
    PlayerResponse getPlayer(@PathVariable Long id);

    @PostMapping("player/api/ids")
    List<PlayerResponse> getAllPlayersById(@RequestBody PlayerRequest ids);
}
