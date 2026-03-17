# API de Upload ZIP de Inspeções

## Endpoint
- **Método:** `POST`
- **Rota:** `/api/inspections/upload-zip`
- **Content-Type:** `multipart/form-data`
- **Campo esperado:** `file`

## Regras do arquivo ZIP
1. O nome do arquivo deve ser o ID do inspetor com extensão `.zip`.
   - Exemplo válido: `15.zip`
2. Dentro do ZIP, cada pasta de primeiro nível deve representar o número da work order.
   - Exemplo: `1001/foto-1.jpg`
3. Arquivos suportados para content-type automático:
   - `.jpg` / `.jpeg` → `image/jpeg`
   - `.png` → `image/png`
   - `.webp` → `image/webp`
   - Outras extensões → `application/octet-stream`

## Exemplo de estrutura do ZIP
```text
15.zip
 ├── 1001/
 │   ├── frente.jpg
 │   └── lateral.png
 └── 1002/
     └── telhado.jpeg
```

## Resposta de sucesso (200)
```json
{
  "inspectorId": 15,
  "totalPhotos": 3,
  "totalInspections": 2
}
```

## Erros comuns
- **400 Bad Request**
  - `Arquivo ZIP é obrigatório.`
  - `Arquivo precisa ter extensão .zip`
  - `Nome do ZIP deve ser o ID do inspetor (ex: 15.zip).`
  - `Nenhuma foto válida encontrada. O ZIP deve conter pastas por número de work order com fotos dentro.`
- **404 Not Found**
  - `Inspetor não encontrado para o ID {id}`

## Exemplo com cURL
```bash
curl -X POST 'http://localhost:8080/api/inspections/upload-zip' \
  -H 'Authorization: Bearer <token>' \
  -F 'file=@15.zip;type=application/zip'
```
