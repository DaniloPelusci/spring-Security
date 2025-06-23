package com.crm.springSecurity.repository;

import aj.org.objectweb.asm.commons.Remapper;
import com.crm.springSecurity.model.TipoDocumento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TipoDocumentoRepository extends JpaRepository<TipoDocumento, Long> {
    Optional<TipoDocumento> findByDescricao(String descricao);
}

