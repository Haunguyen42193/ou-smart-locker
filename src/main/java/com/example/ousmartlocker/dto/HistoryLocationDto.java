package com.example.ousmartlocker.dto;

import com.example.ousmartlocker.model.LockerLocation;
import com.example.ousmartlocker.model.enums.HistoryLocationRole;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HistoryLocationDto {
    private LockerLocation location;
    private HistoryLocationRole role;
}
