package com.example.ousmartlocker.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LockerLocationDto implements Serializable {
    private Long locationId;
    private String location;
}
