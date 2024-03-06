package com.example.ousmartlocker.services.impl;

import com.example.ousmartlocker.dto.OuSmartLockerResp;
import com.example.ousmartlocker.model.Locker;
import com.example.ousmartlocker.repository.LockerRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class LockerServiceImplTest {

    @Mock
    LockerRepository lockerRepository;

    @InjectMocks
    LockerServiceImpl lockerService;


    @Test
    void getAlllocker() {
        List<Locker> lockers = new ArrayList<>();
        lockers.add(new Locker(/*thông tin locker 1*/));
        lockers.add(new Locker(/*thông tin locker 2*/));
        // ...

        Mockito.when(lockerRepository.findAll()).thenReturn(lockers);

        // Gọi phương thức cần kiểm tra
        OuSmartLockerResp result = lockerService.getAlllocker();

        // Kiểm tra kết quả
        assertEquals(HttpStatus.OK, result.getStatus());
        assertEquals("Get all locker successful", result.getMessage());
        assertEquals(lockers, result.getData());
    }

}