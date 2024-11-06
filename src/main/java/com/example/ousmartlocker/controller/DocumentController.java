package com.example.ousmartlocker.controller;

import com.example.ousmartlocker.dto.DocumentDto;
import com.example.ousmartlocker.dto.OuSmartLockerResp;
import com.example.ousmartlocker.model.Document;
import com.example.ousmartlocker.services.IDocumentService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/document")
public class DocumentController {
    @Autowired
    private IDocumentService service;
    @GetMapping
    public OuSmartLockerResp getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public OuSmartLockerResp getDetail(@PathVariable long id) {
        return service.getDetail(id);
    }

    @PostMapping
    public OuSmartLockerResp create(Document dto) {
        return service.create(dto);
    }

    @PutMapping
    public OuSmartLockerResp update(Document dto) {
        return service.update(dto);
    }
}
