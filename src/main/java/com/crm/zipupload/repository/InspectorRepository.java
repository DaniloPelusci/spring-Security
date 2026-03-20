package com.crm.zipupload.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crm.zipupload.entity.Inspector;

public interface InspectorRepository extends JpaRepository<Inspector, Long> {
}
