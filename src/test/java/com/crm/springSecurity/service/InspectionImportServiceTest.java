package com.crm.springSecurity.service;

import com.crm.springSecurity.model.Inspection;
import com.crm.springSecurity.model.dto.InspectionImportResponseDTO;
import com.crm.springSecurity.repository.InspectionRepository;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.mock.web.MockMultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

class InspectionImportServiceTest {

    @Test
    void deveImportarLinhasDaPlanilhaComMapeamentoCorreto() throws IOException {
        InspectionRepository repository = mock(InspectionRepository.class);
        when(repository.saveAll(anyList())).thenAnswer(invocation -> invocation.getArgument(0));

        InspectionImportService service = new InspectionImportService(repository);

        MockMultipartFile arquivo = new MockMultipartFile(
                "file",
                "inspections.xlsx",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                criarPlanilhaTeste()
        );

        InspectionImportResponseDTO response = service.importar(arquivo, 10L);

        assertEquals(2, response.getTotalLinhas());
        assertEquals(2, response.getImportadas());

        ArgumentCaptor<List<Inspection>> captor = ArgumentCaptor.forClass(List.class);
        verify(repository).saveAll(captor.capture());

        List<Inspection> salvas = captor.getValue();
        assertEquals(2, salvas.size());

        Inspection primeira = salvas.getFirst();
        assertEquals("Assigned", primeira.getStatus());
        assertEquals("352640516", primeira.getWorder());
        assertEquals("TDW9FI", primeira.getInspector());
        assertEquals("WEL936", primeira.getClient());
        assertEquals("2454 160TH AVE NE", primeira.getAddress1());
        assertEquals("BELLEVUE", primeira.getCity());
        assertEquals("98008", primeira.getZip());
        assertEquals("01/02/2026", primeira.getDuedate());
        assertEquals("N", primeira.getRush());
        assertEquals(10L, primeira.getInspetorId());

        Inspection segunda = salvas.get(1);
        assertEquals("Y", segunda.getFreezeFlag());
        assertEquals("Y", segunda.getNaturalFlag());
        assertEquals("Y", segunda.getPump());
    }

    private byte[] criarPlanilhaTeste() throws IOException {
        try (XSSFWorkbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("inspections");
            Row header = sheet.createRow(0);
            String[] colunas = {
                    "STATUS", "WORDER", "INSPECTOR", "CLIENT", "NAME", "ADDRESS1", "ADDRESS2", "CITY", "ZIP", "OTYPE",
                    "DUEDATE", "WINDOW", "START DATE", "NEGLECT", "RUSH", "FOLLOWUP", "VACANT", "MORTGAGE", "VANDALISM",
                    "FREEZE", "STORM", "ROOF", "WATER", "NATURAL", "FIRE", "HAZARD", "STRUCTURE", "MOLD", "PUMP"
            };

            for (int i = 0; i < colunas.length; i++) {
                header.createCell(i).setCellValue(colunas[i]);
            }

            Row linha1 = sheet.createRow(1);
            linha1.createCell(0).setCellValue("Assigned");
            linha1.createCell(1).setCellValue("352640516");
            linha1.createCell(2).setCellValue("TDW9FI");
            linha1.createCell(3).setCellValue("WEL936");
            linha1.createCell(4).setCellValue("ANDREOTPI -PROJECT MANAGER, JOE");
            linha1.createCell(5).setCellValue("2454 160TH AVE NE");
            linha1.createCell(7).setCellValue("BELLEVUE");
            linha1.createCell(8).setCellValue("98008");
            linha1.createCell(10).setCellValue("01/02/2026");
            linha1.createCell(14).setCellValue("N");

            Row linha2 = sheet.createRow(2);
            linha2.createCell(0).setCellValue("Assigned");
            linha2.createCell(1).setCellValue("352490004");
            linha2.createCell(19).setCellValue("Y");
            linha2.createCell(23).setCellValue("Y");
            linha2.createCell(28).setCellValue("Y");

            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }
}
