package com.crm.springSecurity.service;

import com.crm.springSecurity.model.FotoInspection;
import com.crm.springSecurity.repository.FotoInspectionRepository;
import com.crm.springSecurity.repository.InspectionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FotoInspectionServiceTest {

    @Test
    void deveListarPorInspectionIdQuandoFiltroInformado() {
        FotoInspectionRepository fotoRepository = mock(FotoInspectionRepository.class);
        InspectionRepository inspectionRepository = mock(InspectionRepository.class);
        FotoInspectionService service = new FotoInspectionService(fotoRepository, inspectionRepository);

        when(fotoRepository.findByInspectionId(10L)).thenReturn(List.of(new FotoInspection()));

        List<FotoInspection> resultado = service.listar(10L);

        assertEquals(1, resultado.size());
        verify(fotoRepository).findByInspectionId(10L);
        verify(fotoRepository, never()).findAll();
    }

    @Test
    void deveCriarFotoQuandoInspectionIdValido() {
        FotoInspectionRepository fotoRepository = mock(FotoInspectionRepository.class);
        InspectionRepository inspectionRepository = mock(InspectionRepository.class);
        FotoInspectionService service = new FotoInspectionService(fotoRepository, inspectionRepository);

        FotoInspection foto = new FotoInspection();
        foto.setId(5L);
        foto.setInspectionId(1L);
        foto.setFoto(new byte[]{1, 2, 3});

        when(inspectionRepository.existsById(1L)).thenReturn(true);
        when(fotoRepository.save(any(FotoInspection.class))).thenAnswer(invocation -> invocation.getArgument(0));

        FotoInspection salvo = service.criar(foto);

        assertNull(salvo.getId());
        verify(fotoRepository).save(foto);
    }

    @Test
    void deveFalharQuandoInspectionIdInvalido() {
        FotoInspectionRepository fotoRepository = mock(FotoInspectionRepository.class);
        InspectionRepository inspectionRepository = mock(InspectionRepository.class);
        FotoInspectionService service = new FotoInspectionService(fotoRepository, inspectionRepository);

        FotoInspection foto = new FotoInspection();
        foto.setInspectionId(999L);
        foto.setFoto(new byte[]{1});

        when(inspectionRepository.existsById(999L)).thenReturn(false);

        assertThrows(ResponseStatusException.class, () -> service.criar(foto));
        verify(fotoRepository, never()).save(any());
    }
}
