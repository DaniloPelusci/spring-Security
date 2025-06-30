package com.crm.springSecurity.service;

import com.crm.springSecurity.model.EnderecoLead;
import com.crm.springSecurity.repository.EnderecoLeadRepository;
import jakarta.persistence.EntityNotFoundException;
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

    public EnderecoLead atualizar( EnderecoLead enderecoAtualizado) {
        EnderecoLead existente = repo.findById(enderecoAtualizado.getLead().getId())
                .orElseThrow(() -> new EntityNotFoundException("Endereço não encontrado"));

        // Atualiza campos
        existente.setLogradouro(enderecoAtualizado.getLogradouro());
        existente.setNumero(enderecoAtualizado.getNumero());
        existente.setComplemento(enderecoAtualizado.getComplemento());
        existente.setBairro(enderecoAtualizado.getBairro());
        existente.setCidade(enderecoAtualizado.getCidade());
        existente.setEstado(enderecoAtualizado.getEstado());
        existente.setCep(enderecoAtualizado.getCep());
        existente.setPrincipal(enderecoAtualizado.getPrincipal());
        // Se quiser permitir alterar o lead, faça:
        if (enderecoAtualizado.getLead() != null)
            existente.setLead(enderecoAtualizado.getLead());

        return repo.save(existente);
    }

    @Transactional
    public void definirComoPrincipal(Long leadId, Long enderecoId) {
        // 1. Torna todos os endereços desse lead como principal = false
        repo.marcarTodosComoNaoPrincipal(leadId);

        // 2. Marca o endereço desejado como principal
        EnderecoLead endereco = repo.findById(enderecoId)
                .orElseThrow(() -> new RuntimeException("Endereço não encontrado"));
        endereco.setPrincipal(true);
        repo.save(endereco);
    }
}
