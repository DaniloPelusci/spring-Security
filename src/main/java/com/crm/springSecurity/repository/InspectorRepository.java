package com.crm.springSecurity.repository;

import com.crm.springSecurity.model.Inspector;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InspectorRepository extends JpaRepository<Inspector, Long> {
}
