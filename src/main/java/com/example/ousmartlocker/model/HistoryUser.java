package com.example.ousmartlocker.model;

import com.example.ousmartlocker.model.enums.HistoryUserRole;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class HistoryUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long historyUserId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @ManyToOne
    @JoinColumn(name = "history_id")
    @JsonBackReference
    private History history;

    @Enumerated(EnumType.STRING)
    private HistoryUserRole role;
}
