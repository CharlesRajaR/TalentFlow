package com.rcr.core_engine.services;

import com.rcr.core_engine.dtos.ApiResponse;
import com.rcr.core_engine.dtos.AuthResponse;
import com.rcr.core_engine.dtos.RecruiterLoginRequest;
import com.rcr.core_engine.dtos.RecruiterRegistrationRequest;

public interface RecruiterAuthService {
    ApiResponse registerRecruiter(RecruiterRegistrationRequest request);
    AuthResponse login(RecruiterLoginRequest request);
}