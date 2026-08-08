package com.example.roomservice.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedBy;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RoomResponse {
    private Long id;
    private String roomCode;
    private int maxPlayers;
    private int availableSlots;
    private RoomEntity.Status status;
    private String createdBy;
    private LocalDateTime createdAt;

    public static RoomResponse fromEntity(RoomEntity entity){
        return RoomResponse.builder()
                .id(entity.getId())
                .roomCode(entity.getRoomCode())
                .maxPlayers(entity.getMaxPlayers())
                .availableSlots(entity.getAvailableSlots())
                .status(entity.getStatus())
                .createdBy(entity.getCreatedBy())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
