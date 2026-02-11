package com.crm.springSecurity.repository;

import com.crm.springSecurity.model.Inspection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InspectionRepository extends JpaRepository<Inspection, Long> {
    java.util.List<Inspection> findByInspetorId(Long inspetorId);
}
