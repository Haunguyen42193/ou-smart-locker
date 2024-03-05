package com.example.ousmartlocker.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponseDto {
    private UserDto user;
    private String accessToken;
    private Long loginTime;
    private Long expirationDuration;
}
