package com.crm.springSecurity.service;

import com.crm.springSecurity.model.Inspection;
import com.crm.springSecurity.repository.InspectionRepository;
import com.crm.springSecurity.repository.InspectorRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class InspectionService {

    private final InspectionRepository inspectionRepository;
    private final InspectorRepository inspectorRepository;

    public InspectionService(InspectionRepository inspectionRepository, InspectorRepository inspectorRepository) {
        this.inspectionRepository = inspectionRepository;
        this.inspectorRepository = inspectorRepository;
    }

    public List<Inspection> listar(Long inspetorId) {
        if (inspetorId == null) {
            return inspectionRepository.findAll();
        }
        return inspectionRepository.findByInspetorId(inspetorId);
    }

    public Inspection buscarPorId(Long id) {
        return inspectionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Inspeção não encontrada"));
    }

    public Inspection criar(Inspection inspection) {
        inspection.setId(null);
        validarInspetorId(inspection.getInspetorId());
        return inspectionRepository.save(inspection);
    }

    public Inspection atualizar(Long id, Inspection inspection) {
        Inspection existente = buscarPorId(id);
        validarInspetorId(inspection.getInspetorId());
        inspection.setId(existente.getId());
        return inspectionRepository.save(inspection);
    }

    public void excluir(Long id) {
        Inspection existente = buscarPorId(id);
        inspectionRepository.delete(existente);
    }

    private void validarInspetorId(Long inspetorId) {
        if (inspetorId == null) {
            return;
        }

        if (!inspectorRepository.existsById(inspetorId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "inspetor_id inválido");
        }
    }
}
