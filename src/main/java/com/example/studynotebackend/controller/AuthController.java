package com.example.studynotebackend.controller;

import com.example.studynotebackend.domain.UserAccount;
import com.example.studynotebackend.dto.LoginRequest;
import com.example.studynotebackend.dto.LoginResponse;
import com.example.studynotebackend.dto.RegisterRequest;
import com.example.studynotebackend.service.AuthService;
import com.example.studynotebackend.dto.UserDto;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req) {
        authService.register(req);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest req) {
        LoginResponse resp = authService.login(req);
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> me() {
        // 从 SecurityContext 中取出用户名
        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        UserAccount user = authService.findByUsername(username);
        // 转 DTO
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setCreatedAt(user.getCreatedAt().toString());
        return ResponseEntity.ok(dto);
    }
}
