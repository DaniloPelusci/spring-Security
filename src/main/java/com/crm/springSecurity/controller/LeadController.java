package com.crm.springSecurity.controller;

import com.crm.springSecurity.model.DocumentosLead;
import com.crm.springSecurity.model.Lead;
import com.crm.springSecurity.model.TipoDocumento;
import com.crm.springSecurity.model.dto.LeadCadastroDTO;
import com.crm.springSecurity.model.dto.LeadFiltroDTO;
import com.crm.springSecurity.repository.DocumentosLeadRepository;
import com.crm.springSecurity.repository.TipoDocumentoRepository;
import com.crm.springSecurity.service.LeadService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/leads")
public class LeadController {
    @Autowired
    private DocumentosLeadRepository repo;

    @Autowired
    private LeadService leadService;

    @Autowired
    private TipoDocumentoRepository tipoDocRepo;

    @PostMapping
    public ResponseEntity<?> criarLead(@RequestBody LeadCadastroDTO leadDTO, Authentication authentication) {
        try {
            Lead lead = leadService.cadastrarLead(leadDTO, authentication);
            return ResponseEntity.ok(lead);
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }
    
    @GetMapping
    public List<Lead> listarTodos() {
        return leadService.listarTodos();
    }

    @GetMapping("/corretor/{corretorId}")
    public List<Lead> buscarPorCorretor(@PathVariable Long corretorId) {
        return leadService.buscarPorCorretor(corretorId);
    }

    @PostMapping("/filtrar")
    public List<Lead> buscarPorFiltro(@RequestBody LeadFiltroDTO filtro) {
        return leadService.buscarPorFiltro(filtro);
    }

    @PutMapping("/{id}/classificar")
    public ResponseEntity<?> classificarDocumento(
            @PathVariable Long id,
            @RequestParam Long tipoDocumentoId) {
        DocumentosLead doc = repo.findById(id).orElse(null);
        if (doc == null) return ResponseEntity.notFound().build();
        TipoDocumento tipo = tipoDocRepo.findById(tipoDocumentoId).orElse(null);
        if (tipo == null) return ResponseEntity.badRequest().body("Tipo de documento inválido.");
        doc.setTipoDocumento(tipo);
        repo.save(doc);
        return ResponseEntity.ok("Documento classificado.");
    }


    @PostMapping("/{id}/gerar-link-upload")
    // @PreAuthorize("hasRole('CORRETOR') or hasRole('ADMIN')") // descomente se já usar roles
    public String gerarLinkUpload(@PathVariable Long id) {
        String codigo = leadService.gerarCodigoUpload(id);
        String url = "https://seusistema.com/envio-documento/" + codigo;
        return url;
    }

}

