package com.rcr.core_engine.services;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OtpService {

    private static final long OTP_VALIDITY_SECONDS = 300; // 5 minutes
    private final Map<String, OtpEntry> otpCache = new ConcurrentHashMap<>();
    private final SecureRandom random = new SecureRandom();

    private record OtpEntry(String otp, Instant expiresAt) {}

    public String generateOtp(String key) {
        // Generate a 6-digit numeric OTP
        String otp = String.format("%06d", random.nextInt(1_000_000));
        otpCache.put(key, new OtpEntry(otp, Instant.now().plusSeconds(OTP_VALIDITY_SECONDS)));
        
        // Log to console for development/testing
        System.out.println("DEBUG >>> OTP for [" + key + "] is: " + otp);
        return otp;
    }

    public boolean validateAndConsumeOtp(String key, String incomingOtp) {
        OtpEntry entry = otpCache.get(key);
        if (entry == null) {
            return false;
        }

        if (Instant.now().isAfter(entry.expiresAt())) {
            otpCache.remove(key);
            return false;
        }

        if (entry.otp().equals(incomingOtp)) {
            otpCache.remove(key); // single-use only
            return true;
        }

        return false;
    }
}