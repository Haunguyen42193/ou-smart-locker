package com.example.ousmartlocker.dto;

import com.example.ousmartlocker.model.User;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FaceDto {
    private long id;
    private String faceEncoding;
}
