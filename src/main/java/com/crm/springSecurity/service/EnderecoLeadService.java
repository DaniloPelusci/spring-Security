package com.crm.springSecurity.service;

import com.crm.springSecurity.model.EnderecoLead;
import com.crm.springSecurity.repository.EnderecoLeadRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EnderecoLeadService {
    @Autowired
    private EnderecoLeadRepository repo;

    @Transactional
    public EnderecoLead salvar(EnderecoLead endereco) {
        if (endereco.getPrincipal() != null && endereco.getPrincipal()) {
            repo.marcarTodosComoNaoPrincipal(endereco.getLead().getId());
        }
        repo.save(endereco);
        return repo.save(endereco);
    }

    public List<EnderecoLead> listarPorLead(Long leadId) {
        return repo.findByLeadId(leadId);
    }

    public Optional<EnderecoLead> buscarPorId(Long id) {
        return repo.findById(id);
    }

    public void deletar(Long id) {
        repo.deleteById(id);
    }
}
