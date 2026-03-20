package com.crm.springSecurity.controller;

import com.crm.springSecurity.model.FotoInspection;
import com.crm.springSecurity.service.FotoInspectionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Fotos de inspeção", description = "APIs para CRUD de fotos ligadas às inspeções")
@RestController
@RequestMapping("/api/foto-inspections")
public class FotoInspectionController {

    private final FotoInspectionService fotoInspectionService;

    public FotoInspectionController(FotoInspectionService fotoInspectionService) {
        this.fotoInspectionService = fotoInspectionService;
    }

    @Operation(summary = "Listar fotos de inspeção")
    @GetMapping
    public List<FotoInspection> listar(@RequestParam(value = "inspectionId", required = false) Long inspectionId) {
        return fotoInspectionService.listar(inspectionId);
    }

    @Operation(summary = "Buscar foto de inspeção por ID")
    @GetMapping("/{id}")
    public FotoInspection buscarPorId(@PathVariable Long id) {
        return fotoInspectionService.buscarPorId(id);
    }

    @Operation(summary = "Criar foto de inspeção")
    @PostMapping
    public ResponseEntity<FotoInspection> criar(@RequestBody FotoInspection fotoInspection) {
        return ResponseEntity.status(HttpStatus.CREATED).body(fotoInspectionService.criar(fotoInspection));
    }

    @Operation(summary = "Atualizar foto de inspeção")
    @PutMapping("/{id}")
    public FotoInspection atualizar(@PathVariable Long id, @RequestBody FotoInspection fotoInspection) {
        return fotoInspectionService.atualizar(id, fotoInspection);
    }

    @Operation(summary = "Excluir foto de inspeção")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        fotoInspectionService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
