package com.example.ousmartlocker.controller;

import com.example.ousmartlocker.dto.OuSmartLockerResp;
import com.example.ousmartlocker.services.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/history")
public class HistoryController {
    @Autowired
    private HistoryService historyService;
    @GetMapping("/record")
    @PreAuthorize("hasRole('ADMIN')")
    public OuSmartLockerResp getHistoryRecord(@RequestParam String startDate, @RequestParam String endDate) {
        return historyService.getHistoryRecord(startDate, endDate);
    }

    @GetMapping("/all")
    public OuSmartLockerResp getAllHistory() {
        return historyService.getAllHistory();
    }

    @GetMapping("/{historyId}")
    public OuSmartLockerResp getHistoryById(@PathVariable Long historyId) {
        return historyService.getHistoryById(historyId);
    }

    @GetMapping("/count")
    @PreAuthorize("hasRole('ADMIN')")
    public OuSmartLockerResp countHistory() {
        return historyService.countHistory();
    }
}
