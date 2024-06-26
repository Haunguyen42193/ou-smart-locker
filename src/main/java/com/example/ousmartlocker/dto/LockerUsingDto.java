package com.example.ousmartlocker.dto;

import com.example.ousmartlocker.model.Locker;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LockerUsingDto {
    private String lockerName;
    private long record;
}
