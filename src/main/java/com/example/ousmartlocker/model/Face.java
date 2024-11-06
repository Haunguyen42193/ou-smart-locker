package com.example.ousmartlocker.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "faces")
public class Face {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String faceEncoding;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
