package com.crm.springSecurity.model.dto;

import com.crm.springSecurity.model.Lead;

public class LeadEdicaoDTO {
    private Long id;
    private String nome;
    private String cpfCnpj;
    private String telefone;
    private String origem;
    private String statusLeads;
    private String observacao;
    private Long corretorId;
    private String corretorNome;
    private Long correspondenteId;
    private String correspondenteNome;
    private String statusLead;

    // getters e setters


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

    public String getCpfCnpj() {
        return cpfCnpj;
    }

    public void setCpfCnpj(String cpfCnpj) {
        this.cpfCnpj = cpfCnpj;
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

    public String getStatusLeads() {
        return statusLeads;
    }

    public void setStatusLeads(String statusLeads) {
        this.statusLeads = statusLeads;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Long getCorretorId() {
        return corretorId;
    }

    public void setCorretorId(Long corretorId) {
        this.corretorId = corretorId;
    }

    public String getCorretorNome() {
        return corretorNome;
    }

    public void setCorretorNome(String corretorNome) {
        this.corretorNome = corretorNome;
    }

    public Long getCorrespondenteId() {
        return correspondenteId;
    }

    public void setCorrespondenteId(Long correspondenteId) {
        this.correspondenteId = correspondenteId;
    }

    public String getCorrespondenteNome() {
        return correspondenteNome;
    }

    public void setCorrespondenteNome(String correspondenteNome) {
        this.correspondenteNome = correspondenteNome;
    }

    public String getStatusLead() {
        return statusLead;
    }

    public void setStatusLead(String statusLead) {
        this.statusLead = statusLead;
    }

    public static LeadEdicaoDTO fromEntity(Lead lead) {
        LeadEdicaoDTO dto = new LeadEdicaoDTO();
        dto.setId(lead.getId());
        dto.setNome(lead.getNome());
        dto.setCpfCnpj(lead.getCpfCnpj());
        dto.setTelefone(lead.getTelefone());
        dto.setOrigem(lead.getOrigem());
        dto.setStatusLeads(lead.getStatusLeads());
        dto.setObservacao(lead.getObservacao());
        dto.setStatusLead(lead.getStatusLead());

        if (lead.getCorretor() != null) {
            dto.setCorretorId(lead.getCorretor().getId());
            dto.setCorretorNome(lead.getCorretor().getNome());
        }

        if (lead.getCorrespondente() != null) {
            dto.setCorrespondenteId(lead.getCorrespondente().getId());
            dto.setCorrespondenteNome(lead.getCorrespondente().getNome());
        }

        return dto;
    }
}
