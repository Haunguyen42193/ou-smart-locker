package com.example.ousmartlocker.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class History {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long historyId;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "history")
    private List<HistoryUser> users;
    @OneToOne
    private Locker locker;
    private String startTime;
    private String endTime;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "history")
    private List<HistoryLocation> location;
    @OneToOne
    private Otp otp;
}
