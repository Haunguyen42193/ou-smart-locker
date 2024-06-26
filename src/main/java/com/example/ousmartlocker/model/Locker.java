package com.example.ousmartlocker.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

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
    @Column(name = "is_occupied")
    private Boolean isOccupied;
    @ManyToOne
    private LockerLocation lockerLocation;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "locker")
    private List<LockerUsingRecord> lockerUsingRecords;
}

