package com.crm.springSecurity.althControler;

import com.crm.springSecurity.jwt.JwtUtils;
import com.crm.springSecurity.modelSecurity.User;
import com.crm.springSecurity.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));


        String token = jwtUtils.generateToken(user);
        return ResponseEntity.ok(new AuthResponse(token));
    }

    public record AuthRequest(String username, String password) {}
    public record AuthResponse(String token) {}

    @GetMapping("/me")
    public ResponseEntity<?> getMe(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        Long userId = jwtUtils.extractUserId(token);

        return ResponseEntity.ok("ID do usuário logado: " + userId);
    }
    @GetMapping("/me/1")
    public ResponseEntity<?> getMe() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok("ID do usuário logado: " + user.getId());
    }


}

