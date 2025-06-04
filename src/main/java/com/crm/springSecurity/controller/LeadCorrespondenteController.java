package com.crm.springSecurity.controller;


import com.crm.springSecurity.model.Lead;
import com.crm.springSecurity.model.dto.LeadCorrespondenteDTO;
import com.crm.springSecurity.service.LeadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/correspondente/leads")
public class LeadCorrespondenteController {

    @Autowired
    private LeadService leadService;

    @PreAuthorize("hasAnyRole('ADMIN','CORRESPONDENTE')")
    @GetMapping("/completos")
    public ResponseEntity<List<LeadCorrespondenteDTO>> listarLeadsCompletos() {
        List<Lead> leads = leadService.listarLeadsAptosParaCorrespondente();
        List<LeadCorrespondenteDTO> dtos = leads.stream()
                .map(lead -> new LeadCorrespondenteDTO(lead, gerarTelefoneFake()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PreAuthorize("hasAnyRole('ADMIN','CORRESPONDENTE')")
    @PostMapping("/{leadId}/encaminhar")
    public ResponseEntity<Void> encaminharLead(@PathVariable Long leadId, Authentication authentication) {
        String username = authentication.getName();
        leadService.aceitarLeadPorCorrespondente(leadId, username);
        return ResponseEntity.ok().build();
    }

    private String gerarTelefoneFake() {
        Random r = new Random();
        return String.format("(99)%05d-%04d", r.nextInt(100000), r.nextInt(10000));
    }
}
