package com.example.ticketservice.repository;

import com.example.ticketservice.entity.TicketEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TicketRepository extends JpaRepository<TicketEntity, Long> {
    List<TicketEntity> findByRoomCode(String roomCode);

    Optional<TicketEntity> findByPlayerIdAndGameId(Long playerId, Long gameId);

    boolean existsByTicketAndRoomCode(String ticket, String roomCode);

    Optional<TicketEntity> findByTicketId(Long id);

    List<TicketEntity> findAllByRoomCode(String roomCode);
}
