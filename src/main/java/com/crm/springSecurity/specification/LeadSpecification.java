package com.crm.springSecurity.specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.crm.springSecurity.model.Lead;
import com.crm.springSecurity.model.dto.LeadFiltroDTO;

import jakarta.persistence.criteria.Predicate;

public class LeadSpecification {
    public static Specification<Lead> filtrar(LeadFiltroDTO filtro) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (filtro.getNome() != null && !filtro.getNome().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("nome")), "%" + filtro.getNome().toLowerCase() + "%"));
            }
            if (filtro.getStatus() != null) {
                predicates.add(cb.equal(root.get("status"), filtro.getStatus()));
            }
            if (filtro.getCorretorId() != null) {
                predicates.add(cb.equal(root.get("corretor").get("id"), filtro.getCorretorId()));
            }
            // Adicione mais filtros conforme a necessidade
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}

