package com.crm.springSecurity.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "ocupacao")
public class Ocupacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String descricao;

    @OneToMany(mappedBy = "ocupacao")
    private List<UsuarioOcupacao> usuarioOcupacoes;

    // Getters e Setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public List<UsuarioOcupacao> getUsuarioOcupacoes() { return usuarioOcupacoes; }
    public void setUsuarioOcupacoes(List<UsuarioOcupacao> usuarioOcupacoes) { this.usuarioOcupacoes = usuarioOcupacoes; }
}
