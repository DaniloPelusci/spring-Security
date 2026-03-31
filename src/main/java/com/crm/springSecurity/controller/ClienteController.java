package com.crm.springSecurity.controller;

import com.crm.springSecurity.model.Cliente;
import com.crm.springSecurity.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @PostMapping("/converter/{leadId}")
    public Cliente converterLead(@PathVariable Long leadId) {
        return clienteService.converterLead(leadId);
    }
}
