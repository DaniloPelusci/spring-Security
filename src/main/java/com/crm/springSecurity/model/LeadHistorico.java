package com.crm.springSecurity.model;


import com.crm.springSecurity.alth.modelSecurity.User;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "lead_historico")
public class LeadHistorico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "lead_id")
    private Lead lead;

    private LocalDateTime dataModificacao;
    private String acao;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Getters e Setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Lead getLead() { return lead; }
    public void setLead(Lead lead) { this.lead = lead; }

    public LocalDateTime getDataModificacao() { return dataModificacao; }
    public void setDataModificacao(LocalDateTime dataModificacao) { this.dataModificacao = dataModificacao; }

    public String getAcao() { return acao; }
    public void setAcao(String acao) { this.acao = acao; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}

