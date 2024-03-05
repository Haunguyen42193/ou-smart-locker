package com.example.ousmartlocker.dto;

import lombok.Data;

@Data
public class ChangePassDto {
    private String oldPass;
    private String newPass;
}
