package com.example.ousmartlocker.services.impl;

import com.example.ousmartlocker.dto.LockerLocationDto;
import com.example.ousmartlocker.dto.OuSmartLockerResp;
import com.example.ousmartlocker.dto.mapper.ModelMapper;
import com.example.ousmartlocker.exception.LockerLocationNotFoundException;
import com.example.ousmartlocker.model.Locker;
import com.example.ousmartlocker.model.LockerLocation;
import com.example.ousmartlocker.repository.LockerLocationRepository;
import com.example.ousmartlocker.repository.LockerRepository;
import com.example.ousmartlocker.services.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationServiceImpl implements LocationService {
    @Autowired
    private LockerLocationRepository lockerLocationRepository;
    @Autowired
    private LockerRepository lockerRepository;

    @Override
    public OuSmartLockerResp getAllLocation() {
        return OuSmartLockerResp.builder().status(HttpStatus.OK).message("Get all location successful").data(lockerLocationRepository.findAll()).build();
    }

    @Override
    public OuSmartLockerResp addLockerLocation(LockerLocation lockerLocation) {
        LockerLocation location = lockerLocationRepository.save(lockerLocation);
        LockerLocationDto lockerLocationDto = ModelMapper.mapToLockerLocationDto(location);
        return OuSmartLockerResp.builder().status(HttpStatus.OK).message("Add location successful").data(lockerLocationDto).build();
    }

    @Override
    public OuSmartLockerResp deleteLocation(long id) {
        LockerLocation location = lockerLocationRepository.findById(id)
                .orElseThrow(() -> new LockerLocationNotFoundException("Location not found"));
        List<Locker> lockers = lockerRepository.findAllByLockerLocation_LocationId(location.getLocationId());
        lockerRepository.deleteAll(lockers);
        lockerLocationRepository.delete(location);
        return OuSmartLockerResp.builder().status(HttpStatus.NO_CONTENT).message("Success").data(null).build();
    }

    @Override
    public OuSmartLockerResp updateLocation(long id, LockerLocationDto dto) {
        LockerLocation locker = lockerLocationRepository.findById(id)
                .orElseThrow(() -> new LockerLocationNotFoundException("Location not found"));
        locker.setLocationId(dto.getLocationId());
        locker.setLocation(dto.getLocation());
        lockerLocationRepository.save(locker);
        LockerLocationDto lockerLocationDto = ModelMapper.mapToLockerLocationDto(locker);
        return OuSmartLockerResp.builder()
                .status(HttpStatus.NO_CONTENT)
                .message("Success")
                .data(lockerLocationDto)
                .build();
    }

    @Override
    public OuSmartLockerResp countLocation() {
        long num = lockerLocationRepository.count();
        return OuSmartLockerResp.builder().status(HttpStatus.OK).message("Count user success").data(num).build();
    }
}
