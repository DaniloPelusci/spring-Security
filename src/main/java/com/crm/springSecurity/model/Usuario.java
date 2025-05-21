package com.crm.springSecurity.model;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    private Long id;

    private String nome;

    private String telefone;

    private String email;

    @ManyToMany
    @JoinTable(
            name = "usuarioOcupacao",
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "ocupacao_id")
    )
    private Set<Ocupacao> ocupacoes;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Ocupacao> getOcupacoes() {
        return ocupacoes;
    }

    public void setOcupacoes(Set<Ocupacao> ocupacoes) {
        this.ocupacoes = ocupacoes;
    }
}

