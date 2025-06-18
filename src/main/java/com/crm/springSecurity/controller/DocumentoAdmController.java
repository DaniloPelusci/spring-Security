package com.crm.springSecurity.controller;

import com.crm.springSecurity.model.DocumentosLead;
import com.crm.springSecurity.model.TipoDocumento;
import com.crm.springSecurity.repository.DocumentosLeadRepository;
import com.crm.springSecurity.repository.TipoDocumentoRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "Administração de Documentos",
        description = "APIs para classificação de documentos dos leads"
)
@RestController
@RequestMapping("/api/documentos")
public class DocumentoAdmController {

    @Autowired
    private DocumentosLeadRepository repo;

    @Autowired
    private TipoDocumentoRepository tipoDocRepo;

    @Operation(
            summary = "Classificar documento de lead",
            description = "Atualiza o tipo de documento de um documento do lead. Ideal para uso administrativo.",
            parameters = {
                    @Parameter(name = "id", description = "ID do documento do lead", required = true),
                    @Parameter(name = "tipoDocumentoId", description = "ID do novo tipo de documento", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Documento classificado com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Documento ou tipo de documento não encontrado")
            }
    )
    @PutMapping("/{id}/classificar")
    public ResponseEntity<?> classificarDocumento(
            @PathVariable Long id,
            @RequestParam Long tipoDocumentoId) {
        var docOpt = repo.findById(id);
        var tipoOpt = tipoDocRepo.findById(tipoDocumentoId);
        if (docOpt.isEmpty() || tipoOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Documento ou tipo de documento não encontrado.");
        }
        DocumentosLead doc = docOpt.get();
        doc.setTipoDocumento(tipoOpt.get());
        repo.save(doc);
        return ResponseEntity.ok("Documento classificado.");
    }
}
