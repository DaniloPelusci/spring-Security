package com.crm.springSecurity.model.dto;


import com.crm.springSecurity.model.Lead;

public class LeadCorrespondenteDTO {
    private Long id;
    private String nome;
    private String telefone; // Falso/mascarado
    private String origem;
    private String statusLead;

    public LeadCorrespondenteDTO(Lead lead, String telefoneFake) {
        this.id = lead.getId();
        this.nome = lead.getNome();
        this.telefone = telefoneFake;
        this.origem = lead.getOrigem();
        this.statusLead = lead.getStatusLead();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getOrigem() {
        return origem;
    }

    public void setOrigem(String origem) {
        this.origem = origem;
    }

    public String getStatusLead() {
        return statusLead;
    }

    public void setStatusLead(String statusLead) {
        this.statusLead = statusLead;
    }
}
