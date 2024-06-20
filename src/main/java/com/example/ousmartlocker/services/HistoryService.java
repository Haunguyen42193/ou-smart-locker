package com.example.ousmartlocker.services;

import com.example.ousmartlocker.dto.OuSmartLockerResp;

public interface HistoryService {
    OuSmartLockerResp getHistoryRecord(String startDate, String endDate);

    OuSmartLockerResp getAllHistory();

    OuSmartLockerResp getHistoryById(Long historyId);

    OuSmartLockerResp countHistory();
}
