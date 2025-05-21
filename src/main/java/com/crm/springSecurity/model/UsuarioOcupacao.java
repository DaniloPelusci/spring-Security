package com.crm.springSecurity.model;



import com.crm.springSecurity.alth.modelSecurity.User;
import jakarta.persistence.*;

@Entity
@Table(
        name = "usuario_ocupacao",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "ocupacao_id"})
)
public class UsuarioOcupacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "ocupacao_id")
    private Ocupacao ocupacao;

    // Getters e Setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Ocupacao getOcupacao() { return ocupacao; }
    public void setOcupacao(Ocupacao ocupacao) { this.ocupacao = ocupacao; }
}

