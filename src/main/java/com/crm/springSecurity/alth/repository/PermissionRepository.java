package com.crm.springSecurity.alth.repository;

import com.crm.springSecurity.alth.modelSecurity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
}
