package com.crm.springSecurity.repository;

import com.crm.springSecurity.model.EnderecoLead;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EnderecoLeadRepository extends JpaRepository<EnderecoLead, Long> {
    List<EnderecoLead> findByLeadId(Long leadId);
}
