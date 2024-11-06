package com.example.ousmartlocker.services;

import com.example.ousmartlocker.dto.DocumentDto;
import com.example.ousmartlocker.dto.OuSmartLockerResp;
import com.example.ousmartlocker.model.Document;

public interface IDocumentService {
    OuSmartLockerResp getAll();

    OuSmartLockerResp create(Document dto);

    OuSmartLockerResp update(Document dto);

    OuSmartLockerResp getDetail(long id);
}
