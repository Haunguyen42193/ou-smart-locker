package com.example.ousmartlocker.dto;

import com.example.ousmartlocker.model.User;
import com.example.ousmartlocker.model.enums.HistoryUserRole;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HistoryUserDto {
    private User user;
    private HistoryUserRole role;
}
