package com.rcr.core_engine.services;

import com.rcr.core_engine.dtos.CaptchaResponse;

public interface CaptchaService {
    CaptchaResponse generateCaptcha();
    Boolean verifyCaptcha(String captchaId, String captchaInput);
}
