package com.crm.springSecurity.service;

import com.crm.springSecurity.alth.modelSecurity.User;
import com.crm.springSecurity.alth.repository.UserRepository;
import com.crm.springSecurity.model.Lead;
import com.crm.springSecurity.model.dto.LeadCadastroDTO;
import com.crm.springSecurity.repository.LeadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;

@Service
public class LeadService {

    @Autowired
    private LeadRepository leadRepository;

    @Autowired
    private UserRepository userRepository;

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
}
