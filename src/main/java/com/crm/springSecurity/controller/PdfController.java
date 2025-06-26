package com.crm.springSecurity.controller;

import com.crm.springSecurity.service.RelatorioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.List;

@RestController
@RequestMapping("/api/relatorio")
public class PdfController {

    @Autowired
    private RelatorioService relatorioService;

    @GetMapping(value = "/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> gerarPdf(@RequestParam String texto) {
        LinkedHashMap<String, List<String>> dados = new LinkedHashMap<>();
        dados.put("Nome", Arrays.asList("João", "Maria", "Ana"));
        dados.put("QTD", Arrays.asList("10", "20", "30"));
        String brasao = "";
        byte[] data = Base64.getDecoder().decode(brasao);

        byte[] pdfBytes = relatorioService.gerarRelatorioPdf(texto, data, dados );

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=relatorio.pdf");


        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }
}

