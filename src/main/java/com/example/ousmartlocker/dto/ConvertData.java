package com.example.ousmartlocker.dto;

import com.example.ousmartlocker.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ConvertData {
    public static UserDto convertUserToUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .username(user.getUsername())
                .phone(user.getPhone())
                .roles(user.getRoles())
                .build();
    }

    public static <T> T convertObjectToObject(Object object, Class<T> aClass) {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(object, aClass);
    }
}
