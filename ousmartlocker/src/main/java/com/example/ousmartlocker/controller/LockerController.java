package com.example.ousmartlocker.controller;

import com.example.ousmartlocker.entity.Locker;
import com.example.ousmartlocker.model.OuSmartLockerResp;
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
    public OuSmartLockerResp registerLocker(@RequestParam("userId") Long userId) {
        return lockerService.registerLocker(userId);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/all")
    public OuSmartLockerResp getAllLocker() {
        return lockerService.getAlllocker();
    }


}

