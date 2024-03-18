package com.example.ousmartlocker.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterLockerInfoResp {
    private Long lockerId;
    private Long historyId;
    private String startTime;
    private String endTime;
}
