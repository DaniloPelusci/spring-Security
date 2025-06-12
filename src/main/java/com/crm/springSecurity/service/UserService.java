package com.crm.springSecurity.service;

import com.crm.springSecurity.alth.modelSecurity.User;
import com.crm.springSecurity.alth.repository.UserRepository;

import com.crm.springSecurity.model.dto.UserCadastroDTO;
import com.crm.springSecurity.model.dto.UserDTO;
import com.crm.springSecurity.model.dto.UserEdicaoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

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
        if (userRepository.existsByUsername(((dto.getUserName())))){
            throw new RuntimeException("Usuário já existe");
        }
        User user = new User();
        user.setUsername(dto.getUserName());
        user.setNome(dto.getNome());
        user.setEmail(dto.getEmail());
        user.setTelefone(dto.getTelefone());
        user.setPassword(new BCryptPasswordEncoder().encode(dto.getPassword()));
        user.setEnabled(true);
        // Você pode definir permissões padrão aqui, se desejar
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

