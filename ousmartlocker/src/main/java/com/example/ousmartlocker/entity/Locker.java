package com.example.ousmartlocker.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "locker")
public class Locker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long lockerId;
    private String lockerName;
    private String lockerLocation;
    @Column(name = "is_occupied")
    private Boolean IsOccupied;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}

