package com.crm.springSecurity.controller;

import com.crm.springSecurity.model.Inspection;
import com.crm.springSecurity.model.dto.InspectionImportResponseDTO;
import com.crm.springSecurity.service.InspectionImportService;
import com.crm.springSecurity.service.InspectionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Tag(name = "Inspections", description = "APIs para importação de inspeções")
@RestController
@RequestMapping("/api/inspections")
public class InspectionController {

    private final InspectionImportService inspectionImportService;
    private final InspectionService inspectionService;

    public InspectionController(InspectionImportService inspectionImportService, InspectionService inspectionService) {
        this.inspectionImportService = inspectionImportService;
        this.inspectionService = inspectionService;
    }

    @Operation(
            summary = "Listar inspeções",
            description = "Retorna inspeções cadastradas. Se informado inspetorId, aplica filtro pelo inspetor.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de inspeções retornada com sucesso")
            }
    )
    @GetMapping
    public List<Inspection> listarInspections(@RequestParam(value = "inspetorId", required = false) Long inspetorId) {
        return inspectionService.listar(inspetorId);
    }


    @Operation(summary = "Buscar inspeção por ID")
    @GetMapping("/{id}")
    public Inspection buscarPorId(@PathVariable Long id) {
        return inspectionService.buscarPorId(id);
    }

    @Operation(summary = "Criar inspeção")
    @PostMapping
    public ResponseEntity<Inspection> criar(@RequestBody Inspection inspection) {
        return ResponseEntity.status(HttpStatus.CREATED).body(inspectionService.criar(inspection));
    }

    @Operation(summary = "Atualizar inspeção")
    @PutMapping("/{id}")
    public Inspection atualizar(@PathVariable Long id, @RequestBody Inspection inspection) {
        return inspectionService.atualizar(id, inspection);
    }

    @Operation(summary = "Excluir inspeção")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        inspectionService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Importar planilha de inspeções",
            description = "Recebe um arquivo .xlsx e grava os registros na tabela inspections.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Planilha importada com sucesso",
                            content = @Content(schema = @Schema(implementation = InspectionImportResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Arquivo inválido")
            }
    )
    @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> importarInspections(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "inspetorId", required = false) Long inspetorId
    ) {
        try {
            InspectionImportResponseDTO response = inspectionImportService.importar(file, inspetorId);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("erro", "Não foi possível ler o arquivo Excel enviado."));
        }
    }
}
