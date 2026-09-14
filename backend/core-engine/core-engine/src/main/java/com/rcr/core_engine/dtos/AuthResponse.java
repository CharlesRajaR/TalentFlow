package com.rcr.core_engine.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthResponse {
    private boolean success;
    private String message;
    private String accessToken;
    private String tokenType;
    private String role;
}
