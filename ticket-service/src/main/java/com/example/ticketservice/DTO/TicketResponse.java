package com.example.ticketservice.DTO;

import com.example.ticketservice.entity.TicketEntity;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TicketResponse {
    private Long ticketId;
    private Long playerID;
    private String roomCode;
    private String ticket;

    public static TicketResponse fromEntity(TicketEntity entity){
        return TicketResponse.builder()
                .ticketId(entity.getTicketId())
                .playerID(entity.getPlayerId())
                .roomCode(entity.getRoomCode())
                .ticket(entity.getTicket())
                .build();
    }
}
