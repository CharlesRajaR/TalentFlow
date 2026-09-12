package com.rcr.core_engine.controllers;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rcr.core_engine.dtos.ApiResponse;
import com.rcr.core_engine.dtos.CandidateRegistrationRequest;
import com.rcr.core_engine.services.CandidateAuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController 
@RequestMapping("api/v1/auth/")
@RequiredArgsConstructor 
public class CandidateAuthController {
    private final CandidateAuthService candidateAuthService;

    @PostMapping("register")
    public ResponseEntity<ApiResponse> postMethodName(@Valid @RequestBody CandidateRegistrationRequest
    request) {
        
        ApiResponse response = candidateAuthService.registerCandidate(request);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
}
