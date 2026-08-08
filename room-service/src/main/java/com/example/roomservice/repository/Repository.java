package com.example.roomservice.repository;

import com.example.roomservice.entity.RoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface Repository extends JpaRepository<RoomEntity,Long> {
    boolean existsByRoomCode(String roomCode);

    Optional<RoomEntity> findByRoomCode(String roomCode);
}
