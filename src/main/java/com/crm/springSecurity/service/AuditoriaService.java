package com.crm.springSecurity.service;

import com.crm.springSecurity.model.DocumentosLead;
import com.crm.springSecurity.model.Lead;
import com.crm.springSecurity.model.LeadHistorico;
import com.crm.springSecurity.repository.DocumentosLeadRepository;
import com.crm.springSecurity.repository.LeadHistoricoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.crm.springSecurity.alth.modelSecurity.User;


import java.time.LocalDateTime;

@Service
public class AuditoriaService {
    @Autowired
    private LeadHistoricoRepository leadHistoricoRepository;

    /**
     * Registra no histórico uma ação executada em um lead.
     *
     * @param user  usuário que realizou a ação
     * @param lead  lead envolvido
     * @param acao  descrição da ação
     */
    public void registrarAcao(User user, Lead lead, String acao) {
        LeadHistorico historico = new LeadHistorico();
        historico.setUser(user);
        historico.setLead(lead);
        historico.setDataModificacao(LocalDateTime.now());
        historico.setAcao(acao);
        leadHistoricoRepository.save(historico);
    }

    public void registrarDownload(User user, DocumentosLead documento) {
        // Exemplo simples: só loga no console
        System.out.printf("Usuário %s (id: %d) baixou o documento %s (id: %d) do lead id: %d em %s\n",
                user.getUsername(), user.getId(),
                documento.getNomeArquivo(), documento.getId(),
                documento.getLead().getId(),
                LocalDateTime.now());

        LeadHistorico historico = new LeadHistorico();
        historico.setUser(user);
        historico.setDataModificacao(LocalDateTime.now());
        historico.setLead(documento.getLead());
        historico.setAcao("Download tipo de documento: "+documento.getTipoDocumento());

        leadHistoricoRepository.save(historico);
    }
}
