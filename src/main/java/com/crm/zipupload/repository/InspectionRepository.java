package com.crm.zipupload.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crm.zipupload.entity.Inspection;

public interface InspectionRepository extends JpaRepository<Inspection, Long> {

    Optional<Inspection> findByInspectorIdAndWorkOrderNumber(Long inspectorId, String workOrderNumber);
}
