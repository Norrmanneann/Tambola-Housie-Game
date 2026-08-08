package com.example.playerservice.entity;

import lombok.*;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PlayerResponse {
    private Long id;
    private String name;
    private String email;
    private LocalDateTime createdAt;

    public static PlayerResponse fromEntity(PlayerEntity entity){
        return PlayerResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .email(entity.getEmail())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
