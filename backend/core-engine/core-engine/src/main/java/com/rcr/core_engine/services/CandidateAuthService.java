package com.rcr.core_engine.services;

import com.rcr.core_engine.dtos.*;
import com.rcr.core_engine.dtos.ApiResponse;

public interface CandidateAuthService {
    ApiResponse registerCandidate(CandidateRegistrationRequest request);
} 
