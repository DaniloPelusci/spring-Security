package com.crm.springsecurity.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.crm.springsecurity.dto.ZipUploadResultDto;
import com.crm.springsecurity.service.ZipInspectionUploadService;

@RestController
@RequestMapping("/api/inspections")
public class InspectionUploadController {

    private final ZipInspectionUploadService zipInspectionUploadService;

    public InspectionUploadController(ZipInspectionUploadService zipInspectionUploadService) {
        this.zipInspectionUploadService = zipInspectionUploadService;
    }

    @PostMapping(value = "/upload-zip", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ZipUploadResultDto uploadZip(@RequestParam("file") MultipartFile file) {
        return zipInspectionUploadService.upload(file);
    }
}
