package com.example.roomservice.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoomRequest {

    @NotBlank
    private Long playerId;
    @NotBlank
    private int maxPlayers;
}
