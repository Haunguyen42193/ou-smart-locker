package com.example.ousmartlocker.dto;

import lombok.*;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OtpDto {
    private Long otpId;
    private String otpNumber;
    private String setGeneratedAt;
    private String expireTime;
}
