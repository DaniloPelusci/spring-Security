package com.crm.springSecurity.service;

import com.crm.springSecurity.model.Inspection;
import com.crm.springSecurity.repository.InspectionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InspectionServiceTest {

    @Test
    void deveListarTodasQuandoInspetorIdNaoForInformado() {
        InspectionRepository repository = mock(InspectionRepository.class);
        InspectionService service = new InspectionService(repository);

        when(repository.findAll()).thenReturn(List.of(new Inspection(), new Inspection()));

        List<Inspection> resultado = service.listar(null);

        assertEquals(2, resultado.size());
        verify(repository).findAll();
        verify(repository, never()).findByInspetorId(anyLong());
    }

    @Test
    void deveFiltrarPorInspetorQuandoInspetorIdForInformado() {
        InspectionRepository repository = mock(InspectionRepository.class);
        InspectionService service = new InspectionService(repository);

        when(repository.findByInspetorId(42L)).thenReturn(List.of(new Inspection()));

        List<Inspection> resultado = service.listar(42L);

        assertEquals(1, resultado.size());
        verify(repository).findByInspetorId(42L);
        verify(repository, never()).findAll();
    }

    @Test
    void deveCriarInspecaoComIdNulo() {
        InspectionRepository repository = mock(InspectionRepository.class);
        InspectionService service = new InspectionService(repository);

        Inspection inspection = new Inspection();
        inspection.setId(12L);

        when(repository.save(any(Inspection.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Inspection criada = service.criar(inspection);

        assertNull(criada.getId());
        verify(repository).save(inspection);
    }

    @Test
    void deveAtualizarInspecaoExistente() {
        InspectionRepository repository = mock(InspectionRepository.class);
        InspectionService service = new InspectionService(repository);

        Inspection existente = new Inspection();
        existente.setId(5L);

        Inspection payload = new Inspection();
        payload.setStatus("DONE");

        when(repository.findById(5L)).thenReturn(Optional.of(existente));
        when(repository.save(any(Inspection.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Inspection atualizada = service.atualizar(5L, payload);

        assertEquals(5L, atualizada.getId());
        assertEquals("DONE", atualizada.getStatus());
    }

    @Test
    void deveLancarNotFoundAoBuscarInspecaoInexistente() {
        InspectionRepository repository = mock(InspectionRepository.class);
        InspectionService service = new InspectionService(repository);

        when(repository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> service.buscarPorId(999L));
    }
}
