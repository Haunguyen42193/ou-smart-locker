package com.example.ousmartlocker.services;

import com.example.ousmartlocker.dto.EmailInfoRequestDto;
import com.example.ousmartlocker.dto.ReRegisterLockerDto;
import com.example.ousmartlocker.model.Locker;
import com.example.ousmartlocker.dto.OuSmartLockerResp;

public interface LockerService {
    OuSmartLockerResp addlocker(Locker locker);
    OuSmartLockerResp getAlllocker();
    OuSmartLockerResp registerLocker();
    OuSmartLockerResp confirm(EmailInfoRequestDto emailInfoRequestDto);

    OuSmartLockerResp reRegisterLocker(ReRegisterLockerDto reRegisterLockerDto);
}
