package com.example.ousmartlocker.model;

import lombok.Data;

@Data
public class ChangePassDto {
    private String oldPass;
    private String newPass;
}
