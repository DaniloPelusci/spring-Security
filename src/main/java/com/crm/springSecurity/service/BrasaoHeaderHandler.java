package com.crm.springSecurity.service;

import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.element.Image;
import com.itextpdf.io.image.ImageDataFactory;

public class BrasaoHeaderHandler implements IEventHandler {
    private final byte[] brasaoBytes;

    public BrasaoHeaderHandler(byte[] brasaoBytes) {
        this.brasaoBytes = brasaoBytes;
    }

    @Override
    public void handleEvent(Event event) {
        PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
        PdfDocument pdfDoc = docEvent.getDocument();
        Rectangle pageSize = pdfDoc.getDefaultPageSize();
        PdfCanvas pdfCanvas = new PdfCanvas(docEvent.getPage());
        try {
            Image img = new Image(ImageDataFactory.create(brasaoBytes));
            float larguraPagina = pageSize.getWidth();
            float alturaMaxima = 60f;
            if (img.getImageScaledHeight() > alturaMaxima) {
                img.scaleToFit(img.getImageScaledWidth(), alturaMaxima);
            }
            float x = (larguraPagina - img.getImageScaledWidth()) / 2f;
            float y = pageSize.getTop() - img.getImageScaledHeight() - 5;
            new Canvas(pdfCanvas, pageSize)
                    .add(img.setFixedPosition(x, y));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

