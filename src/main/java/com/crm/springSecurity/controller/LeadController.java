package com.crm.springSecurity.controller;

import com.crm.springSecurity.model.Lead;
import com.crm.springSecurity.model.dto.LeadCadastroDTO;
import com.crm.springSecurity.model.dto.LeadFiltroDTO;
import com.crm.springSecurity.service.LeadService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/leads")
public class LeadController {

    @Autowired
    private LeadService leadService;

    @PostMapping
    public ResponseEntity<?> criarLead(@RequestBody LeadCadastroDTO leadDTO, Authentication authentication) {
        try {
            Lead lead = leadService.cadastrarLead(leadDTO, authentication);
            return ResponseEntity.ok(lead);
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }
    
    @GetMapping
    public List<Lead> listarTodos() {
        return leadService.listarTodos();
    }

    @GetMapping("/corretor/{corretorId}")
    public List<Lead> buscarPorCorretor(@PathVariable Long corretorId) {
        return leadService.buscarPorCorretor(corretorId);
    }

    @PostMapping("/filtrar")
    public List<Lead> buscarPorFiltro(@RequestBody LeadFiltroDTO filtro) {
        return leadService.buscarPorFiltro(filtro);
    }
}

