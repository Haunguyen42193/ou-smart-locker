package com.example.ousmartlocker.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SenderPasswordDto {
    private String mail;
    private String phone;
    private String name;
    private String newPass;
}
