package com.crm.springSecurity.controller;

import com.crm.springSecurity.model.Inspector;
import com.crm.springSecurity.service.InspectorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Inspetores", description = "APIs para cadastro de inspetores")
@RestController
@RequestMapping("/api/inspectors")
public class InspectorController {

    private final InspectorService inspectorService;

    public InspectorController(InspectorService inspectorService) {
        this.inspectorService = inspectorService;
    }

    @Operation(summary = "Listar inspetores")
    @GetMapping
    public List<Inspector> listar() {
        return inspectorService.listar();
    }

    @Operation(summary = "Buscar inspetor por ID")
    @GetMapping("/{id}")
    public Inspector buscarPorId(@PathVariable Long id) {
        return inspectorService.buscarPorId(id);
    }

    @Operation(summary = "Criar inspetor")
    @PostMapping
    public ResponseEntity<Inspector> criar(@RequestBody Inspector inspector) {
        return ResponseEntity.status(HttpStatus.CREATED).body(inspectorService.criar(inspector));
    }

    @Operation(summary = "Atualizar inspetor")
    @PutMapping("/{id}")
    public Inspector atualizar(@PathVariable Long id, @RequestBody Inspector inspector) {
        return inspectorService.atualizar(id, inspector);
    }

    @Operation(summary = "Excluir inspetor")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        inspectorService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
