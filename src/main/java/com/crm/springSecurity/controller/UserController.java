package com.crm.springSecurity.controller;

import com.crm.springSecurity.alth.modelSecurity.User;
import com.crm.springSecurity.model.dto.UserCadastroDTO;
import com.crm.springSecurity.model.dto.UserDTO;
import com.crm.springSecurity.model.dto.UserEdicaoDTO;
import com.crm.springSecurity.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // Endpoint para listar todos os usuários (apenas admin)
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserDTO> listarUsuarios() {
        return userService.listarTodos();
    }
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> cadastrarUsuario(@RequestBody UserCadastroDTO dto) {
        User user = userService.cadastrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(UserDTO.fromEntity(user));
    }

    @PutMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserDTO> atualizarProprioUsuario(@RequestBody UserEdicaoDTO dto, Authentication authenticationauth) {
        String username = authenticationauth.getName();
        User atualizado = userService.atualizarProprioUsuario(username, dto);
        return ResponseEntity.ok(UserDTO.fromEntity(atualizado));
    }

    @GetMapping("/corretores")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GERENTE_DE_LEADS')")
    public ResponseEntity<List<UserDTO>> listarCorretores() {
        List<UserDTO> corretores = userService.buscarCorretores();
        return ResponseEntity.ok(corretores);
    }


    // Demais endpoints...
}

