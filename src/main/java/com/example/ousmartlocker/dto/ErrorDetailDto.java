package com.example.ousmartlocker.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDetailDto {
    private String errorMessage;
    private String devErrorMessage;
    private Long timestamp;
}
