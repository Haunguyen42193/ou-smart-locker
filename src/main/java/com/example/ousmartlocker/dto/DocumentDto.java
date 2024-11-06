package com.example.ousmartlocker.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DocumentDto {
    private long id;
    private String title;
    private String content;
    private String type; // Loại tài liệu
    private String securityLevel;
    private DepartmentDto department;
}
