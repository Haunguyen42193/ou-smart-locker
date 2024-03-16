package com.example.ousmartlocker.dto;

import lombok.Data;

@Data
public class RegisterLockerDto {
    private Long sender;
    private Long shipper;
    private Long receiver;
    private String locationSend;
    private String locationReceive;
    private String time;
}
