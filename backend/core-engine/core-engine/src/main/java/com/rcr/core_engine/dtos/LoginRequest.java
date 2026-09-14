package com.rcr.core_engine.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRequest {

    public enum LoginType {
        EMAIL_PASSWORD,
        EMAIL_OTP,
        MOBILE_OTP
    }

    @NotNull(message = "Login type is required")
    private LoginType loginType;

    // Required for EMAIL_PASSWORD and EMAIL_OTP
    private String email;

    // Required for EMAIL_PASSWORD
    private String password;

    // Required for MOBILE_OTP
    private String mobile;

    // Required for EMAIL_OTP and MOBILE_OTP
    private String otp;
}