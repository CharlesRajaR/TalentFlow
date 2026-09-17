package com.rcr.core_engine.dtos;

import com.rcr.core_engine.entity.FormFieldConfig;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class CreateJobRequest {

    @NotBlank(message = "Job title is required")
    private String title;

    @NotBlank(message = "Job description is required")
    private String description;

    @NotBlank(message = "Experience is required")
    private String experience;

    @NotEmpty(message = "At least one skill is required")
    private List<String> skills;

    // Form fields to be generated dynamically on frontend
    private List<FormFieldConfig> formFields;
}