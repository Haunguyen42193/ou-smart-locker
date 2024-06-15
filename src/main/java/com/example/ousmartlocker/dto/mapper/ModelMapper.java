package com.example.ousmartlocker.dto.mapper;

import com.example.ousmartlocker.dto.*;
import com.example.ousmartlocker.model.*;
import com.example.ousmartlocker.model.enums.Role;
import com.example.ousmartlocker.util.SmartLockerUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;

public class ModelMapper {

    public static LockerDto mapToLockerDto(Locker locker) {
        return LockerDto.builder()
                .isOccupied(locker.getIsOccupied())
                .lockerLocation(mapToLockerLocationDto(locker.getLockerLocation()))
                .lockerName(locker.getLockerName())
                .build();
    }

    public static LockerLocationDto mapToLockerLocationDto(LockerLocation location) {
        return LockerLocationDto.builder()
                .locationId(location.getLocationId())
                .location(location.getLocation())
                .build();
    }

    public static User mapSignUpDtoToUser(SignUpDto signUpDto, PasswordEncoder passwordEncoder) {
        return User.builder()
                .name(signUpDto.getName())
                .email(signUpDto.getEmail())
                .username(signUpDto.getUsername())
                .password(passwordEncoder.encode(signUpDto.getPassword().trim()))
                .phone(SmartLockerUtils.formatPhoneNumber(signUpDto.getPhone()))
                .roles(Collections.singletonList(Role.ROLE_USER))
                .build();
    }

    public static OtpDto mapToOtpDto(Otp otp) {
        return OtpDto.builder()
                .otpId(otp.getOtpId())
                .otpNumber(otp.getOtpNumber())
                .setGeneratedAt(otp.getSetGeneratedAt())
                .expireTime(otp.getExpireTime())
                .build();
    }

    public static LockerLocationDto mapToLocationDto(LockerLocation location) {
        return LockerLocationDto.builder().locationId(location.getLocationId())
                .location(location.getLocation()).build();
    }

    public static HistoryLocationDto mapHistoryLocationToDto(HistoryLocation historyLocation) {
        return HistoryLocationDto.builder()
                .location(mapToLocationDto(historyLocation.getLocation()))
                .role(historyLocation.getRole())
                .build();
    }

    public static HistoryUserDto mapHistoryUserToDto(HistoryUser historyUser) {
        return HistoryUserDto.builder()
                .user(mapToUserDto(historyUser.getUser()))
                .role(historyUser.getRole())
                .build();
    }

    public static UserDto mapToUserDto(User user) {
        return UserDto.builder()
                .id(user.getUserId())
                .email(user.getEmail())
                .phone(user.getPhone())
                .roles(user.getRoles())
                .username(user.getUsername())
                .name(user.getName())
                .build();
    }

    public static HistoryDto mapHistoryToDto(History history) {
        return HistoryDto.builder()
                .historyId(history.getHistoryId())
                .locker(ModelMapper.mapToLockerDto(history.getLocker()))
                .endTime(history.getEndTime())
                .startTime(history.getStartTime())
                .location(history.getLocation().stream().map(ModelMapper::mapHistoryLocationToDto).toList())
                .users(history.getUsers().stream().map(ModelMapper::mapHistoryUserToDto).toList())
                .otp(ModelMapper.mapToOtpDto(history.getOtp()))
                .onProcedure(history.getOnProcedure())
                .build();
    }

    public static List<HistoryUser> mapHistoryUsersNew(List<HistoryUser> users) {
        return users.stream().map(ModelMapper::mapHistoryUserNew).toList();
    }

    public static HistoryUser mapHistoryUserNew(HistoryUser historyUser) {
        return HistoryUser.builder()
                .history(historyUser.getHistory())
                .user(historyUser.getUser())
                .role(historyUser.getRole())
                .build();
    }

    public static List<HistoryLocation> mapHistoryLocationsNew(List<HistoryLocation> location) {
        return location.stream().map(ModelMapper::mapHistoryLocationNew).toList();
    }

    public static HistoryLocation mapHistoryLocationNew(HistoryLocation historyLocation) {
        return HistoryLocation.builder()
                .history(historyLocation.getHistory())
                .location(historyLocation.getLocation())
                .role(historyLocation.getRole())
                .build();
    }

    public static LockerLocation mapLockerLocation(LockerLocationDto lockerLocation) {
        return LockerLocation.builder()
                .locationId(lockerLocation.getLocationId())
                .location(lockerLocation.getLocation())
                .build();
    }

    public static UserDto convertUserToUserDto(User user) {
        return UserDto.builder()
                .id(user.getUserId())
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
