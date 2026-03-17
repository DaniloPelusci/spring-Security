package com.crm.springsecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crm.springsecurity.entity.Inspector;

public interface InspectorRepository extends JpaRepository<Inspector, Long> {
}
