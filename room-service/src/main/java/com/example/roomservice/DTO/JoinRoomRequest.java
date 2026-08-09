package com.example.roomservice.DTO;

import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JoinRoomRequest {
    @NonNull
    private Long playerId;
    @NonNull
    @Size(min = 6, max = 6)
    private String roomCode;
}
