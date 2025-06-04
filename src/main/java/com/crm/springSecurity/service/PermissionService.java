package com.crm.springSecurity.service;

import com.crm.springSecurity.alth.modelSecurity.Permission;
import com.crm.springSecurity.alth.repository.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PermissionService {

    @Autowired
    private PermissionRepository repository;

    public Permission save(Permission permission) {
        return repository.save(permission);
    }

    public List<Permission> findAll() {
        return repository.findAll();
    }

    public Optional<Permission> findById(Long id) {
        return repository.findById(id);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
