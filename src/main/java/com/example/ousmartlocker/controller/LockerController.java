package com.example.ousmartlocker.controller;

import com.example.ousmartlocker.dto.EmailInfoRequestDto;
import com.example.ousmartlocker.dto.ReRegisterLockerDto;
import com.example.ousmartlocker.dto.RegisterLockerDto;
import com.example.ousmartlocker.model.Locker;
import com.example.ousmartlocker.dto.OuSmartLockerResp;
import com.example.ousmartlocker.model.LockerLocation;
import com.example.ousmartlocker.services.LockerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/locker")
public class LockerController {
    @Autowired
    private LockerService lockerService;

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public OuSmartLockerResp addLocker(@RequestBody Locker locker) {
        return lockerService.addlocker(locker);
    }

    @PostMapping("/location/add")
    @PreAuthorize("hasRole('ADMIN')")
    public OuSmartLockerResp addLockerLocation(@RequestBody LockerLocation lockerLocation) {
        return lockerService.addLockerLocation(lockerLocation);
    }

    @PostMapping("/register")
    public OuSmartLockerResp registerLocker(@RequestBody RegisterLockerDto registerLockerDto) {
        return lockerService.registerLocker(registerLockerDto);
    }

    @GetMapping("/register/confirm/{historyId}")
    public OuSmartLockerResp confirmRegisterLocker(@PathVariable Long historyId) {
        return lockerService.confirmRegisterLockerSuccessful(historyId);
    }

    @PostMapping("/re-register")
    @PreAuthorize("hasRole('SHIPPER')")
    public OuSmartLockerResp reRegisterLocker(@RequestBody ReRegisterLockerDto reRegisterLockerDto) {
        return lockerService.reRegisterLocker(reRegisterLockerDto);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/all")
    public OuSmartLockerResp getAllLocker() {
        return lockerService.getAlllocker();
    }

    @PostMapping("/re-register/confirm")
    public OuSmartLockerResp confirmLocker(@RequestBody EmailInfoRequestDto emailInfoRequestDto) {
        return lockerService.reRegisterConfirm(emailInfoRequestDto);
    }

    @GetMapping("/history/{historyId}")
    public OuSmartLockerResp getHistoryById(@PathVariable Long historyId) {
        return lockerService.getHistoryById(historyId);
    }
}

