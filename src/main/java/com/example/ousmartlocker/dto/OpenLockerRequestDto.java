package com.example.ousmartlocker.dto;

import lombok.Data;

@Data
public class OpenLockerRequestDto {
    private Long lockerId;
    private String otp;
}