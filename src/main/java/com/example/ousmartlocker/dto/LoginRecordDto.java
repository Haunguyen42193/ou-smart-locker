package com.example.ousmartlocker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginRecordDto implements Serializable {
    private long record;
    private List<LoginRecordUserDto> loginRecordUserDtos;
}
