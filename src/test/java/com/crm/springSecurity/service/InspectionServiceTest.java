package com.crm.springSecurity.service;

import com.crm.springSecurity.model.Inspection;
import com.crm.springSecurity.repository.InspectionRepository;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
}
