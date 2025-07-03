package com.crm.springSecurity.controller;

import com.crm.springSecurity.alth.modelSecurity.User;
import com.crm.springSecurity.model.dto.UserCadastroDTO;
import com.crm.springSecurity.model.dto.UserDTO;
import com.crm.springSecurity.model.dto.UserEdicaoDTO;
import com.crm.springSecurity.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Usuários",
        description = "APIs para gerenciamento de usuários"
)
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(
            summary = "Listar todos os usuários",
            description = "Retorna todos os usuários cadastrados. Requer papel ADMIN.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuários listados com sucesso")
            }
    )
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserDTO> listarUsuarios() {
        return userService.listarTodos();
    }

    @Operation(
            summary = "Cadastrar novo usuário",
            description = "Cria um novo usuário no sistema. Requer papel ADMIN.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(schema = @Schema(implementation = UserCadastroDTO.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso",
                            content = @Content(schema = @Schema(implementation = UserDTO.class)))
            }
    )
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> cadastrarUsuario(@RequestBody UserCadastroDTO dto) {
        User user = userService.cadastrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(UserDTO.fromEntity(user));
    }

    @Operation(
            summary = "Atualizar o próprio usuário",
            description = "Permite que o usuário autenticado atualize seus próprios dados.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(schema = @Schema(implementation = UserEdicaoDTO.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso",
                            content = @Content(schema = @Schema(implementation = UserDTO.class)))
            }
    )
    @PutMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserDTO> atualizarProprioUsuario(
            @RequestBody UserEdicaoDTO dto,
            Authentication authenticationauth) {
        String username = authenticationauth.getName();
        User atualizado = userService.atualizarProprioUsuario(username, dto);
        return ResponseEntity.ok(UserDTO.fromEntity(atualizado));
    }

    @Operation(
            summary = "Listar todos os corretores",
            description = "Retorna todos os usuários com papel de corretor. Requer papel ADMIN ou GERENTE_DE_LEADS.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Corretores listados com sucesso")
            }
    )
    @GetMapping("/corretores")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE_DE_LEADS')")
    public ResponseEntity<List<UserDTO>> listarCorretores() {
        List<UserDTO> corretores = userService.buscarCorretores();
        return ResponseEntity.ok(corretores);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUsuarioID(@PathVariable Long id) {
        UserDTO user = userService.getById(id);

        return ResponseEntity.ok(user);
    }

    // Demais endpoints...
}
