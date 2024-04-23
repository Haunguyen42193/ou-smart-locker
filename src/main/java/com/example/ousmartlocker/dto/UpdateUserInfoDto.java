package com.example.ousmartlocker.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class UpdateUserInfoDto implements Serializable {
    private String name;
    private String phone;
    private String mail;
}
