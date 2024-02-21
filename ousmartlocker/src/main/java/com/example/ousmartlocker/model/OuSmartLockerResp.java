package com.example.ousmartlocker.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@Builder
public class OuSmartLockerResp {
    private HttpStatus status;
    private String message;
    private Object data;
}
