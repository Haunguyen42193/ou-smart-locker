package com.example.ousmartlocker.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "documents")
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;
    private String type; // Loại tài liệu
    private String securityLevel;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

}
