package com.crm.springSecurity.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import com.crm.springSecurity.repository.DocumentosLeadRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.crm.springSecurity.alth.modelSecurity.User;
import com.crm.springSecurity.alth.repository.UserRepository;
import com.crm.springSecurity.model.Lead;
import com.crm.springSecurity.model.dto.LeadCadastroDTO;
import com.crm.springSecurity.model.dto.LeadFiltroDTO;
import com.crm.springSecurity.repository.LeadRepository;
import com.crm.springSecurity.specification.LeadSpecification;
import com.crm.springSecurity.model.DocumentosLead;
import com.crm.springSecurity.model.TipoDocumento;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.web.multipart.MultipartFile;

@Service
public class LeadService {

    @Autowired
    private LeadRepository leadRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DocumentosLeadRepository documentosLeadRepository;

    public Lead cadastrarLead(LeadCadastroDTO leadDTO, Authentication authentication) {
        String username = authentication.getName();
        User usuarioLogado = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        boolean isAdmin = usuarioLogado.getAuthorities().stream()
                .anyMatch(p -> p.getAuthority().equalsIgnoreCase("admin"));

        boolean isGestorLeads = usuarioLogado.getAuthorities().stream()
                .anyMatch(p -> p.getAuthority().equalsIgnoreCase("gestor_Leads"));

        boolean isCorretor = usuarioLogado.getAuthorities().stream()
                .anyMatch(p -> p.getAuthority().equalsIgnoreCase("corretor"));

        Long corretorIdParaAtribuir = null;

        if (leadDTO.getCorretorId() != null) {
            if (isGestorLeads || isAdmin) {
                corretorIdParaAtribuir = leadDTO.getCorretorId();
            } else if (isCorretor && leadDTO.getCorretorId().equals(usuarioLogado.getId())) {
                corretorIdParaAtribuir = usuarioLogado.getId();
            } else {
                throw new AccessDeniedException("Você não tem permissão para atribuir este corretor.");
            }
        } else {
            if (isCorretor) {
                corretorIdParaAtribuir = usuarioLogado.getId();
            }
        }

        Lead lead = new Lead();
        lead.setNome(leadDTO.getNome());
        lead.setTelefone(leadDTO.getTelefone());
        lead.setOrigem(leadDTO.getOrigem());
        lead.setStatusLeads("Novo");
        if (corretorIdParaAtribuir != null) {
            User corretor = userRepository.findById(corretorIdParaAtribuir)
                    .orElseThrow(() -> new EntityNotFoundException("Corretor não encontrado"));
            lead.setCorretor(corretor);
        }

        return leadRepository.save(lead);
    }
    
    public List<Lead> listarTodos() {
        String username = getUsuarioLogado();
        User user = userRepository.findByUsername(username)
             .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        // Exemplo: só ADMIN vê tudo
        boolean isAdmin = user.getAuthorities().stream()
                              .anyMatch(role -> role.getAuthority().equalsIgnoreCase("ADMIN"));

        if (isAdmin) {
            return leadRepository.findAll();
        } else {
            // Corretor ou outro perfil só vê seus leads
            return leadRepository.findByCorretorId(user.getId());
        }
    }

    public List<Lead> buscarPorCorretor(Long corretorId) {
        String username = getUsuarioLogado();
        User user = userRepository.findByUsername(username)
             .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        boolean isAdmin = user.getAuthorities().stream()
                              .anyMatch(role -> role.getAuthority().equalsIgnoreCase("ADMIN"));

        if (isAdmin || user.getId().equals(corretorId)) {
            return leadRepository.findByCorretorId(corretorId);
        } else {
            throw new AccessDeniedException("Acesso negado!");
        }
    }

    public List<Lead> buscarPorFiltro(LeadFiltroDTO filtro) {
        String username = getUsuarioLogado();
        User user = userRepository.findByUsername(username)
             .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        boolean isAdmin = user.getAuthorities().stream()
                              .anyMatch(role -> role.getAuthority().equalsIgnoreCase("ADMIN"));

        // Se não for admin, só deixa buscar leads do próprio corretor
        if (!isAdmin) {
            filtro.setCorretorId(user.getId());
        }
        return leadRepository.findAll(LeadSpecification.filtrar(filtro));
    }

    
    private String getUsuarioLogado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName(); // retorna o username
    }

    public List<Lead> listarLeadsAptosParaCorrespondente() {
        LocalDate limite = LocalDate.now().minusMonths(3);
        return leadRepository.findAll().stream()
                .filter(lead -> {
                    List<DocumentosLead> docs = documentosLeadRepository.findByLeadId(lead.getId());
                    boolean comprovanteEnderecoValido = docs.stream()
                            .anyMatch(doc -> doc.getTipoDocumento().getDescricao() == "COMPROVANTE_ENDERECO"
                                    && doc.getDataEmissao() != null
                                    && !doc.getDataEmissao().isBefore(limite));
                    // Adicione aqui a lógica para exigir outros documentos obrigatórios se desejar
                    return comprovanteEnderecoValido;
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public void aceitarLeadPorCorrespondente(Long leadId, String usernameCorrespondente) {
        Lead lead = leadRepository.findById(leadId)
                .orElseThrow(() -> new RuntimeException("Lead não encontrado"));
        User correspondente = userRepository.findByUsername(usernameCorrespondente)
                .orElseThrow(() -> new RuntimeException("Usuário correspondente não encontrado"));
        lead.setCorrespondente(correspondente);
        lead.setStatusLead("ENCAMINHADO_CORRESPONDENTE");
        leadRepository.save(lead);
    }

    public boolean salvarDocumentoPorCodigo(String codigo, MultipartFile file) {
        Lead lead = leadRepository.findByCodigoUpload(codigo);
        if (lead == null) return false;
        // Cria um novo DocumentosLead com tipoDocumento null ou "PENDENTE"
        DocumentosLead doc = new DocumentosLead();
        doc.setNomeArquivo(file.getOriginalFilename());
        doc.setTipoArquivo(file.getContentType());
        doc.setDataUpload(LocalDate.now());
        doc.setConteudo(file.getBytes());
        doc.setLead(lead);
        doc.setTipoDocumento(null); // Ainda não classificado
        doc.setDataEmissao(null);
        documentosLeadRepository.save(doc);
        return true;
    }
}
