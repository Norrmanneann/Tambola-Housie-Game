package com.example.roomservice.DTO;

import com.example.roomservice.entity.RoomEntity;
import com.example.roomservice.feign.PlayerServiceFeign;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
    private List<String> playerName;
    private Long createdBy;
    private LocalDateTime createdAt;

    public static RoomResponse fromEntity(RoomEntity entity, List<String> names){

        return RoomResponse.builder()
                .id(entity.getId())
                .roomCode(entity.getRoomCode())
                .maxPlayers(entity.getMaxPlayers())
                .availableSlots(entity.getAvailableSlots())
                .status(entity.getStatus())
                .playerName(names)
                .createdBy(entity.getCreatedBy())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
