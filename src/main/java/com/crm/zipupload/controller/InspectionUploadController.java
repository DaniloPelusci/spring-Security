package com.crm.zipupload.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.crm.zipupload.dto.ZipUploadResultDto;
import com.crm.zipupload.service.ZipInspectionUploadService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/inspections")
public class InspectionUploadController {

    private final ZipInspectionUploadService zipInspectionUploadService;

    public InspectionUploadController(ZipInspectionUploadService zipInspectionUploadService) {
        this.zipInspectionUploadService = zipInspectionUploadService;
    }

    @Operation(
            summary = "Upload de ZIP de inspeções",
            description = "Recebe um arquivo .zip para importar fotos de inspeção. " +
                    "O nome do arquivo deve ser o ID do inspetor (ex: 15.zip) e " +
                    "cada pasta interna deve representar uma work order (ex: 1001/foto-1.jpg).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "ZIP processado com sucesso",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ZipUploadResultDto.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "inspectorId": 15,
                                      "totalPhotos": 3,
                                      "totalInspections": 2
                                    }
                                    """))),
            @ApiResponse(responseCode = "400", description = "Arquivo inválido ou ZIP sem fotos válidas",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                                    @ExampleObject(name = "nomeInvalido", value = """
                                            {
                                              "status": 400,
                                              "error": "Bad Request",
                                              "message": "Nome do ZIP deve ser o ID do inspetor (ex: 15.zip)."
                                            }
                                            """),
                                    @ExampleObject(name = "semFotosValidas", value = """
                                            {
                                              "status": 400,
                                              "error": "Bad Request",
                                              "message": "Nenhuma foto válida encontrada. O ZIP deve conter pastas por número de work order com fotos dentro."
                                            }
                                            """) })),
            @ApiResponse(responseCode = "404", description = "Inspetor não encontrado",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = """
                                    {
                                      "status": 404,
                                      "error": "Not Found",
                                      "message": "Inspetor não encontrado para o ID 15"
                                    }
                                    """)))
    })
    @PostMapping(value = "/upload-zip", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ZipUploadResultDto uploadZip(
            @Parameter(description = "Arquivo .zip com nome no formato {idDoInspetor}.zip", required = true)
            @RequestParam("file") MultipartFile file) {
        return zipInspectionUploadService.upload(file);
    }
}
