package com.example.ousmartlocker.services;

import com.example.ousmartlocker.dto.OpenLockerRequestDto;
import com.example.ousmartlocker.dto.OuSmartLockerResp;
import com.example.ousmartlocker.dto.ReRegisterLockerDto;
import com.example.ousmartlocker.dto.RegisterLockerDto;
import com.example.ousmartlocker.model.Locker;
import com.example.ousmartlocker.model.LockerLocation;

public interface LockerService {
    OuSmartLockerResp addLocker(Locker locker);

    OuSmartLockerResp getAllLocker();

    OuSmartLockerResp getAllLocation();

    OuSmartLockerResp senderRegisterLocker(RegisterLockerDto registerLockerDto);

    OuSmartLockerResp confirmShipperRegisterLocker(Long historyId);

    OuSmartLockerResp shipperRegisterLocker(ReRegisterLockerDto reRegisterLockerDto);

    OuSmartLockerResp addLockerLocation(LockerLocation lockerLocation);

    OuSmartLockerResp confirmSenderRegisterLocker(Long historyId);

    OuSmartLockerResp getHistoryById(Long historyId);

    OuSmartLockerResp shipperRegisterSendLocker(ReRegisterLockerDto reRegisterLockerDto);

    OuSmartLockerResp confirmShipperRegisterSendLocker(Long historyId);

    OuSmartLockerResp receiverRegisterGetLocker(ReRegisterLockerDto reRegisterLockerDto);

    OuSmartLockerResp confirmReceiverRegisterSendLocker(Long historyId);

    OuSmartLockerResp getAllHistory();

    OuSmartLockerResp shipperConfirmOrderLocker(Long historyId);

    OuSmartLockerResp verifyAndOpenLocker(OpenLockerRequestDto request);

    OuSmartLockerResp registerRetry(ReRegisterLockerDto reRegisterLockerDto);
}
