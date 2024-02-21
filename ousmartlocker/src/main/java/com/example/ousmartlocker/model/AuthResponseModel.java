package com.example.ousmartlocker.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponseModel {
    private UserDto user;
    private String accessToken;
    private Long loginTime;
    private Long expirationDuration;
}
