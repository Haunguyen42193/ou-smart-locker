package com.example.ousmartlocker.services.impl;

import com.example.ousmartlocker.repository.LockerRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class LockerServiceImplTest {

    @Mock
    LockerRepository lockerRepository;

    @InjectMocks
    LockerServiceImpl lockerService;


//    @Test
//    void getAlllocker() {
//        List<Locker> lockers = new ArrayList<>();
//        lockers.add(new Locker(/*thông tin locker 1*/));
//        lockers.add(new Locker(/*thông tin locker 2*/));
//        // ...
//
//        Mockito.when(lockerRepository.findAll()).thenReturn(lockers);
//
//        // Gọi phương thức cần kiểm tra
//        OuSmartLockerResp result = lockerService.getAlllocker();
//
//        // Kiểm tra kết quả
//        assertEquals(HttpStatus.OK, result.getStatus());
//        assertEquals("Get all locker successful", result.getMessage());
//        assertEquals(lockers, result.getData());
//    }

}