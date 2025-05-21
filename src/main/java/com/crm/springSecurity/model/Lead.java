package com.crm.springSecurity.model;

import com.crm.springSecurity.alth.modelSecurity.User;
import jakarta.persistence.*;

@Entity
@Table(name = "leads")
public class Lead {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String cpfCnpj;
    private String telefone;
    private String origem;

    @Column(name = "status_leads")
    private String statusLeads;

    private String observacao;

    @ManyToOne
    @JoinColumn(name = "corretor_id")
    private User corretor;

    // Getters e Setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCpfCnpj() { return cpfCnpj; }
    public void setCpfCnpj(String cpfCnpj) { this.cpfCnpj = cpfCnpj; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public String getOrigem() { return origem; }
    public void setOrigem(String origem) { this.origem = origem; }

    public String getStatusLeads() { return statusLeads; }
    public void setStatusLeads(String statusLeads) { this.statusLeads = statusLeads; }

    public String getObservacao() { return observacao; }
    public void setObservacao(String observacao) { this.observacao = observacao; }

    public User getCorretor() { return corretor; }
    public void setCorretor(User corretor) { this.corretor = corretor; }
}

