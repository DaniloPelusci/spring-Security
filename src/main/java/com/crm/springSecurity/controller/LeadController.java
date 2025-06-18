package com.crm.springSecurity.controller;

import com.crm.springSecurity.model.DocumentosLead;
import com.crm.springSecurity.model.Lead;
import com.crm.springSecurity.model.TipoDocumento;
import com.crm.springSecurity.model.dto.LeadCadastroDTO;
import com.crm.springSecurity.model.dto.LeadEdicaoDTO;
import com.crm.springSecurity.model.dto.LeadFiltroDTO;
import com.crm.springSecurity.repository.DocumentosLeadRepository;
import com.crm.springSecurity.repository.TipoDocumentoRepository;
import com.crm.springSecurity.service.LeadService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@Tag(name = "Leads", description = "APIs para gerenciamento de leads")
@RestController
@RequestMapping("/api/leads")
public class LeadController {
    @Autowired
    private DocumentosLeadRepository repo;

    @Autowired
    private LeadService leadService;

    @Autowired
    private TipoDocumentoRepository tipoDocRepo;

    @Operation(
            summary = "Atualizar um lead",
            description = "Atualiza um lead pelo ID. Requer papel ADMIN ou CORRETOR.",
            parameters = {
                    @Parameter(name = "id", description = "ID do lead", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lead atualizado com sucesso",
                            content = @Content(schema = @Schema(implementation = LeadEdicaoDTO.class))),
                    @ApiResponse(responseCode = "403", description = "Acesso negado"),
                    @ApiResponse(responseCode = "404", description = "Lead não encontrado")
            }
    )
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','CORRETOR')")
    public ResponseEntity<LeadEdicaoDTO> atualizarLead(@PathVariable Long id, @RequestBody LeadEdicaoDTO dto) {
        Lead leadAtualizado = leadService.atualizar(id, dto);
        return ResponseEntity.ok(LeadEdicaoDTO.fromEntity(leadAtualizado));
    }

    @Operation(
            summary = "Criar novo lead",
            description = "Cria um novo lead.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lead criado com sucesso"),
                    @ApiResponse(responseCode = "403", description = "Acesso negado")
            }
    )
    @PostMapping
    public ResponseEntity<?> criarLead(@RequestBody LeadCadastroDTO leadDTO, Authentication authentication) {
        try {
            Lead lead = leadService.cadastrarLead(leadDTO, authentication);
            return ResponseEntity.ok(lead);
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @Operation(
            summary = "Listar todos os leads",
            description = "Retorna todos os leads do sistema.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de leads retornada com sucesso")
            }
    )
    @GetMapping
    public List<Lead> listarTodos() {
        return leadService.listarTodos();
    }

    @Operation(
            summary = "Buscar leads por corretor",
            description = "Busca todos os leads associados a um corretor.",
            parameters = {
                    @Parameter(name = "corretorId", description = "ID do corretor", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de leads do corretor retornada com sucesso")
            }
    )
    @GetMapping("/corretor/{corretorId}")
    public List<Lead> buscarPorCorretor(@PathVariable Long corretorId) {
        return leadService.buscarPorCorretor(corretorId);
    }

    @Operation(
            summary = "Filtrar leads",
            description = "Busca leads de acordo com os filtros passados.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Leads filtrados retornados com sucesso")
            }
    )
    @PostMapping("/filtrar")
    public List<Lead> buscarPorFiltro(@RequestBody LeadFiltroDTO filtro) {
        return leadService.buscarPorFiltro(filtro);
    }

    @Operation(
            summary = "Classificar documento de um lead",
            description = "Classifica um documento do lead de acordo com o tipo de documento.",
            parameters = {
                    @Parameter(name = "id", description = "ID do documento", required = true),
                    @Parameter(name = "tipoDocumentoId", description = "ID do tipo de documento", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Documento classificado com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Tipo de documento inválido"),
                    @ApiResponse(responseCode = "404", description = "Documento não encontrado")
            }
    )
    @PutMapping("/{id}/classificar")
    public ResponseEntity<?> classificarDocumento(
            @PathVariable Long id,
            @RequestParam Long tipoDocumentoId) {
        DocumentosLead doc = repo.findById(id).orElse(null);
        if (doc == null) return ResponseEntity.notFound().build();
        TipoDocumento tipo = tipoDocRepo.findById(tipoDocumentoId).orElse(null);
        if (tipo == null) return ResponseEntity.badRequest().body("Tipo de documento inválido.");
        doc.setTipoDocumento(tipo);
        repo.save(doc);
        return ResponseEntity.ok("Documento classificado.");
    }

    @Operation(
            summary = "Gerar link para upload de documento",
            description = "Gera um link único para upload de documentos para o lead.",
            parameters = {
                    @Parameter(name = "id", description = "ID do lead", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "URL gerada com sucesso")
            }
    )
    @PostMapping("/{id}/gerar-link-upload")
    // @PreAuthorize("hasRole('CORRETOR') or hasRole('ADMIN')") // descomente se já usar roles
    public String gerarLinkUpload(@PathVariable Long id) {
        String codigo = leadService.gerarCodigoUpload(id);
        String url = "https://seusistema.com/envio-documento/" + codigo;
        return url;
    }
}
