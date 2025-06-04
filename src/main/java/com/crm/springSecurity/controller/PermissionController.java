package com.crm.springSecurity.controller;

import com.crm.springSecurity.alth.modelSecurity.Permission;
import com.crm.springSecurity.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/permissions")
public class PermissionController {

    @Autowired
    private PermissionService service;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<Permission> findAll() {
        return service.findAll();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<Permission> findById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Permission> create(@RequestBody Permission permission) {
        if (service.findAll().stream().anyMatch(p -> p.getDescription().equalsIgnoreCase(permission.getDescription()))) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(service.save(permission));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Permission> update(@PathVariable Long id, @RequestBody Permission permission) {
        if (!service.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        permission.setId(id);
        return ResponseEntity.ok(service.save(permission));
    }

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
