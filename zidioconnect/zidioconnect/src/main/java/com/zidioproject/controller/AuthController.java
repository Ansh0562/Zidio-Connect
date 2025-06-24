package com.zidioproject.controller;

import com.zidioproject.dto.RegisterRequest;
import com.zidioproject.entity.User;
import com.zidioproject.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.zidioproject.dto.LoginRequest;
import com.zidioproject.service.AuthService;
import com.zidioproject.security.JwtService;
import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    // Use RegistrationService to handle registration for all user types
    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtService jwtService;

    /**
     * Register a new user and save all related data (user + profile based on role).
     * This endpoint will store data in all relevant tables, not just the user table.
     * @param request Registration details (name, email, password, role, etc.)
     * @return Success message
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        // Call the registration service to save user and profile
        registrationService.registerUser(request);
        return ResponseEntity.ok("Registration successful");
    }

    /**
     * Login endpoint: Authenticates user and returns JWT token if successful.
     * @param request LoginRequest with email and password
     * @return JWT token if credentials are valid
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        // Authenticate user using Lombok-generated getters
        User user = authService.authenticate(request.getEmail(), request.getPassword());
        // Generate JWT token with authorities for role-based authorization
        String token = jwtService.generateToken(user);
        // Return token in response
        return ResponseEntity.ok(Collections.singletonMap("token", token));
    }
}
