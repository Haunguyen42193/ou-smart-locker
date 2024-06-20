package com.example.ousmartlocker.services.impl;

import com.example.ousmartlocker.dto.*;
import com.example.ousmartlocker.dto.mapper.ModelMapper;
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
        List<HistoryDto> historyDtos = histories.stream().map(ModelMapper::mapHistoryToDto).toList();
        return OuSmartLockerResp.builder().status(HttpStatus.OK).message("Successful").data(historyDtos).build();
    }

    @Override
    public OuSmartLockerResp getHistoryById(Long historyId) {
        History history = historyRepository.findById(historyId).orElse(null);
        if (Objects.isNull(history))
            throw new HistoryNotFoundException("Not found history");
        HistoryDto historyDto = ModelMapper.mapHistoryToDto(history);
        return OuSmartLockerResp.builder().status(HttpStatus.OK).message("Successful").data(historyDto).build();
    }

    @Override
    public OuSmartLockerResp countHistory() {
        long num = historyRepository.count();
        return OuSmartLockerResp.builder().status(HttpStatus.OK).message("Count user success").data(num).build();
    }
}
