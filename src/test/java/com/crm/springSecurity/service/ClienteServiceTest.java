package com.crm.springSecurity.service;

import com.crm.springSecurity.model.Cliente;
import com.crm.springSecurity.model.Lead;
import com.crm.springSecurity.repository.ClienteRepository;
import com.crm.springSecurity.repository.LeadRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClienteServiceTest {

    @Mock
    private LeadRepository leadRepository;
    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

    public ClienteServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveConverterLeadEmCliente() {
        Lead lead = new Lead();
        lead.setId(1L);
        lead.setNome("Lead Teste");
        when(leadRepository.findById(1L)).thenReturn(Optional.of(lead));
        when(clienteRepository.save(any(Cliente.class))).thenAnswer(i -> i.getArgument(0));

        Cliente cliente = clienteService.converterLead(1L);

        assertNotNull(cliente);
        assertEquals("Lead Teste", cliente.getNome());
        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }

    @Test
    void deveLancarExcecaoQuandoLeadNaoExiste() {
        when(leadRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> clienteService.converterLead(1L));
    }
}
