package com.example.ousmartlocker.controller;

import com.example.ousmartlocker.dto.LockerDto;
import com.example.ousmartlocker.dto.LockerLocationDto;
import com.example.ousmartlocker.dto.OuSmartLockerResp;
import com.example.ousmartlocker.model.LockerLocation;
import com.example.ousmartlocker.services.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/location")
public class LocationController {
    @Autowired
    private LocationService locationService;

    @GetMapping("/all")
    public OuSmartLockerResp getAllLocation() {
        return locationService.getAllLocation();
    }

    //Thêm địa điểm
    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public OuSmartLockerResp addLockerLocation(@RequestBody LockerLocation lockerLocation) {
        return locationService.addLockerLocation(lockerLocation);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public OuSmartLockerResp deleteLocation(@PathVariable("id") long id) {
        return locationService.deleteLocation(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public OuSmartLockerResp updateLocation(@PathVariable("id") long id, @RequestBody LockerLocationDto dto) {
        return locationService.updateLocation(id, dto);
    }

    @GetMapping("/count")
    @PreAuthorize("hasRole('ADMIN')")
    public OuSmartLockerResp countLocation() {
        return locationService.countLocation();
    }
}
