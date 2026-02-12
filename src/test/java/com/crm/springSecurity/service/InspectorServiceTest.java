package com.crm.springSecurity.service;

import com.crm.springSecurity.model.Inspector;
import com.crm.springSecurity.repository.InspectorRepository;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InspectorServiceTest {

    @Test
    void deveCriarInspectorZerandoId() {
        InspectorRepository repository = mock(InspectorRepository.class);
        InspectorService service = new InspectorService(repository);

        Inspector inspector = new Inspector();
        inspector.setId(99L);
        inspector.setNome("Carlos");

        when(repository.save(any(Inspector.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Inspector salvo = service.criar(inspector);

        assertNull(salvo.getId());
        assertEquals("Carlos", salvo.getNome());
        verify(repository).save(inspector);
    }

    @Test
    void deveLancarNotFoundQuandoInspectorNaoExiste() {
        InspectorRepository repository = mock(InspectorRepository.class);
        InspectorService service = new InspectorService(repository);

        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> service.buscarPorId(1L));
    }
}
