package com.crm.springSecurity.controller;

import com.crm.springSecurity.alth.modelSecurity.User;
import com.crm.springSecurity.exeption.ApiErrorResponse;
import com.crm.springSecurity.model.DocumentosLead;
import com.crm.springSecurity.model.Lead;
import com.crm.springSecurity.repository.DocumentosLeadRepository;
import com.crm.springSecurity.service.DocumentosLeadService;
import com.crm.springSecurity.service.LeadService;
import com.crm.springSecurity.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Tag(
        name = "Documentos Lead",
        description = "APIs para gerenciamento e download de documentos de leads"
)
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
    @Autowired
    private DocumentosLeadRepository documentosLeadRepository;

    @Operation(
            summary = "Criar novo documento para lead",
            description = "Cria e salva um novo documento para um lead. Requer papel ADMIN ou CORRETOR.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Documento criado com sucesso",
                            content = @Content(schema = @Schema(implementation = DocumentosLead.class))),
                    @ApiResponse(responseCode = "403", description = "Acesso negado")
            }
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'CORRETOR')")
    @PostMapping
    public ResponseEntity<DocumentosLead> criar(@RequestBody DocumentosLead doc) {
        return ResponseEntity.ok(service.salvar(doc));
    }

    @Operation(
            summary = "Listar documentos de um lead",
            description = "Lista todos os documentos associados a um lead. Requer papel ADMIN ou CORRETOR.",
            parameters = {
                    @Parameter(name = "leadId", description = "ID do lead", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de documentos retornada com sucesso"),
                    @ApiResponse(responseCode = "403", description = "Acesso negado")
            }
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'CORRETOR')")
    @GetMapping("/lead/{leadId}")
    public ResponseEntity<List<DocumentosLead>> listarPorLead(@PathVariable Long leadId) {
        return ResponseEntity.ok(service.listarPorLead(leadId));
    }

    @Operation(
            summary = "Buscar documento por ID",
            description = "Busca um documento pelo seu ID. Requer papel ADMIN ou CORRETOR.",
            parameters = {
                    @Parameter(name = "id", description = "ID do documento", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Documento retornado com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Documento não encontrado"),
                    @ApiResponse(responseCode = "403", description = "Acesso negado")
            }
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'CORRETOR')")
    @GetMapping("/{id}")
    public ResponseEntity<DocumentosLead> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Deletar documento",
            description = "Remove um documento pelo ID. Requer papel ADMIN ou CORRETOR.",
            parameters = {
                    @Parameter(name = "id", description = "ID do documento", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "204", description = "Documento deletado com sucesso"),
                    @ApiResponse(responseCode = "403", description = "Acesso negado")
            }
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'CORRETOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Download de documento",
            description = "Permite baixar o documento se o usuário possuir permissão. Valida se o documento pertence ao lead e se o usuário pode acessar. Retorna arquivo binário.",
            parameters = {
                    @Parameter(name = "leadId", description = "ID do lead", required = true),
                    @Parameter(name = "documentoId", description = "ID do documento", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Arquivo baixado com sucesso"),
                    @ApiResponse(responseCode = "403", description = "Acesso negado ou sem permissão para baixar o documento"),
                    @ApiResponse(responseCode = "400", description = "Documento não pertence a este lead"),
                    @ApiResponse(responseCode = "404", description = "Lead ou documento não encontrado")
            }
    )
    @GetMapping("/{documentoId}/download")
    public ResponseEntity<?> baixarDocumento(
            @PathVariable Long leadId,
            @PathVariable Long documentoId,
            Authentication authentication) {

        User user = userService.getUsuarioByAuthentication(authentication);
        Lead lead = leadService.findbyId(leadId).get();

        if (!leadService.podeBaixarDocumento(lead, user)) {
            ApiErrorResponse error = new ApiErrorResponse(
                    HttpStatus.FORBIDDEN.value(),
                    "Acesso negado. Você não tem permissão para baixar este documento."
            );
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
        }

        DocumentosLead documento = documentosLeadService.buscarPorId(documentoId).get();

        if (!documento.getLead().getId().equals(leadId)) {
            ApiErrorResponse error = new ApiErrorResponse(
                    HttpStatus.BAD_REQUEST.value(),
                    "Documento não pertence a este lead."
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }

        // Retornar o conteúdo do bytea como download
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\"" + documento.getNomeArquivo() + "\"")
                .body(documento.getConteudo());
    }

    // Este método não é endpoint REST, então não precisa de documentação Swagger.
    public DocumentosLead buscarDocumentoPorId(Long documentoId) {
        return documentoLeadRepository.findById(documentoId)
                .orElseThrow(() -> new EntityNotFoundException("Documento não encontrado"));
    }
    @PostMapping("/api/leads/upload-documentos")
    public ResponseEntity<?> uploadDocumentos(
            @RequestParam("leadId") Long leadId,
            @RequestParam("arquivos") List<MultipartFile> arquivos
    ) {
             documentosLeadService.salvarArquivo(leadId, arquivos);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/api/documentos-lead/{id}/download")
    public ResponseEntity<byte[]> downloadDocumento(@PathVariable Long id) {
        DocumentosLead doc = documentosLeadRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + doc.getNomeArquivo() + "\"")
                .contentType(MediaType.parseMediaType(doc.getTipoArquivo()))
                .body(doc.getConteudo());
    }
}
