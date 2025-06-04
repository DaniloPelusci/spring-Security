package com.crm.springSecurity.repository;

import com.crm.springSecurity.model.DocumentosLead;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentosLeadRepository extends JpaRepository<DocumentosLead, Long> {
    List<DocumentosLead> findByLeadId(Long leadId);
}
