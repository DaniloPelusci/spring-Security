package com.crm.springSecurity.model.dto;

import com.crm.springSecurity.alth.modelSecurity.User;

public class UserDTO {
    private Long id;
    private String userName;
    private String nome;
    private String email;
    private String telefone;

    // construtores, getters, setters...
    public UserDTO(Long id, String userName, String nome, String email, String telefone) {
        this.id = id;
        this.userName = userName;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
    }

    // Construtor padrão (necessário para alguns frameworks, opcional para JPQL)
    public UserDTO() {}


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public static UserDTO fromEntity(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUserName(user.getUsername());
        dto.setNome(user.getNome());
        dto.setEmail(user.getEmail());
        dto.setTelefone(user.getTelefone());
        return dto;
    }
}

