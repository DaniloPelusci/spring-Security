package com.crm.springSecurity.modelSecurity;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;


@Entity
@Table(name = "permission")
public class Permission implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    @Override
    public String getAuthority() {
        return description;
    }
}

