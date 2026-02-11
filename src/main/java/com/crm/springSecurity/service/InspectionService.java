package com.crm.springSecurity.service;

import com.crm.springSecurity.model.Inspection;
import com.crm.springSecurity.repository.InspectionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InspectionService {

    private final InspectionRepository inspectionRepository;

    public InspectionService(InspectionRepository inspectionRepository) {
        this.inspectionRepository = inspectionRepository;
    }

    public List<Inspection> listar(Long inspetorId) {
        if (inspetorId == null) {
            return inspectionRepository.findAll();
        }
        return inspectionRepository.findByInspetorId(inspetorId);
    }
}
