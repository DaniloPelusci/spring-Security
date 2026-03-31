package com.crm.springSecurity.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "pos_venda")
public class PosVenda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    private String notas;
    private LocalDate dataContato;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public LocalDate getDataContato() {
        return dataContato;
    }

    public void setDataContato(LocalDate dataContato) {
        this.dataContato = dataContato;
    }
}
