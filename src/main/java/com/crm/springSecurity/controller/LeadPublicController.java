package com.crm.springSecurity.controller;

import com.crm.springSecurity.service.LeadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/public/leads")
public class LeadPublicController {

    @Autowired
    private LeadService leadService;

    @PostMapping("/upload-documento/{codigo}")
    public ResponseEntity<?> uploadDocumento(
            @PathVariable String codigo,
            @RequestParam("file") MultipartFile file) {
        boolean ok = leadService.salvarDocumentoPorCodigo(codigo, file);
        if (ok) return ResponseEntity.ok("Documento recebido. Aguardando análise.");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Código inválido ou expirado.");
    }
}

