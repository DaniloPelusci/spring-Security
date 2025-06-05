// controller/DocumentoAdmController.java

package com.crm.springSecurity.controller;

import com.crm.springSecurity.model.DocumentosLead;
import com.crm.springSecurity.model.TipoDocumento;
import com.crm.springSecurity.repository.DocumentosLeadRepository;
import com.crm.springSecurity.repository.TipoDocumentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/documentos")
public class DocumentoAdmController {

    @Autowired
    private DocumentosLeadRepository repo;

    @Autowired
    private TipoDocumentoRepository tipoDocRepo;

    @PutMapping("/{id}/classificar")
    public ResponseEntity<?> classificarDocumento(
            @PathVariable Long id,
            @RequestParam Long tipoDocumentoId) {
        var docOpt = repo.findById(id);
        var tipoOpt = tipoDocRepo.findById(tipoDocumentoId);
        if (docOpt.isEmpty() || tipoOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Documento ou tipo de documento não encontrado.");
        }
        DocumentosLead doc = docOpt.get();
        doc.setTipoDocumento(tipoOpt.get());
        repo.save(doc);
        return ResponseEntity.ok("Documento classificado.");
    }
}
