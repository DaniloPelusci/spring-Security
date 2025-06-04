package com.crm.springSecurity.service;

import com.crm.springSecurity.model.DocumentosLead;
import com.crm.springSecurity.repository.DocumentosLeadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DocumentosLeadService {
    @Autowired
    private DocumentosLeadRepository repo;

    public DocumentosLead salvar(DocumentosLead doc) {
        return repo.save(doc);
    }

    public List<DocumentosLead> listarPorLead(Long leadId) {
        return repo.findByLeadId(leadId);
    }

    public Optional<DocumentosLead> buscarPorId(Long id) {
        return repo.findById(id);
    }

    public void deletar(Long id) {
        repo.deleteById(id);
    }
}

