package com.crm.springSecurity.service;

import com.crm.springSecurity.model.DocumentosLead;
import com.crm.springSecurity.model.Lead;
import com.crm.springSecurity.model.TipoDocumento;
import com.crm.springSecurity.repository.DocumentosLeadRepository;
import com.crm.springSecurity.repository.LeadRepository;
import com.crm.springSecurity.repository.TipoDocumentoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class DocumentosLeadService {
    @Autowired
    private DocumentosLeadRepository repo;

    @Autowired
    private TipoDocumentoRepository tipoDocumentoRepository;
    @Autowired
    private LeadRepository leadRepository;
    @Autowired
    private DocumentosLeadRepository documentosLeadRepository;

    public DocumentosLead salvar(DocumentosLead doc) {
        return repo.save(doc);
    }

    public List<DocumentosLead> listarPorLead(Long leadId) {
        return repo.findByLeadId(leadId);
    }

    public Optional<DocumentosLead> buscarPorId(Long id) {
        return repo.findById(id);
    }

    public void deletar(Long id) {
        repo.deleteById(id);
    }


    @Transactional
    public void salvarArquivo(Long leadId, List<MultipartFile> arquivos, List<Long> tiposDocumentoId) {
        // Verifique se o lead existe, se quiser
        Lead lead = leadRepository.findById(leadId).orElseThrow(() -> new RuntimeException("Lead não encontrado"));

        for (int i = 0; i < arquivos.size(); i++) {
            MultipartFile arquivo = arquivos.get(i);
            Long tipoId = tiposDocumentoId.get(i);
            try {
                String nomeArquivo = arquivo.getOriginalFilename();
                String tipoArquivo = arquivo.getContentType();
                TipoDocumento tipoDocumento = tipoDocumentoRepository.getReferenceById(tipoId);

                DocumentosLead doc = new DocumentosLead();
                doc.setNomeArquivo(nomeArquivo);
                doc.setTipoArquivo(tipoArquivo);
                doc.setDataUpload(LocalDate.now());
                doc.setConteudo(arquivo.getBytes());
                doc.setLead(lead);
                doc.setTipoDocumento(tipoDocumento);
                doc.setDataEmissao(LocalDate.now());

                documentosLeadRepository.save(doc);

            } catch (IOException e) {
                throw new RuntimeException("Erro ao processar arquivo: " + arquivo.getOriginalFilename(), e);
            }
        }
    }


}

