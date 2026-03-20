package com.crm.springSecurity.repository;

import com.crm.springSecurity.model.FotoInspection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FotoInspectionRepository extends JpaRepository<FotoInspection, Long> {
    List<FotoInspection> findByInspectionId(Long inspectionId);
}
