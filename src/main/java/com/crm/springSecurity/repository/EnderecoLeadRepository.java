package com.crm.springSecurity.repository;

import com.crm.springSecurity.model.EnderecoLead;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EnderecoLeadRepository extends JpaRepository<EnderecoLead, Long> {
    List<EnderecoLead> findByLeadId(Long leadId);

    @Modifying
    @Query("UPDATE EnderecoLead e SET e.principal = false WHERE e.lead.id = :leadId")
    void marcarTodosComoNaoPrincipal(@Param("leadId") Long leadId);
}
