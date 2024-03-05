package com.example.ousmartlocker.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmailInfoDto {
    private String mail;
    private String content;
    private String subject;
}
