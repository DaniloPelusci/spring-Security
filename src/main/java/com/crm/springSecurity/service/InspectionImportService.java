package com.crm.springSecurity.service;

import com.crm.springSecurity.model.Inspection;
import com.crm.springSecurity.model.dto.InspectionImportResponseDTO;
import com.crm.springSecurity.repository.InspectionRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Service
public class InspectionImportService {

    private final InspectionRepository inspectionRepository;

    public InspectionImportService(InspectionRepository inspectionRepository) {
        this.inspectionRepository = inspectionRepository;
    }

    public InspectionImportResponseDTO importar(MultipartFile arquivo, Long inspetorId) throws IOException {
        if (arquivo == null || arquivo.isEmpty()) {
            throw new IllegalArgumentException("Arquivo Excel é obrigatório.");
        }

        try (InputStream inputStream = arquivo.getInputStream();
             Workbook workbook = new XSSFWorkbook(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();
            if (!rowIterator.hasNext()) {
                return new InspectionImportResponseDTO(0, 0);
            }

            Row headerRow = rowIterator.next();
            Map<String, Integer> headers = indexarHeaders(headerRow);

            List<Inspection> inspections = new ArrayList<>();
            int totalLinhas = 0;

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                if (linhaVazia(row)) {
                    continue;
                }

                totalLinhas++;
                Inspection inspection = new Inspection();
                inspection.setInspetorId(inspetorId);

                inspection.setStatus(getValor(row, headers, "STATUS"));
                inspection.setWorder(getValor(row, headers, "WORDER"));
                inspection.setInspector(getValor(row, headers, "INSPECTOR"));
                inspection.setClient(getValor(row, headers, "CLIENT"));
                inspection.setName(getValor(row, headers, "NAME"));
                inspection.setAddress1(getValor(row, headers, "ADDRESS1"));
                inspection.setAddress2(getValor(row, headers, "ADDRESS2"));
                inspection.setCity(getValor(row, headers, "CITY"));
                inspection.setZip(getValor(row, headers, "ZIP"));
                inspection.setOtype(getValor(row, headers, "OTYPE"));
                inspection.setDuedate(getValor(row, headers, "DUEDATE"));
                inspection.setRush(getValor(row, headers, "RUSH"));
                inspection.setFollowup(getValor(row, headers, "FOLLOWUP"));
                inspection.setVacant(getValor(row, headers, "VACANT"));
                inspection.setMortgage(getValor(row, headers, "MORTGAGE"));
                inspection.setVandalism(getValor(row, headers, "VANDALISM"));
                inspection.setFreezeFlag(getValor(row, headers, "FREEZE"));
                inspection.setStorm(getValor(row, headers, "STORM"));
                inspection.setRoof(getValor(row, headers, "ROOF"));
                inspection.setWater(getValor(row, headers, "WATER"));
                inspection.setNaturalFlag(getValor(row, headers, "NATURAL"));
                inspection.setFire(getValor(row, headers, "FIRE"));
                inspection.setHazard(getValor(row, headers, "HAZARD"));
                inspection.setStructure(getValor(row, headers, "STRUCTURE"));
                inspection.setMold(getValor(row, headers, "MOLD"));
                inspection.setPump(getValor(row, headers, "PUMP"));

                inspections.add(inspection);
            }

            inspectionRepository.saveAll(inspections);
            return new InspectionImportResponseDTO(totalLinhas, inspections.size());
        }
    }

    private Map<String, Integer> indexarHeaders(Row headerRow) {
        Map<String, Integer> headers = new HashMap<>();
        DataFormatter formatter = new DataFormatter();
        for (Cell cell : headerRow) {
            String header = formatter.formatCellValue(cell);
            if (header != null && !header.isBlank()) {
                headers.put(normalizar(header), cell.getColumnIndex());
            }
        }
        return headers;
    }

    private String getValor(Row row, Map<String, Integer> headers, String coluna) {
        Integer idx = headers.get(normalizar(coluna));
        if (idx == null) {
            return null;
        }

        Cell cell = row.getCell(idx, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
        if (cell == null) {
            return null;
        }

        DataFormatter formatter = new DataFormatter();
        String valor = formatter.formatCellValue(cell);
        return valor == null || valor.isBlank() ? null : valor.trim();
    }

    private boolean linhaVazia(Row row) {
        DataFormatter formatter = new DataFormatter();
        for (int i = row.getFirstCellNum(); i < row.getLastCellNum(); i++) {
            if (i < 0) {
                continue;
            }
            Cell cell = row.getCell(i, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
            if (cell != null && !formatter.formatCellValue(cell).isBlank()) {
                return false;
            }
        }
        return true;
    }

    private String normalizar(String texto) {
        return texto == null ? "" : texto.trim().toUpperCase(Locale.ROOT);
    }
}
