package com.rcr.core_engine.services;

import com.rcr.core_engine.dtos.*;

public interface CandidateAuthService {
    ApiResponse registerCandidate(CandidateRegistrationRequest request);
    void sendOtp(SendOtpRequest request);
    AuthResponse login(LoginRequest request);
} 
