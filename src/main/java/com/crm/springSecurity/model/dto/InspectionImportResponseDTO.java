package com.crm.springSecurity.model.dto;

public class InspectionImportResponseDTO {
    private int totalLinhas;
    private int importadas;

    public InspectionImportResponseDTO(int totalLinhas, int importadas) {
        this.totalLinhas = totalLinhas;
        this.importadas = importadas;
    }

    public int getTotalLinhas() {
        return totalLinhas;
    }

    public void setTotalLinhas(int totalLinhas) {
        this.totalLinhas = totalLinhas;
    }

    public int getImportadas() {
        return importadas;
    }

    public void setImportadas(int importadas) {
        this.importadas = importadas;
    }
}
