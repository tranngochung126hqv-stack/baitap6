package com.example.coursemanagement.controller;

import com.example.coursemanagement.dto.LoginRequest;
import com.example.coursemanagement.security.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AuthApiController {
    private final JwtService jwtService;

    public AuthApiController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        if (request == null || request.username() == null || request.password() == null)
            return ResponseEntity.badRequest()
                    .body(Map.of("status", "unauthorized", "message", "Thieu tai khoan hoac mat khau."));
        String role;
        // Demo accounts for the assignment. Connect this check to the project's user
        // table in a real system.
        if ("admin".equals(request.username()) && "admin123".equals(request.password()))
            role = "ADMIN";
        else if ("user".equals(request.username()) && "user123".equals(request.password()))
            role = "USER";
        else
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("status", "unauthorized", "message", "Sai tai khoan hoac mat khau."));
        String token = jwtService.generateToken(request.username(), role);
        return ResponseEntity.ok(Map.of("status", "authorized", "tokenType", "Bearer", "token", token,
                "username", request.username(), "role", role));
    }

    @GetMapping("/user/profile")
    public Map<String, Object> userProfile(org.springframework.security.core.Authentication authentication) {
        return Map.of("status", "authorized", "message", "Ban da truy cap API danh cho USER.",
                "username", authentication.getName(), "role", authentication.getAuthorities());
    }

    @GetMapping("/admin/dashboard")
    public Map<String, Object> adminDashboard(org.springframework.security.core.Authentication authentication) {
        return Map.of("status", "authorized", "message", "Ban da truy cap API danh cho ADMIN.",
                "username", authentication.getName(), "role", authentication.getAuthorities());
    }
}
