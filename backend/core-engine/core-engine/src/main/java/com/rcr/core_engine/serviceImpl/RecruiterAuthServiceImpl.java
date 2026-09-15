package com.rcr.core_engine.serviceImpl;

import com.rcr.core_engine.dtos.ApiResponse;
import com.rcr.core_engine.dtos.AuthResponse;
import com.rcr.core_engine.dtos.RecruiterLoginRequest;
import com.rcr.core_engine.dtos.RecruiterRegistrationRequest;
import com.rcr.core_engine.enums.Role;
import com.rcr.core_engine.entity.User;
import com.rcr.core_engine.repositories.UserRepository;
import com.rcr.core_engine.security.JwtService;
import com.rcr.core_engine.services.CaptchaService; 
import com.rcr.core_engine.services.RecruiterAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RecruiterAuthServiceImpl implements RecruiterAuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final CaptchaService captchaService;

    @Override
    @Transactional
    public ApiResponse registerRecruiter(RecruiterRegistrationRequest request) {
        
        boolean isCaptchaValid = captchaService.verifyCaptcha(request.getCaptchaId(), request.getCaptchaInput());
        if (!isCaptchaValid) {
            throw new IllegalArgumentException("Invalid or expired CAPTCHA");
        }

        String email = request.getEmail().trim().toLowerCase();
        String mobile = request.getMobile().trim();

        
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Email already registered with another account");
        }

        if (userRepository.findByMobile(mobile).isPresent()) {
            throw new IllegalArgumentException("Mobile number already registered with another account");
        }

        User recruiter = User.builder()
                .firstName(request.getFirstName().trim())
                .lastName(request.getLastName().trim())
                .email(email)
                .mobile(mobile)
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ROLE_RECRUITER)
                .createdAt(LocalDateTime.now())
                .build();

        userRepository.save(recruiter);

        return new ApiResponse(true, "Recruiter registered successfully");
    }

    @Override
    public AuthResponse login(RecruiterLoginRequest request) {
        String email = request.getEmail().trim().toLowerCase();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BadCredentialsException("Invalid recruiter credentials"));

        if (user.getRole() != Role.ROLE_RECRUITER) {
            throw new BadCredentialsException("Access denied: Not an authorized recruiter account");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid recruiter credentials");
        }

        String token = jwtService.generateToken(user.getEmail(), user.getRole().toString());

        return AuthResponse.builder()
                .success(true)
                .message("Recruiter authenticated successfully")
                .accessToken(token)
                .tokenType("Bearer")
                .role(user.getRole().toString())
                .build();
    }
}