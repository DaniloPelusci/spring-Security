package com.crm.springSecurity.controller;

import com.crm.springSecurity.alth.modelSecurity.Permission;
import com.crm.springSecurity.service.PermissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Permissões",
        description = "APIs para gerenciamento de permissões dos usuários (apenas ADMIN)"
)
@RestController
@RequestMapping("/api/permissions")
public class PermissionController {

    @Autowired
    private PermissionService service;

    @Operation(
            summary = "Listar todas as permissões",
            description = "Retorna todas as permissões cadastradas. Requer papel ADMIN.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Permissões listadas com sucesso")
            }
    )
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<Permission> findAll() {
        return service.findAll();
    }

    @Operation(
            summary = "Buscar permissão por ID",
            description = "Retorna uma permissão pelo seu ID. Requer papel ADMIN.",
            parameters = {
                    @Parameter(name = "id", description = "ID da permissão", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Permissão retornada com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Permissão não encontrada")
            }
    )
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<Permission> findById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Criar nova permissão",
            description = "Cria uma nova permissão. Não permite permissões duplicadas (descrição única). Requer papel ADMIN.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(schema = @Schema(implementation = Permission.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Permissão criada com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Permissão já existe")
            }
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Permission> create(@RequestBody Permission permission) {
        if (service.findAll().stream().anyMatch(p -> p.getDescription().equalsIgnoreCase(permission.getDescription()))) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(service.save(permission));
    }

    @Operation(
            summary = "Atualizar permissão",
            description = "Atualiza os dados de uma permissão existente pelo ID. Requer papel ADMIN.",
            parameters = {
                    @Parameter(name = "id", description = "ID da permissão", required = true)
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(schema = @Schema(implementation = Permission.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Permissão atualizada com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Permissão não encontrada")
            }
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Permission> update(@PathVariable Long id, @RequestBody Permission permission) {
        if (!service.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        permission.setId(id);
        return ResponseEntity.ok(service.save(permission));
    }

    @Operation(
            summary = "Deletar permissão",
            description = "Remove uma permissão pelo ID. Requer papel ADMIN.",
            parameters = {
                    @Parameter(name = "id", description = "ID da permissão", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "204", description = "Permissão deletada com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Permissão não encontrada")
            }
    )
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!service.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
