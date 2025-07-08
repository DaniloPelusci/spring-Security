package com.crm.springSecurity.service;

import com.crm.springSecurity.model.Cliente;
import com.crm.springSecurity.model.Lead;
import com.crm.springSecurity.repository.ClienteRepository;
import com.crm.springSecurity.repository.LeadRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ClienteService {

    @Autowired
    private LeadRepository leadRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    public Cliente converterLead(Long leadId) {
        Lead lead = leadRepository.findById(leadId)
                .orElseThrow(() -> new EntityNotFoundException("Lead não encontrado"));
        Cliente cliente = new Cliente();
        cliente.setLead(lead);
        cliente.setNome(lead.getNome());
        cliente.setDataCadastro(LocalDate.now());
        return clienteRepository.save(cliente);
    }
}
