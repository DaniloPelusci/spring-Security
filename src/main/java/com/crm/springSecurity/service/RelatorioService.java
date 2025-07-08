package com.crm.springSecurity.service;


import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RelatorioService {

    public byte[] gerarRelatorioPdf(String texto, byte[] brasaoBytes, Map<String, List<String>> dadosIniciais) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Cabeçalho com brasão (se necessário)
            pdf.addEventHandler(PdfDocumentEvent.START_PAGE, new BrasaoHeaderHandler(brasaoBytes));
            document.setMargins(90, 36, 36, 36);

            // Título principal (usando o texto passado)
            document.add(new Paragraph("Relatorio completo de ferramentas de IA")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setBold()
                    .setFontSize(14)
                    .setMarginTop(40)
            );

            // Adiciona a primeira tabela recebida por parâmetro (se houver)

            if (dadosIniciais != null && !dadosIniciais.isEmpty()) {
                Table tabelaInicial = getTable(dadosIniciais);
                document.add(tabelaInicial);
            }

            // Tabela de Ferramentas Usadas
            LinkedHashMap<String, List<String>> dadosFerramentas = new LinkedHashMap<>();
            dadosFerramentas = gerarTabelaFerramentasQtdUsadas(dadosFerramentas);
            Table tabelaFerramentasQtdUsadas = getTable(dadosFerramentas);
            document.add(new Paragraph("Ferramentas Usadas")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setBold()
                    .setFontSize(14)
                    .setMarginTop(40));
            document.add(tabelaFerramentasQtdUsadas);

            // Tabela de Valor Médio Gasto
            LinkedHashMap<String, List<String>> dadosValor = new LinkedHashMap<>();
            dadosValor = gerarTabelaValorMedioGasto(dadosValor);
            Table tabelaValorMedioGasto = getTable(dadosValor);
            document.add(new Paragraph("Valor Médio Gasto por Servidor")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setBold()
                    .setFontSize(14)
                    .setMarginTop(40));
            document.add(tabelaValorMedioGasto);

            // Tabela de Servidores com Treinamento
            LinkedHashMap<String, List<String>> dadosTreinamento = new LinkedHashMap<>();
            dadosTreinamento = gerarTabelaQtdServidoresComTreinamento(dadosTreinamento);
            Table tabelaServidoresComTreinamento = getTable(dadosTreinamento);
            document.add(new Paragraph("Servidores com Treinamento em Ferramentas de IA")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setBold()
                    .setFontSize(14)
                    .setMarginTop(40));
            document.add(tabelaServidoresComTreinamento);

            // Se quiser adicionar uma conclusão, descrição ou texto final:
            document.add(new Paragraph(texto)
                    .setTextAlignment(TextAlignment.LEFT)
                    .setFontSize(12)
                    .setMarginTop(40)
            );

            document.close();
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar PDF", e);
        }
    }

    private LinkedHashMap<String, List<String>> gerarTabelaFerramentasQtdUsadas(LinkedHashMap<String, List<String>> dados) {
        // Simulando que foram usadas 7 ferramentas diferentes
        dados.put("QTD Ferramentas Usadas", Collections.singletonList("7"));
        return dados;
    }
    private LinkedHashMap<String, List<String>> gerarTabelaValorMedioGasto(LinkedHashMap<String, List<String>> dados) {
        // Simulação de lista de servidores
        List<String> nomes = Arrays.asList("gpt", "depseeak", "gemini");
        List<String> qtds = Arrays.asList("3", "5", "2");
        List<String> valores = Arrays.asList("1200.50", "2100.75", "950.00");

        dados.put("Nome", nomes);
        dados.put("QTD", qtds);
        dados.put("Valor Total", valores);
        return dados;
    }
    private LinkedHashMap<String, List<String>> gerarTabelaQtdServidoresComTreinamento(LinkedHashMap<String, List<String>> dados) {
        // Simulando 12 servidores com treinamento
        dados.put("QTD Servidores com Treinamento", Collections.singletonList("12"));
        return dados;
    }





    private static Table getTable(Map<String, List<String>> dados) {
        // Quantidade de colunas conforme cabeçalho
        int colunas = dados.size();
        Table table = new Table(colunas);

        // Cabeçalho
        for (String coluna : dados.keySet()) {
            Cell cellHeader = new Cell().add(new Paragraph(coluna));
            table.addHeaderCell(cellHeader);
        }

        // Descobre quantidade de linhas pela primeira coluna
        int linhas = dados.values().iterator().next().size();

        // Corpo da tabela
        for (int i = 0; i < linhas; i++) {
            for (List<String> listaColuna : dados.values()) {
                String valor = (i < listaColuna.size()) ? listaColuna.get(i) : "";
                Cell cell = new Cell().add(new Paragraph(valor));
                table.addCell(cell);
            }
        }

        table.setBorder(Border.NO_BORDER);
        return table;
    }


}
