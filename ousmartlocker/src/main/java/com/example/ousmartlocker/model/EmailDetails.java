package com.example.ousmartlocker.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmailDetails {
    private String mail;
    private String name;
    private String otp;
}
