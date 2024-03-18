package com.example.ousmartlocker.services;

import com.example.ousmartlocker.dto.EmailInfoRequestDto;
import com.example.ousmartlocker.dto.ReRegisterLockerDto;
import com.example.ousmartlocker.dto.RegisterLockerDto;
import com.example.ousmartlocker.model.Locker;
import com.example.ousmartlocker.dto.OuSmartLockerResp;
import com.example.ousmartlocker.model.LockerLocation;

public interface LockerService {
    OuSmartLockerResp addlocker(Locker locker);
    OuSmartLockerResp getAlllocker();
    OuSmartLockerResp registerLocker(RegisterLockerDto registerLockerDto);
    OuSmartLockerResp reRegisterConfirm(EmailInfoRequestDto emailInfoRequestDto);

    OuSmartLockerResp reRegisterLocker(ReRegisterLockerDto reRegisterLockerDto);

    OuSmartLockerResp addLockerLocation(LockerLocation lockerLocation);

    OuSmartLockerResp confirmRegisterLockerSuccessful(Long historyId);

    OuSmartLockerResp getHistoryById(Long historyId);
}
