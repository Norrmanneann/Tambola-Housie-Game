package com.example.playerservice.DTO;

import com.example.playerservice.entity.PlayerEntity;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PlayerResponse {
    private Long id;
    private String name;

    public static PlayerResponse fromEntity(PlayerEntity entity){
        return PlayerResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }
}
