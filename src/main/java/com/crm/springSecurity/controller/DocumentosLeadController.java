package com.crm.springSecurity.controller;

import com.crm.springSecurity.alth.modelSecurity.User;
import com.crm.springSecurity.model.DocumentosLead;
import com.crm.springSecurity.model.Lead;
import com.crm.springSecurity.repository.DocumentosLeadRepository;
import com.crm.springSecurity.service.DocumentosLeadService;
import com.crm.springSecurity.service.LeadService;
import com.crm.springSecurity.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/documentos-lead")
public class DocumentosLeadController {
    @Autowired
    private DocumentosLeadService service;

    @Autowired
    private DocumentosLeadRepository documentoLeadRepository;
    @Autowired
    private LeadService leadService;
    @Autowired
    private UserService userService;
    @Autowired
    private DocumentosLeadService documentosLeadService;

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

    public DocumentosLead buscarDocumentoPorId(Long documentoId) {
        return documentoLeadRepository.findById(documentoId)
                .orElseThrow(() -> new EntityNotFoundException("Documento não encontrado"));
    }

    @GetMapping("/{documentoId}/download")
    public ResponseEntity<?> baixarDocumento(
            @PathVariable Long leadId,
            @PathVariable Long documentoId,
            Authentication authentication) {

        User user = userService.getUsuarioByAuthentication(authentication);
        Lead lead = leadService.findbyId(leadId).get();

        if (!leadService.podeBaixarDocumento(lead, user)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acesso negado.");
        }

        DocumentosLead documento = documentosLeadService.buscarPorId(documentoId).get();

        if (!documento.getLead().getId().equals(leadId)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Documento não pertence a este lead.");
        }

        // Retornar o conteúdo do bytea como download
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\"" + documento.getNomeArquivo() + "\"")
                .body(documento.getConteudo());
    }

}

