package com.example.ousmartlocker.services;

import com.example.ousmartlocker.entity.Locker;
import com.example.ousmartlocker.model.OuSmartLockerResp;

public interface LockerService {
    OuSmartLockerResp addlocker(Locker locker);
    OuSmartLockerResp getAlllocker();
    OuSmartLockerResp registerLocker(Long userId);
}
