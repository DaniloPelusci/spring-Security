package com.crm.springSecurity.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.crm.springSecurity.model.Lead;

public interface LeadRepository extends JpaRepository<Lead, Long>, JpaSpecificationExecutor<Lead> {
	 List<Lead> findByCorretorId(Long corretorId);
	Optional<Lead> findByCodigoUpload(String codigoUpload);

}
