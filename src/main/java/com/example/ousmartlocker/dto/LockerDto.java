package com.example.ousmartlocker.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LockerDto implements Serializable {
    private String lockerName;
    private Boolean isOccupied;
    private LockerLocationDto lockerLocation;
}
