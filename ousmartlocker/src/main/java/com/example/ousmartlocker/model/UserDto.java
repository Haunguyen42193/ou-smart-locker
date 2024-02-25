package com.example.ousmartlocker.model;

import com.example.ousmartlocker.entity.Role;
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
