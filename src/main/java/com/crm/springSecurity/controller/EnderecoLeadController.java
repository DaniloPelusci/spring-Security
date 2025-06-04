package com.crm.springSecurity.controller;

import com.crm.springSecurity.model.EnderecoLead;
import com.crm.springSecurity.service.EnderecoLeadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enderecos-lead")
public class EnderecoLeadController {
    @Autowired
    private EnderecoLeadService service;

    @PreAuthorize("hasAnyRole('ADMIN', 'CORRETOR')")
    @PostMapping
    public ResponseEntity<EnderecoLead> criar(@RequestBody EnderecoLead endereco) {
        return ResponseEntity.ok(service.salvar(endereco));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'CORRETOR')")
    @GetMapping("/lead/{leadId}")
    public ResponseEntity<List<EnderecoLead>> listarPorLead(@PathVariable Long leadId) {
        return ResponseEntity.ok(service.listarPorLead(leadId));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'CORRETOR')")
    @GetMapping("/{id}")
    public ResponseEntity<EnderecoLead> buscarPorId(@PathVariable Long id) {
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
