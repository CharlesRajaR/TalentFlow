package com.rcr.core_engine.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SendOtpRequest {
    // Send either email or mobile
    private String email;
    private String mobile;
    
    @NotBlank(message = "Channel is required: EMAIL or MOBILE")
    private String channel; // "EMAIL" or "MOBILE"
}