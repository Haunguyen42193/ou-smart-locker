package com.example.ousmartlocker.dto;

import com.example.ousmartlocker.model.Locker;
import com.example.ousmartlocker.model.Otp;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HistoryDto {
    private Long historyId;
    private List<HistoryUserDto> users;
    private Locker locker;
    private String startTime;
    private String endTime;
    private List<HistoryLocationDto> location;
    private Otp otp;
    private Long onProcedure;
}
