package com.crm.zipupload.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resumo do processamento do upload de inspeções via arquivo ZIP")
public record ZipUploadResultDto(
        @Schema(description = "ID do inspetor identificado pelo nome do arquivo ZIP", example = "15")
        Long inspectorId,
        @Schema(description = "Quantidade total de fotos salvas", example = "3")
        int totalPhotos,
        @Schema(description = "Quantidade de inspeções (work orders) processadas", example = "2")
        int totalInspections) {
}
