package com.rcr.core_engine.serviceImpl;

import org.springframework.stereotype.Service;

import com.rcr.core_engine.dtos.CaptchaResponse;
import com.rcr.core_engine.services.CaptchaService;
import com.rcr.core_engine.utilities.CaptchaUtil;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class CaptchaServiceImpl implements CaptchaService {

    private final CaptchaUtil captchaUtil;

    @Override
    public CaptchaResponse generateCaptcha() {
        String text = captchaUtil.generateCaptchaText();
        String imageBase64 = captchaUtil.generateCaptchaImageBase64(text);
        String captchaId = captchaUtil.createSignedToken(text);

        return new CaptchaResponse(captchaId, imageBase64);
    }

    @Override
    public Boolean verifyCaptcha(String captchaId, String captchaInput) {
        return captchaUtil.validateCaptcha(captchaId, captchaInput);
    }
   
}
