package com.rcr.core_engine.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rcr.core_engine.dtos.CaptchaResponse;
import com.rcr.core_engine.services.CaptchaService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;


@RestController 
@RequiredArgsConstructor 
@RequestMapping("api/captcha")
public class CaptchaController {
    private final CaptchaService captchaService;

    @GetMapping("/generate")
    public ResponseEntity<CaptchaResponse> generateCaptcha() {
       return ResponseEntity.ok(captchaService.generateCaptcha());
    }
    
}
