package com.rcr.core_engine.controllers;

import com.rcr.core_engine.dtos.ApiResponse;
import com.rcr.core_engine.dtos.AuthResponse;
import com.rcr.core_engine.dtos.RecruiterLoginRequest;
import com.rcr.core_engine.dtos.RecruiterRegistrationRequest;
import com.rcr.core_engine.services.RecruiterAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth/recruiter")
@RequiredArgsConstructor
@Tag(name = "Recruiter Auth", description = "Endpoints for recruiter registration and authentication")
public class RecruiterAuthController {

    private final RecruiterAuthService recruiterAuthService;

    @PostMapping("/register")
    @Operation(summary = "Register a new recruiter account")
    public ResponseEntity<ApiResponse> register(@Valid @RequestBody RecruiterRegistrationRequest request) {
        ApiResponse response = recruiterAuthService.registerRecruiter(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    @Operation(summary = "Recruiter Email and Password Login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody RecruiterLoginRequest request) {
        AuthResponse response = recruiterAuthService.login(request);
        return ResponseEntity.ok(response);
    }
}