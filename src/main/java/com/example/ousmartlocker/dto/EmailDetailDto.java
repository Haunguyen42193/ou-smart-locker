package com.example.ousmartlocker.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmailDetailDto {
    private String mail;
    private String name;
    private String otp;
}
