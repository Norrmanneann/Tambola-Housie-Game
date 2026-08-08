package com.example.playerservice.repository;

import com.example.playerservice.DTO.PlayerIdsRequest;
import com.example.playerservice.entity.PlayerEntity;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

@org.springframework.stereotype.Repository
public interface PlayerRepository extends JpaRepository<PlayerEntity, Long> {
    boolean existsByEmail(String email);

    PlayerEntity getById(Long id);

    @Query(nativeQuery = true, value = "select * from players_db where id in (:ids)")
    List<PlayerEntity> findAllByIds(@Param("ids") List<Long> ids);
}
