package com.example.ousmartlocker.dto;

import com.example.ousmartlocker.model.Role;
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
}
