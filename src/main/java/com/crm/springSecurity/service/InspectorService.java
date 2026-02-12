package com.crm.springSecurity.service;

import com.crm.springSecurity.model.Inspector;
import com.crm.springSecurity.repository.InspectorRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class InspectorService {

    private final InspectorRepository inspectorRepository;

    public InspectorService(InspectorRepository inspectorRepository) {
        this.inspectorRepository = inspectorRepository;
    }

    public List<Inspector> listar() {
        return inspectorRepository.findAll();
    }

    public Inspector buscarPorId(Long id) {
        return inspectorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Inspetor não encontrado"));
    }

    public Inspector criar(Inspector inspector) {
        inspector.setId(null);
        return inspectorRepository.save(inspector);
    }

    public Inspector atualizar(Long id, Inspector inspector) {
        Inspector existente = buscarPorId(id);
        existente.setNome(inspector.getNome());
        existente.setEmail(inspector.getEmail());
        existente.setTelefone(inspector.getTelefone());
        existente.setAtivo(inspector.getAtivo());
        return inspectorRepository.save(existente);
    }

    public void excluir(Long id) {
        Inspector existente = buscarPorId(id);
        inspectorRepository.delete(existente);
    }
}
