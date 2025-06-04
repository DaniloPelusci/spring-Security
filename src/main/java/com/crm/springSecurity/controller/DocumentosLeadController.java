package com.crm.springSecurity.controller;

import com.crm.springSecurity.model.DocumentosLead;
import com.crm.springSecurity.service.DocumentosLeadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/documentos-lead")
public class DocumentosLeadController {
    @Autowired
    private DocumentosLeadService service;

    @PreAuthorize("hasAnyRole('ADMIN', 'CORRETOR')")
    @PostMapping
    public ResponseEntity<DocumentosLead> criar(@RequestBody DocumentosLead doc) {
        return ResponseEntity.ok(service.salvar(doc));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'CORRETOR')")
    @GetMapping("/lead/{leadId}")
    public ResponseEntity<List<DocumentosLead>> listarPorLead(@PathVariable Long leadId) {
        return ResponseEntity.ok(service.listarPorLead(leadId));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'CORRETOR')")
    @GetMapping("/{id}")
    public ResponseEntity<DocumentosLead> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'CORRETOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}

