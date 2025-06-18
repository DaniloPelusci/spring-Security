package com.crm.springSecurity.controller;

import com.crm.springSecurity.model.EnderecoLead;
import com.crm.springSecurity.service.EnderecoLeadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Endereço Lead",
        description = "APIs para gerenciamento de endereços dos leads"
)
@RestController
@RequestMapping("/api/enderecos-lead")
public class EnderecoLeadController {
    @Autowired
    private EnderecoLeadService service;

    @Operation(
            summary = "Criar novo endereço para lead",
            description = "Cria e salva um novo endereço para o lead. Requer papel ADMIN ou CORRETOR.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Endereço criado com sucesso"),
                    @ApiResponse(responseCode = "403", description = "Acesso negado")
            }
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'CORRETOR')")
    @PostMapping
    public ResponseEntity<EnderecoLead> criar(@RequestBody EnderecoLead endereco) {
        return ResponseEntity.ok(service.salvar(endereco));
    }

    @Operation(
            summary = "Listar endereços de um lead",
            description = "Lista todos os endereços associados a um lead. Requer papel ADMIN ou CORRETOR.",
            parameters = {
                    @Parameter(name = "leadId", description = "ID do lead", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de endereços retornada com sucesso"),
                    @ApiResponse(responseCode = "403", description = "Acesso negado")
            }
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'CORRETOR')")
    @GetMapping("/lead/{leadId}")
    public ResponseEntity<List<EnderecoLead>> listarPorLead(@PathVariable Long leadId) {
        return ResponseEntity.ok(service.listarPorLead(leadId));
    }

    @Operation(
            summary = "Buscar endereço por ID",
            description = "Busca um endereço pelo seu ID. Requer papel ADMIN ou CORRETOR.",
            parameters = {
                    @Parameter(name = "id", description = "ID do endereço", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Endereço retornado com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Endereço não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Acesso negado")
            }
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'CORRETOR')")
    @GetMapping("/{id}")
    public ResponseEntity<EnderecoLead> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Deletar endereço",
            description = "Remove um endereço pelo ID. Requer papel ADMIN ou CORRETOR.",
            parameters = {
                    @Parameter(name = "id", description = "ID do endereço", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "204", description = "Endereço deletado com sucesso"),
                    @ApiResponse(responseCode = "403", description = "Acesso negado")
            }
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'CORRETOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
