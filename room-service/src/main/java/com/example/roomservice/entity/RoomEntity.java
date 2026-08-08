package com.example.roomservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedBy;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
public class RoomEntity {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    private String roomCode;
    private int maxPlayers;
    private int availableSlots;

    @Enumerated(value = EnumType.STRING)
    private Status status;

    @CreatedBy
    private String createdBy;

//    @ElementCollection
//    @CollectionTable(name = "room_players", joinColumns = @JoinColumn(name = "room_id"))
//    @Column(name = "player_id")
//    private Set<Long> playerIds = new HashSet<>();

    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void persist(){
        this.createdAt = LocalDateTime.now();
        if(this.status==null) status = Status.waiting;
    }

    public enum Status{
        waiting,
        ongoing,
        finished
    }
}
