package com.example.ousmartlocker.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmailPassworDto {
    private String mail;
    private String name;
    private String newPass;
}
