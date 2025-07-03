// LeadCadastroCompletoDTO.java
package com.crm.springSecurity.model.dto;

import com.crm.springSecurity.model.EnderecoLead;
import com.crm.springSecurity.model.DocumentosLead;
import com.crm.springSecurity.model.Lead;
import java.util.List;

public class LeadCadastroCompletoDTO {
    private Lead lead;
    EnderecoLead endereco;
    private List<DocumentosLead> documentos;

    // Getters e setters
    public Lead getLead() { return lead; }
    public void setLead(Lead lead) { this.lead = lead; }

    public EnderecoLead getEndereco() {
        return endereco;
    }

    public void setEnderecos(EnderecoLead endereco) {
        this.endereco = endereco;
    }

    public List<DocumentosLead> getDocumentos() { return documentos; }
    public void setDocumentos(List<DocumentosLead> documentos) { this.documentos = documentos; }
}
