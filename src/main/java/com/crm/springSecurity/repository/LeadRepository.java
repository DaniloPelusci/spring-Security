package com.crm.springSecurity.repository;

import com.crm.springSecurity.model.Lead;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeadRepository extends JpaRepository<Lead, Long> {
}
