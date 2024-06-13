package com.example.ousmartlocker.services;

import com.example.ousmartlocker.dto.LockerLocationDto;
import com.example.ousmartlocker.dto.OuSmartLockerResp;
import com.example.ousmartlocker.model.LockerLocation;

public interface LocationService {
    OuSmartLockerResp getAllLocation();

    OuSmartLockerResp addLockerLocation(LockerLocation lockerLocation);

    OuSmartLockerResp deleteLocation(long id);

    OuSmartLockerResp updateLocation(long id, LockerLocationDto dto);
}
