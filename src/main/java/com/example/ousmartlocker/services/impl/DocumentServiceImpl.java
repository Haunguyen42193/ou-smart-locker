package com.example.ousmartlocker.services.impl;

import com.example.ousmartlocker.dto.DocumentDto;
import com.example.ousmartlocker.dto.LockerLocationDto;
import com.example.ousmartlocker.dto.OuSmartLockerResp;
import com.example.ousmartlocker.dto.mapper.ModelMapper;
import com.example.ousmartlocker.model.Department;
import com.example.ousmartlocker.model.Document;
import com.example.ousmartlocker.model.LockerLocation;
import com.example.ousmartlocker.repository.DepartmentRepository;
import com.example.ousmartlocker.repository.DocumentRepository;
import com.example.ousmartlocker.services.IDocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class DocumentServiceImpl implements IDocumentService {
    @Autowired
    private DocumentRepository repository;
    @Autowired
    private DepartmentRepository departmentRepository;
    @Override
    public OuSmartLockerResp getAll() {
        List<Document> documents = repository.findAll();
        List<DocumentDto> dtos = documents.stream().map(ModelMapper::mapToDocumentDto).toList();
        return OuSmartLockerResp.builder().status(HttpStatus.OK).message("Get all document successful").data(dtos).build();
    }

    @Override
    public OuSmartLockerResp getDetail(long id) {
        Document document = repository.findById(id).orElse(null);
        if (Objects.isNull(document)) {
            return OuSmartLockerResp.builder().status(HttpStatus.BAD_REQUEST).message("Can not found Document").build();
        }
        DocumentDto documentDto = ModelMapper.mapToDocumentDto(document);
        return OuSmartLockerResp.builder().status(HttpStatus.OK).message("Get all document successful").data(documentDto).build();
    }

    @Override
    public OuSmartLockerResp create(Document dto) {
        Document document = repository.save(dto);
        DocumentDto documentDto = ModelMapper.mapToDocumentDto(document);
        return OuSmartLockerResp.builder().status(HttpStatus.OK).message("Add document successful").data(documentDto).build();
    }

    @Override
    public OuSmartLockerResp update(Document dto) {
        if (Objects.isNull(dto.getId()) || Objects.isNull(dto.getDepartment()) || Objects.isNull(dto.getDepartment().getId())) {
            return OuSmartLockerResp.builder().status(HttpStatus.BAD_REQUEST).message("Update document fail").build();
        }
        Department department = departmentRepository.findById(dto.getDepartment().getId()).orElse(null);
        if (Objects.isNull(department)) {
            return OuSmartLockerResp.builder().status(HttpStatus.BAD_REQUEST).message("Can not found Department in Document").build();
        }
        dto.setDepartment(department);
        Document document = repository.save(dto);
        DocumentDto documentDto = ModelMapper.mapToDocumentDto(document);
        return OuSmartLockerResp.builder().status(HttpStatus.OK).message("Update document successful").data(documentDto).build();
    }
}
