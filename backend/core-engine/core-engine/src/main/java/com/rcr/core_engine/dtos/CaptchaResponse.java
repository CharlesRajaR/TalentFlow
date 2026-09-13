package com.rcr.core_engine.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter 
@Setter 
@AllArgsConstructor
@NoArgsConstructor 
public class CaptchaResponse {
    private String captchaId;
    private String imageBase64;    
}
