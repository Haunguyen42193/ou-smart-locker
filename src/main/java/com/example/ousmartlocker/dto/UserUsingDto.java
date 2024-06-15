package com.example.ousmartlocker.dto;

import com.example.ousmartlocker.model.User;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserUsingDto {
    private String name;
    private long record;
}
