package com.crm.springSecurity.model;

import jakarta.persistence.*;

@Entity
@Table(name = "leads")
public class Lead {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private String telefone;

    private String cpfCnpj;

    private String origem;

    private String statusLeads;

    private String observacao;

    @ManyToOne
    @JoinColumn(name = "corretor_id")
    private Usuario corretor;

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

    public String getCpfCnpj() {
        return cpfCnpj;
    }

    public void setCpfCnpj(String cpfCnpj) {
        this.cpfCnpj = cpfCnpj;
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

    public Usuario getCorretor() {
        return corretor;
    }

    public void setCorretor(Usuario corretor) {
        this.corretor = corretor;
    }
}


