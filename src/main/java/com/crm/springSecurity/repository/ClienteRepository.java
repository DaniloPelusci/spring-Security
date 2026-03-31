package com.crm.springSecurity.repository;

import com.crm.springSecurity.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}
