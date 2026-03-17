package com.crm.springsecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crm.springsecurity.entity.InspectionPhoto;

public interface InspectionPhotoRepository extends JpaRepository<InspectionPhoto, Long> {
}
