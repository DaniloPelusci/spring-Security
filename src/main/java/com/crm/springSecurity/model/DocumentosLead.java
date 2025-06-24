package com.crm.springSecurity.model;

import com.crm.springSecurity.model.dto.DocumentoLeadDTO;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "documentos_lead")
public class DocumentosLead {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomeArquivo;
    private String tipoArquivo;
    private LocalDate dataUpload;


    @Column(name = "conteudo")
    private byte[] conteudo; // CORRETO para BYTEA!


    @ManyToOne
    @JoinColumn(name = "lead_id")
    private Lead lead;

    @ManyToOne
    @JoinColumn(name = "tipo_documento_id")
    private TipoDocumento tipoDocumento;

    private LocalDate dataEmissao;

    // Getters e Setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNomeArquivo() { return nomeArquivo; }
    public void setNomeArquivo(String nomeArquivo) { this.nomeArquivo = nomeArquivo; }

    public String getTipoArquivo() { return tipoArquivo; }
    public void setTipoArquivo(String tipoArquivo) { this.tipoArquivo = tipoArquivo; }

    public LocalDate getDataUpload() { return dataUpload; }
    public void setDataUpload(LocalDate dataUpload) { this.dataUpload = dataUpload; }

    public byte[] getConteudo() { return conteudo; }
    public void setConteudo(byte[] conteudo) { this.conteudo = conteudo; }

    public Lead getLead() { return lead; }
    public void setLead(Lead lead) { this.lead = lead; }

    public TipoDocumento getTipoDocumento() { return tipoDocumento; }
    public void setTipoDocumento(TipoDocumento tipoDocumento) { this.tipoDocumento = tipoDocumento; }

    public LocalDate getDataEmissao() { return dataEmissao; }
    public void setDataEmissao(LocalDate dataEmissao) { this.dataEmissao = dataEmissao; }


    public DocumentoLeadDTO fromEntity(DocumentosLead doc) {
        DocumentoLeadDTO dto = new DocumentoLeadDTO();
        dto.setId(doc.getId());
        dto.setNomeArquivo(doc.getNomeArquivo());
        dto.setTipoArquivo(doc.getTipoArquivo());
        dto.setDataUpload(doc.getDataUpload());
        dto.setTipoDocumento(doc.getTipoDocumento().getDescricao());
        dto.setDataEmissao(doc.getDataEmissao());
        return dto;
    }

}
