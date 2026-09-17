package com.rcr.core_engine.dtos;

import com.rcr.core_engine.entity.FormFieldConfig;
import com.rcr.core_engine.entity.Job.JobStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class JobResponse {
    private Long id;
    private String title;
    private String description;
    private String experience;
    private List<String> skills;
    private List<FormFieldConfig> applicationFormSchema;
    private JobStatus status;
    private Long recruiterId;
    private String recruiterEmail;
    private LocalDateTime createdAt;
}