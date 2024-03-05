package com.example.ousmartlocker.controller;

import com.example.ousmartlocker.dto.EmailInfoRequestDto;
import com.example.ousmartlocker.dto.ReRegisterLockerDto;
import com.example.ousmartlocker.model.Locker;
import com.example.ousmartlocker.dto.OuSmartLockerResp;
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
    public OuSmartLockerResp addLocker(@RequestBody Locker locker) {
        return lockerService.addlocker(locker);
    }

    @PostMapping("/register")
    public OuSmartLockerResp registerLocker() {
        return lockerService.registerLocker();
    }

    @PostMapping("/re-register")
    public OuSmartLockerResp reRegisterLocker(@RequestBody ReRegisterLockerDto reRegisterLockerDto) {
        return lockerService.reRegisterLocker(reRegisterLockerDto);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/all")
    public OuSmartLockerResp getAllLocker() {
        return lockerService.getAlllocker();
    }

    @PostMapping("/confirm")
    public OuSmartLockerResp confirmLocker(@RequestBody EmailInfoRequestDto emailInfoRequestDto) {
        return lockerService.confirm(emailInfoRequestDto);
    }
}

