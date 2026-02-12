package com.crm.springSecurity.service;

import com.crm.springSecurity.model.FotoInspection;
import com.crm.springSecurity.repository.FotoInspectionRepository;
import com.crm.springSecurity.repository.InspectionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class FotoInspectionService {

    private final FotoInspectionRepository fotoInspectionRepository;
    private final InspectionRepository inspectionRepository;

    public FotoInspectionService(FotoInspectionRepository fotoInspectionRepository,
                                 InspectionRepository inspectionRepository) {
        this.fotoInspectionRepository = fotoInspectionRepository;
        this.inspectionRepository = inspectionRepository;
    }

    public List<FotoInspection> listar(Long inspectionId) {
        if (inspectionId == null) {
            return fotoInspectionRepository.findAll();
        }
        return fotoInspectionRepository.findByInspectionId(inspectionId);
    }

    public FotoInspection buscarPorId(Long id) {
        return fotoInspectionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Foto da inspeção não encontrada"));
    }

    public FotoInspection criar(FotoInspection fotoInspection) {
        fotoInspection.setId(null);
        validarInspectionId(fotoInspection.getInspectionId());
        return fotoInspectionRepository.save(fotoInspection);
    }

    public FotoInspection atualizar(Long id, FotoInspection fotoInspection) {
        FotoInspection existente = buscarPorId(id);
        validarInspectionId(fotoInspection.getInspectionId());
        fotoInspection.setId(existente.getId());
        return fotoInspectionRepository.save(fotoInspection);
    }

    public void excluir(Long id) {
        FotoInspection existente = buscarPorId(id);
        fotoInspectionRepository.delete(existente);
    }

    private void validarInspectionId(Long inspectionId) {
        if (inspectionId == null || !inspectionRepository.existsById(inspectionId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "inspection_id inválido");
        }
    }
}
