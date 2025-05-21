package com.crm.springSecurity.repository;

import com.crm.springSecurity.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {}