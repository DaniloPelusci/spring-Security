package com.crm.springSecurity.alth.repository;

import com.crm.springSecurity.alth.modelSecurity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
    @Modifying
    @Query("SELECT p from Permission p  WHERE p.description = :description")
    Permission findByDescription(String description);
}
