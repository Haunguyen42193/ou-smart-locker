package com.example.ousmartlocker.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JWTAuthResponseDto {
    private String accessToken;
    private String tokenType = "Bearer";
}
