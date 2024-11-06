package com.example.ousmartlocker.dto;

import com.example.ousmartlocker.model.Face;
import com.example.ousmartlocker.model.enums.Role;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserDto {
    private long id;
    private String username;
    private String name;
    private String email;
    private String phone;
    private List<Role> roles;
    private FaceDto face;
    private DepartmentDto department;
}
