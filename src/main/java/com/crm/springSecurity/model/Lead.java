package com.crm.springSecurity.model;

import com.crm.springSecurity.alth.modelSecurity.User;
import jakarta.persistence.*;
import java.util.List;

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
    private String statusLeads; // Campo antigo
    private String observacao;

    @ManyToOne
    @JoinColumn(name = "corretor_id")
    private User corretor;

    @ManyToOne
    @JoinColumn(name = "correspondente_id")
    private User correspondente;

    @Column(name = "status_lead")
    private String statusLead;

    @Column(unique = true)
    private String codigoUpload;

    // Getters e Setters

    public String getCodigoUpload() {
        return codigoUpload;
    }

    public void setCodigoUpload(String codigoUpload) {
            this.codigoUpload = codigoUpload;
    }

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

    public User getCorrespondente() { return correspondente; }
    public void setCorrespondente(User correspondente) { this.correspondente = correspondente; }

    public String getStatusLead() { return statusLead; }
    public void setStatusLead(String statusLead) { this.statusLead = statusLead; }

    // Construtores, equals, hashCode, etc. conforme padrão do projeto.
}
