package com.rcr.core_engine.serviceImpl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.rcr.core_engine.dtos.ApiResponse;
import com.rcr.core_engine.dtos.CandidateRegistrationRequest;
import com.rcr.core_engine.entity.User;
import com.rcr.core_engine.enums.Role;
import com.rcr.core_engine.repositories.UserRepository;
import com.rcr.core_engine.services.CandidateAuthService;
import com.rcr.core_engine.services.CaptchaService;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class CandidateAuthServiceImpl implements CandidateAuthService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final CaptchaService captchaService;

   
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
}
