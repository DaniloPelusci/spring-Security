package com.crm.zipupload.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.crm.zipupload.dto.ZipUploadResultDto;
import com.crm.zipupload.entity.Inspection;
import com.crm.zipupload.entity.InspectionPhoto;
import com.crm.zipupload.entity.Inspector;
import com.crm.zipupload.repository.InspectionPhotoRepository;
import com.crm.zipupload.repository.InspectionRepository;
import com.crm.zipupload.repository.InspectorRepository;

class ZipInspectionUploadServiceTest {

    private InspectorRepository inspectorRepository;
    private InspectionRepository inspectionRepository;
    private InspectionPhotoRepository inspectionPhotoRepository;
    private ZipInspectionUploadService service;

    @BeforeEach
    void setup() {
        inspectorRepository = Mockito.mock(InspectorRepository.class);
        inspectionRepository = Mockito.mock(InspectionRepository.class);
        inspectionPhotoRepository = Mockito.mock(InspectionPhotoRepository.class);
        service = new ZipInspectionUploadService(inspectorRepository, inspectionRepository, inspectionPhotoRepository);
    }

    @Test
    void deveProcessarZipEGravarDescricaoComNomeDaFoto() throws IOException {
        Inspector inspector = new Inspector();
        inspector.setId(15L);

        when(inspectorRepository.findById(15L)).thenReturn(Optional.of(inspector));
        when(inspectionRepository.findByInspectorIdAndWorkOrderNumber(15L, "1001")).thenReturn(Optional.empty());
        when(inspectionRepository.findByInspectorIdAndWorkOrderNumber(15L, "1002")).thenReturn(Optional.empty());
        when(inspectionRepository.save(any(Inspection.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(inspectionPhotoRepository.save(any(InspectionPhoto.class))).thenAnswer(invocation -> invocation.getArgument(0));

        MockMultipartFile file = new MockMultipartFile("file", "15.zip", "application/zip", buildZip());

        ZipUploadResultDto result = service.upload(file);

        assertEquals(15L, result.inspectorId());
        assertEquals(2, result.totalInspections());
        assertEquals(3, result.totalPhotos());
        verify(inspectionPhotoRepository, times(3)).save(any(InspectionPhoto.class));
    }

    @Test
    void deveFalharQuandoNomeDoZipNaoForId() {
        MockMultipartFile file = new MockMultipartFile("file", "inspetorA.zip", "application/zip", new byte[0]);

        assertThrows(ResponseStatusException.class, () -> service.upload(file));
    }

    private byte[] buildZip() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ZipOutputStream zos = new ZipOutputStream(baos)) {
            addEntry(zos, "1001/foto-1.jpg", "img-1");
            addEntry(zos, "1001/foto-2.png", "img-2");
            addEntry(zos, "1002/foto-3.jpeg", "img-3");
        }
        return baos.toByteArray();
    }

    private void addEntry(ZipOutputStream zos, String name, String content) throws IOException {
        ZipEntry entry = new ZipEntry(name);
        zos.putNextEntry(entry);
        zos.write(content.getBytes());
        zos.closeEntry();
    }
}
