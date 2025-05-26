package com.crm.springSecurity.model.dto;

public class LeadFiltroDTO {
    private String nome;
    private String status;
    private Long corretorId;
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Long getCorretorId() {
		return corretorId;
	}
	public void setCorretorId(Long corretorId) {
		this.corretorId = corretorId;
	}
    
}
