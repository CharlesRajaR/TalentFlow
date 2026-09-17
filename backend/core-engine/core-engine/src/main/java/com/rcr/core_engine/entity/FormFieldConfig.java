package com.rcr.core_engine.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FormFieldConfig {

    private String fieldKey;      // e.g., "fullName", "resume", "githubUrl"
    private String label;         // e.g., "Upload Resume", "GitHub Profile"
    private String fieldType;     // "text", "email", "tel", "file", "textarea", "number"
    private boolean required;     // true / false
    private String placeholder;   // e.g., "https://github.com/your-username"

}