package com.pinhaDev.worknest.controllers;

import com.pinhaDev.worknest.Services.AuthService;
import com.pinhaDev.worknest.dto.LoginRequest;
import com.pinhaDev.worknest.dto.LoginResponse;
import com.pinhaDev.worknest.dto.RegisterRequest;
import com.pinhaDev.worknest.dto.RegisterResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest request) {
        authService.register(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(new RegisterResponse("Usuário registrado com sucesso!"));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        var token = authService.login(request);

        return ResponseEntity.status(HttpStatus.OK).body(new LoginResponse(token));
    }
}
