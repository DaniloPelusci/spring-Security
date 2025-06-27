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
import java.util.List;
import java.util.Map;

@Service
public class RelatorioService {

    public byte[] gerarRelatorioPdf(String texto, byte[] brasaoBytes, Map<String , List<String>> dados) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // >>> Adiciona o handler de cabeçalho para repetir o brasão em todas as páginas
            pdf.addEventHandler(PdfDocumentEvent.START_PAGE, new BrasaoHeaderHandler(brasaoBytes));

            document.add(new Paragraph("RELATÓRIO de relato que foi relatado")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setBold()
                    .setFontSize(14)
                    .setMarginTop(40) // valor em pontos, ajuste conforme necessário
            );

            //---tabela separada para poder customizar.
            Table table = getTable(dados);

            document.setMargins(90, 36, 36, 36);
            document.add(table);
            document.add(new Paragraph(texto));
            document.close();
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar PDF", e);
        }
    }

    private static Table getTable(Map<String, List<String>> dados) {
        // Quantidade de colunas conforme cabeçalho
        int colunas = dados.size();
        Table table = new Table(colunas);

        // Cabeçalho
        for (String coluna : dados.keySet()) {
            Cell cellHeader = new Cell().add(new Paragraph(coluna)).setBorder(Border.NO_BORDER);
            table.addHeaderCell(cellHeader);
        }

        // Descobre quantidade de linhas pela primeira coluna
        int linhas = dados.values().iterator().next().size();

        // Corpo da tabela
        for (int i = 0; i < linhas; i++) {
            for (List<String> listaColuna : dados.values()) {
                String valor = (i < listaColuna.size()) ? listaColuna.get(i) : "";
                Cell cell = new Cell().add(new Paragraph(valor)).setBorder(Border.NO_BORDER);
                table.addCell(cell);
            }
        }

        table.setBorder(Border.NO_BORDER);
        return table;
    }


}
