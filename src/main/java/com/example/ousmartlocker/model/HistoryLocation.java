package com.example.ousmartlocker.model;

import com.example.ousmartlocker.model.enums.HistoryLocationRole;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class HistoryLocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long historyLocationId;
    @ManyToOne
    @JoinColumn(name = "history_id")
    private History history;
    @ManyToOne
    @JoinColumn(name = "location_id")
    private LockerLocation location;
    @Enumerated(EnumType.STRING)
    private HistoryLocationRole role;
}
