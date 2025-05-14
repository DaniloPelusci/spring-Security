package com.crm.springSecurity.repository;

import com.crm.springSecurity.modelSecurity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
}
