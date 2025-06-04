package com.crm.springSecurity.service;

// src/test/java/com/crm/springSecurity/service/LeadServiceTest.java

import com.crm.springSecurity.model.Lead;
import com.crm.springSecurity.model.dto.LeadCadastroDTO;
import com.crm.springSecurity.repository.LeadRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

//@SpringBootTest
class LeadServiceTest {

    @Autowired
    private LeadService leadService;

    @Autowired
    private LeadRepository leadRepository;

//    @Test
    void deveCriarLeadComSucesso() {
        LeadCadastroDTO dto = new LeadCadastroDTO();
        dto.setNome("Lead de Teste");
        dto.setTelefone("62999999999");
        dto.setOrigem("LinkedIn");
        dto.setStatusLeads("Novo");
        dto.setObservacao("Lead criado em teste automatizado.");

        Lead leadSalvo = leadService.cadastrarLead(dto, null);
        assertNotNull(leadSalvo.getId());
        assertEquals("Lead de Teste", leadSalvo.getNome());
    }

    @AfterEach
    void limpaBanco() {
        leadRepository.deleteAll();
    }
}

