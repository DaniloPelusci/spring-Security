package com.crm.springSecurity.service;

import com.crm.springSecurity.alth.modelSecurity.Permission;
import com.crm.springSecurity.alth.modelSecurity.User;
import com.crm.springSecurity.alth.repository.PermissionRepository;
import com.crm.springSecurity.alth.repository.UserRepository;

import com.crm.springSecurity.model.dto.UserCadastroDTO;
import com.crm.springSecurity.model.dto.UserDTO;
import com.crm.springSecurity.model.dto.UserEdicaoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    public User getUsuarioByAuthentication(Authentication authentication) {
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
    }

    public List<UserDTO> listarTodos() {
        List<User> users = userRepository.findAll();
        return users.stream().map(UserDTO::fromEntity).collect(Collectors.toList());
    }

    public User atualizarProprioUsuario(String username, UserEdicaoDTO dto) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        if (dto.getNome() != null) user.setNome(dto.getNome());
        if (dto.getEmail() != null) user.setEmail(dto.getEmail());
        if (dto.getTelefone() != null) user.setTelefone(dto.getTelefone());
        // Se quiser permitir alteração de senha:
        if (dto.getPassword() != null && !dto.getPassword().isBlank())
            user.setPassword(new BCryptPasswordEncoder().encode(dto.getPassword()));
        return userRepository.save(user);
    }
    public User cadastrar(UserCadastroDTO dto) {
        if (userRepository.existsByUsername(dto.getUserName())){
            throw new RuntimeException("Usuário já existe");
        }

        User user = new User();
        user.setUsername(dto.getUserName());
        user.setNome(dto.getNome());
        user.setEmail(dto.getEmail());
        user.setTelefone(dto.getTelefone());
        user.setPassword(new BCryptPasswordEncoder().encode(dto.getPassword()));

        // Setar todos os campos de conta como true (importante para login funcionar)
        user.setEnabled(true);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);

        // ATENÇÃO: Setando roles/permissões
        List<Permission> permissoes = dto.getPermissions();
        if (permissoes == null || permissoes.isEmpty()) {
            // Busca uma role padrão (ex: ROLE_USER)

            List<Permission> roleUser = new ArrayList<>();
            for (Permission permission : permissoes) {
                roleUser.add(permissionRepository.findByDescription(permission.getDescription()));
            }
            user.setAuthorities(roleUser);
        } else {
            user.setAuthorities(permissoes);
        }

        return userRepository.save(user);
    }

    public List<UserDTO> buscarCorretores() {
        // Busca todos usuários com role 'CORRETOR'
        return userRepository.findByAuthoritiesDescription("ROLE_CORRETOR")
                .stream()
                .map(UserDTO::fromEntity)
                .toList();
    }

}

