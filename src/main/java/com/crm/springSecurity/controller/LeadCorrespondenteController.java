package com.crm.springSecurity.controller;

import com.crm.springSecurity.model.Lead;
import com.crm.springSecurity.model.dto.LeadCorrespondenteDTO;
import com.crm.springSecurity.service.LeadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Tag(name = "Lead Correspondente", description = "APIs para correspondentes acessarem e encaminharem leads")
@RestController
@RequestMapping("/api/correspondente/leads")
public class LeadCorrespondenteController {

    @Autowired
    private LeadService leadService;

    @Operation(
            summary = "Listar leads completos aptos para correspondente",
            description = "Retorna a lista de leads prontos para análise do correspondente. Requer papel ADMIN ou CORRESPONDENTE.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de leads aptos retornada com sucesso"),
                    @ApiResponse(responseCode = "403", description = "Acesso negado")
            }
    )
    @PreAuthorize("hasAnyRole('ADMIN','CORRESPONDENTE')")
    @GetMapping("/completos")
    public ResponseEntity<List<LeadCorrespondenteDTO>> listarLeadsCompletos() {
        List<Lead> leads = leadService.listarLeadsAptosParaCorrespondente();
        List<LeadCorrespondenteDTO> dtos = leads.stream()
                .map(lead -> new LeadCorrespondenteDTO(lead, gerarTelefoneFake()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @Operation(
            summary = "Encaminhar lead para análise do correspondente",
            description = "Permite que o correspondente aceite e encaminhe um lead para seu nome. Requer papel ADMIN ou CORRESPONDENTE.",
            parameters = {
                    @Parameter(name = "leadId", description = "ID do lead a ser encaminhado", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lead encaminhado com sucesso"),
                    @ApiResponse(responseCode = "403", description = "Acesso negado"),
                    @ApiResponse(responseCode = "404", description = "Lead não encontrado")
            }
    )
    @PreAuthorize("hasAnyRole('ADMIN','CORRESPONDENTE')")
    @PostMapping("/{leadId}/encaminhar")
    public ResponseEntity<Void> encaminharLead(
            @PathVariable Long leadId,
            Authentication authentication) {
        String username = authentication.getName();
        leadService.aceitarLeadPorCorrespondente(leadId, username);
        return ResponseEntity.ok().build();
    }

    private String gerarTelefoneFake() {
        Random r = new Random();
        return String.format("(99)%05d-%04d", r.nextInt(100000), r.nextInt(10000));
    }
}
