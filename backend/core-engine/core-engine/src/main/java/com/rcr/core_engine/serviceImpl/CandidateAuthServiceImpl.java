package com.rcr.core_engine.serviceImpl;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.rcr.core_engine.dtos.ApiResponse;
import com.rcr.core_engine.dtos.AuthResponse;
import com.rcr.core_engine.dtos.CandidateRegistrationRequest;
import com.rcr.core_engine.dtos.LoginRequest;
import com.rcr.core_engine.dtos.SendOtpRequest;
import com.rcr.core_engine.entity.User;
import com.rcr.core_engine.enums.Role;
import com.rcr.core_engine.repositories.UserRepository;
import com.rcr.core_engine.security.JwtService;
import com.rcr.core_engine.services.CandidateAuthService;
import com.rcr.core_engine.services.CaptchaService;
import com.rcr.core_engine.services.OtpService;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class CandidateAuthServiceImpl implements CandidateAuthService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final CaptchaService captchaService;

    private final OtpService otpService;

    private final JwtService jwtService;

   
    @Override 
    public ApiResponse registerCandidate(CandidateRegistrationRequest request){
        if(!captchaService.verifyCaptcha(request.getCaptchaId(), request.getCaptchaInput())){
            throw new IllegalArgumentException("Invalid Captcha! Please try again");
        }


        if(userRepository.existsByEmail(request.getEmail())){
            throw new IllegalArgumentException("Email already exists!");
        }

        if(userRepository.existsByMobile(request.getMobile())){
            throw new IllegalArgumentException("Same mobile number already exists!");
        }

        User candidate = User.builder()
        .firstName(request.getFirstName().trim())
        .lastName(request.getLastName().trim())
        .mobile(request.getMobile().trim())
        .email(request.getEmail().trim())
        .password(passwordEncoder.encode(request.getMobile().trim()))
        .role(Role.ROLE_CANDIDATE)
        .dob(request.getDob())
        .build();

        userRepository.save(candidate);

        ApiResponse response = new ApiResponse(true, "candidate registered successfully");

        return response;
    }


    @Override
    public void sendOtp(SendOtpRequest request) {
        String key;
        if ("EMAIL".equalsIgnoreCase(request.getChannel())) {
            if (request.getEmail() == null || request.getEmail().isBlank()) {
                throw new IllegalArgumentException("Email is required for EMAIL OTP");
            }
            // Check candidate exists
            userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new BadCredentialsException("Account not found with this email"));
            key = request.getEmail().trim().toLowerCase();
        } else if ("MOBILE".equalsIgnoreCase(request.getChannel())) {
            if (request.getMobile() == null || request.getMobile().isBlank()) {
                throw new IllegalArgumentException("Mobile is required for MOBILE OTP");
            }
            userRepository.findByMobile(request.getMobile())
                    .orElseThrow(() -> new BadCredentialsException("Account not found with this mobile number"));
            key = request.getMobile().trim();
        } else {
            throw new IllegalArgumentException("Invalid OTP channel. Use EMAIL or MOBILE.");
        }

        // Generates and caches the OTP (prints to logs)
        otpService.generateOtp(key);
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        User user;

        switch (request.getLoginType()) {
            case EMAIL_PASSWORD -> {
                if (request.getEmail() == null || request.getPassword() == null) {
                    throw new BadCredentialsException("Email and password must be provided");
                }
                user = userRepository.findByEmail(request.getEmail().trim().toLowerCase())
                        .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

                if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                    throw new BadCredentialsException("Invalid email or password");
                }
            }
            case EMAIL_OTP -> {
                if (request.getEmail() == null || request.getOtp() == null) {
                    throw new BadCredentialsException("Email and OTP must be provided");
                }
                String email = request.getEmail().trim().toLowerCase();
                user = userRepository.findByEmail(email)
                        .orElseThrow(() -> new BadCredentialsException("Account not found"));

                boolean isValid = otpService.validateAndConsumeOtp(email, request.getOtp().trim());
                if (!isValid) {
                    throw new BadCredentialsException("Invalid or expired OTP");
                }
            }
            case MOBILE_OTP -> {
                if (request.getMobile() == null || request.getOtp() == null) {
                    throw new BadCredentialsException("Mobile and OTP must be provided");
                }
                String mobile = request.getMobile().trim();
                user = userRepository.findByMobile(mobile)
                        .orElseThrow(() -> new BadCredentialsException("Account not found"));

                boolean isValid = otpService.validateAndConsumeOtp(mobile, request.getOtp().trim());
                if (!isValid) {
                    throw new BadCredentialsException("Invalid or expired OTP");
                }
            }
            default -> throw new IllegalArgumentException("Unsupported login type");
        }

        // Issue JWT token upon successful authentication
        String token = jwtService.generateToken(user.getEmail(), user.getRole().name());

        return AuthResponse.builder()
                .success(true)
                .message("Login successful")
                .accessToken(token)
                .tokenType("Bearer")
                .role(user.getRole().name())
                .build();
    }   
}
