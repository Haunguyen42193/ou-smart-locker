package com.example.ousmartlocker.dto;

import lombok.Data;

@Data
public class RegisterLockerDto {
    private Long shipper;
    private Long receiver;
    private Long locationSend;
    private Long locationReceive;
}
