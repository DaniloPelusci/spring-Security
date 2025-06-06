package com.crm.springSecurity.model.dto;

public class LeadCadastroDTO {
    private String nome;
    private String telefone;
    private String origem;
    private Long corretorId;
    private String statusLeads;
    private String observacao;

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

    public Long getCorretorId() {
        return corretorId;
    }

    public void setCorretorId(Long corretorId) {
        this.corretorId = corretorId;
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
}

