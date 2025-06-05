// controller/LeadPublicController.java

package com.crm.springSecurity.controller;

import com.crm.springSecurity.model.Lead;
import com.crm.springSecurity.model.DocumentosLead;
import com.crm.springSecurity.repository.LeadRepository;
import com.crm.springSecurity.repository.DocumentosLeadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Optional;

@RestController
@RequestMapping("/api/public/leads")
public class LeadPublicController {

    @Autowired
    private LeadRepository leadRepository;

    @Autowired
    private DocumentosLeadRepository documentosLeadRepository;

    @PostMapping("/upload-documento/{codigoUpload}")
    public ResponseEntity<String> uploadDocumento(
            @PathVariable String codigoUpload,
            @RequestParam("file") MultipartFile file) {
        Optional<Lead> leadOpt = leadRepository.findByCodigoUpload(codigoUpload);
        if (leadOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Código de upload inválido ou expirado.");
        }
        Lead lead = leadOpt.get();

        try {
            DocumentosLead doc = new DocumentosLead();
            doc.setNomeArquivo(file.getOriginalFilename());
            doc.setTipoArquivo(file.getContentType());
            doc.setDataUpload(LocalDate.now());
            doc.setConteudo(file.getBytes());
            doc.setLead(lead);
            doc.setTipoDocumento(null); // Vai ser classificado depois
            doc.setDataEmissao(null);
            documentosLeadRepository.save(doc);
            return ResponseEntity.ok("Documento recebido. Aguardando análise do corretor.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao salvar documento.");
        }
    }
}
