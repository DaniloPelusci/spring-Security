package com.crm.zipupload.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.crm.zipupload.dto.ZipUploadResultDto;
import com.crm.zipupload.entity.Inspection;
import com.crm.zipupload.entity.InspectionPhoto;
import com.crm.zipupload.entity.Inspector;
import com.crm.zipupload.repository.InspectionPhotoRepository;
import com.crm.zipupload.repository.InspectionRepository;
import com.crm.zipupload.repository.InspectorRepository;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class ZipInspectionUploadService {

    private final InspectorRepository inspectorRepository;
    private final InspectionRepository inspectionRepository;
    private final InspectionPhotoRepository inspectionPhotoRepository;

    public ZipInspectionUploadService(
            InspectorRepository inspectorRepository,
            InspectionRepository inspectionRepository,
            InspectionPhotoRepository inspectionPhotoRepository) {
        this.inspectorRepository = inspectorRepository;
        this.inspectionRepository = inspectionRepository;
        this.inspectionPhotoRepository = inspectionPhotoRepository;
    }

    @Transactional
    public ZipUploadResultDto upload(MultipartFile zipFile) {
        if (zipFile == null || zipFile.isEmpty()) {
            throw new ResponseStatusException(BAD_REQUEST, "Arquivo ZIP é obrigatório.");
        }

        Long inspectorId = extractInspectorId(zipFile.getOriginalFilename());
        Inspector inspector = inspectorRepository.findById(inspectorId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND,
                        "Inspetor não encontrado para o ID " + inspectorId));

        int savedPhotos = 0;
        Set<String> workOrders = new HashSet<>();
        Map<String, Inspection> inspectionCache = new HashMap<>();

        try (ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(zipFile.getBytes()))) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (entry.isDirectory()) {
                    zis.closeEntry();
                    continue;
                }

                String normalizedPath = entry.getName().replace("\\", "/");
                String[] parts = normalizedPath.split("/");
                if (parts.length < 2) {
                    zis.closeEntry();
                    continue;
                }

                String workOrderNumber = parts[0].trim();
                String fileName = Paths.get(parts[parts.length - 1]).getFileName().toString();
                if (workOrderNumber.isEmpty() || fileName.isEmpty()) {
                    zis.closeEntry();
                    continue;
                }

                Inspection inspection = findOrCreateInspection(inspector, workOrderNumber, inspectionCache);

                InspectionPhoto photo = new InspectionPhoto();
                photo.setInspection(inspection);
                photo.setFileName(fileName);
                photo.setDescription(fileName);
                photo.setData(zis.readAllBytes());
                photo.setContentType(detectContentType(fileName));

                inspectionPhotoRepository.save(photo);
                savedPhotos++;
                workOrders.add(workOrderNumber);

                zis.closeEntry();
            }
        } catch (IOException e) {
            throw new ResponseStatusException(BAD_REQUEST, "Erro ao processar ZIP: " + e.getMessage(), e);
        }

        if (savedPhotos == 0) {
            throw new ResponseStatusException(BAD_REQUEST,
                    "Nenhuma foto válida encontrada. O ZIP deve conter pastas por número de work order com fotos dentro.");
        }

        return new ZipUploadResultDto(inspectorId, savedPhotos, workOrders.size());
    }

    private Inspection findOrCreateInspection(Inspector inspector, String workOrderNumber,
            Map<String, Inspection> inspectionCache) {
        return inspectionCache.computeIfAbsent(workOrderNumber, key -> inspectionRepository
                .findByInspectorIdAndWorkOrderNumber(inspector.getId(), key)
                .orElseGet(() -> {
                    Inspection inspection = new Inspection();
                    inspection.setInspector(inspector);
                    inspection.setWorkOrderNumber(key);
                    return inspectionRepository.save(inspection);
                }));
    }

    private Long extractInspectorId(String originalFilename) {
        if (originalFilename == null || originalFilename.isBlank()) {
            throw new ResponseStatusException(BAD_REQUEST,
                    "Nome do arquivo é obrigatório e deve conter o ID do inspetor (ex: 15.zip).");
        }

        String lower = originalFilename.toLowerCase(Locale.ROOT);
        if (!lower.endsWith(".zip")) {
            throw new ResponseStatusException(BAD_REQUEST, "Arquivo precisa ter extensão .zip");
        }

        String baseName = originalFilename.substring(0, originalFilename.length() - 4).trim();
        try {
            return Long.parseLong(baseName);
        } catch (NumberFormatException ex) {
            throw new ResponseStatusException(BAD_REQUEST,
                    "Nome do ZIP deve ser o ID do inspetor (ex: 15.zip).", ex);
        }
    }

    private String detectContentType(String fileName) {
        String lower = fileName.toLowerCase(Locale.ROOT);
        if (lower.endsWith(".jpg") || lower.endsWith(".jpeg")) {
            return "image/jpeg";
        }
        if (lower.endsWith(".png")) {
            return "image/png";
        }
        if (lower.endsWith(".webp")) {
            return "image/webp";
        }
        return "application/octet-stream";
    }
}
