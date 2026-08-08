package com.example.playerservice.repository;

import com.example.playerservice.entity.PlayerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Repository
public interface Repository extends JpaRepository<PlayerEntity, Long> {
    boolean existsByEmail(String email);

    PlayerEntity getById(Long id);
}
