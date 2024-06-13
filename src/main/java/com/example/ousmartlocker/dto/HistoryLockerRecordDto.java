package com.example.ousmartlocker.dto;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HistoryLockerRecordDto implements Serializable {
    private long record;
    private List<LockerUsingDto> lockerUsingDtos;
    private List<UserUsingDto> userUsingDtos;
}
