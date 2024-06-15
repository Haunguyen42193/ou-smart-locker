package com.example.ousmartlocker.services.impl;

import com.example.ousmartlocker.dto.*;
import com.example.ousmartlocker.exception.HistoryNotFoundException;
import com.example.ousmartlocker.exception.InvalidTimeException;
import com.example.ousmartlocker.model.*;
import com.example.ousmartlocker.repository.HistoryRepository;
import com.example.ousmartlocker.repository.LockerUsingRecordRepository;
import com.example.ousmartlocker.services.HistoryService;
import com.example.ousmartlocker.util.SmartLockerUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class HistoryServiceImpl implements HistoryService {
    @Autowired
    private HistoryRepository historyRepository;
    @Autowired
    private LockerUsingRecordRepository lockerUsingRecordRepository;

    @Override
    public OuSmartLockerResp getHistoryRecord(String startDate, String endDate) {
        if (Strings.isBlank(startDate)) {
            throw new InvalidTimeException("Start time is invalid");
        }
        LocalDate end;
        Date dateTimeEnd;
        if (Strings.isBlank(endDate)) {
            end = LocalDate.now();
            dateTimeEnd = Timestamp.valueOf(end.atStartOfDay());
        } else {
            end = LocalDate.parse(endDate, SmartLockerUtils.timeFormatter);
            dateTimeEnd = Timestamp.valueOf(end.atStartOfDay());
        }
        LocalDate start = LocalDate.parse(startDate, SmartLockerUtils.timeFormatter);
        Date dateTimeStart = Timestamp.valueOf(start.atStartOfDay());
        HistoryLockerRecordDto historyLockerRecordDto = new HistoryLockerRecordDto();
        long historyRecord = lockerUsingRecordRepository.countRecordsBetween(dateTimeStart, dateTimeEnd);
        historyLockerRecordDto.setRecord(historyRecord);
        List<LockerUsingDto> lockerUsingDtos = lockerUsingRecordRepository.countRecordGroupByLocker(dateTimeStart, dateTimeEnd);
        historyLockerRecordDto.setLockerUsingDtos(lockerUsingDtos);
        List<UserUsingDto> userUsingDtos = lockerUsingRecordRepository.countRecordGroupByUser(dateTimeStart, dateTimeEnd);
        historyLockerRecordDto.setUserUsingDtos(userUsingDtos);
        return OuSmartLockerResp.builder().status(HttpStatus.OK).data(historyLockerRecordDto).message("Get success").build();
    }

    @Override
    public OuSmartLockerResp getAllHistory() {
        List<History> histories = historyRepository.findAll();
        List<HistoryDto> historyDtos = histories.stream().map(this::mapHistoryToDto).toList();
        return OuSmartLockerResp.builder().status(HttpStatus.OK).message("Successful").data(historyDtos).build();
    }

    @Override
    public OuSmartLockerResp getHistoryById(Long historyId) {
        History history = historyRepository.findById(historyId).orElse(null);
        if (Objects.isNull(history))
            throw new HistoryNotFoundException("Not found history");
        HistoryDto historyDto = mapHistoryToDto(history);
        return OuSmartLockerResp.builder().status(HttpStatus.OK).message("Successful").data(historyDto).build();
    }

    private HistoryDto mapHistoryToDto(History history) {
        return HistoryDto.builder()
                .historyId(history.getHistoryId())
                .locker(history.getLocker())
                .endTime(history.getEndTime())
                .startTime(history.getStartTime())
                .location(history.getLocation().stream().map(this::mapHistoryLocationToDto).toList())
                .users(history.getUsers().stream().map(this::mapHistoryUserToDto).toList())
                .otp(history.getOtp())
                .onProcedure(history.getOnProcedure())
                .build();
    }

    private HistoryUserDto mapHistoryUserToDto(HistoryUser historyUser) {
        return HistoryUserDto.builder()
                .user(mapToUserDto(historyUser.getUser()))
                .role(historyUser.getRole())
                .build();
    }

    private UserDto mapToUserDto(User user) {
        return UserDto.builder()
                .id(user.getUserId())
                .email(user.getEmail())
                .phone(user.getPhone())
                .roles(user.getRoles())
                .username(user.getUsername())
                .name(user.getName())
                .build();
    }

    private HistoryLocationDto mapHistoryLocationToDto(HistoryLocation historyLocation) {
        return HistoryLocationDto.builder()
                .location(mapToLocationDto(historyLocation.getLocation()))
                .role(historyLocation.getRole())
                .build();
    }

    private LockerLocationDto mapToLocationDto(LockerLocation location) {
        return LockerLocationDto.builder().locationId(location.getLocationId())
                .location(location.getLocation()).build();
    }
}
