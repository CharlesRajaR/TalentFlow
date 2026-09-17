package com.rcr.core_engine.serviceImpl;

import com.rcr.core_engine.dtos.ApiResponse;
import com.rcr.core_engine.dtos.CreateJobRequest;
import com.rcr.core_engine.dtos.JobResponse;
import com.rcr.core_engine.entity.FormFieldConfig;
import com.rcr.core_engine.entity.Job;
import com.rcr.core_engine.entity.Job.JobStatus;
import com.rcr.core_engine.enums.Role;
import com.rcr.core_engine.entity.User;
import com.rcr.core_engine.repositories.JobRepository;
import com.rcr.core_engine.repositories.UserRepository;
import com.rcr.core_engine.services.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public JobResponse createJob(CreateJobRequest request, String recruiterEmail) {
        User recruiter = userRepository.findByEmail(recruiterEmail)
                .orElseThrow(() -> new IllegalArgumentException("Recruiter not found"));

        if (recruiter.getRole() != Role.ROLE_RECRUITER && recruiter.getRole() != Role.ROLE_ADMIM) {
            throw new AccessDeniedException("Only recruiters can post jobs");
        }

        // Apply default form fields if none are specified by recruiter
        List<FormFieldConfig> schema = (request.getFormFields() != null && !request.getFormFields().isEmpty())
                ? request.getFormFields()
                : getDefaultApplicationForm();

        Job job = Job.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .experience(request.getExperience())
                .skills(request.getSkills())
                .applicationFormSchema(schema)
                .status(JobStatus.ACTIVE)
                .recruiter(recruiter)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Job savedJob = jobRepository.save(job);
        return mapToResponse(savedJob);
    }

    @Override
    public List<JobResponse> getRecruiterJobs(String recruiterEmail) {
        User recruiter = userRepository.findByEmail(recruiterEmail)
                .orElseThrow(() -> new IllegalArgumentException("Recruiter not found"));

        return jobRepository.findByRecruiterIdAndStatusNot(recruiter.getId(), JobStatus.DELETED)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public JobResponse getJobById(Long jobId) {
        Job job = jobRepository.findByIdAndStatusNot(jobId, JobStatus.DELETED)
                .orElseThrow(() -> new IllegalArgumentException("Job posting not found"));
        return mapToResponse(job);
    }

    @Override
    @Transactional
    public ApiResponse deleteJob(Long jobId, String recruiterEmail) {
        Job job = jobRepository.findByIdAndStatusNot(jobId, JobStatus.DELETED)
                .orElseThrow(() -> new IllegalArgumentException("Job posting not found"));

        // Only the owner recruiter or ADMIN can delete
        if (!job.getRecruiter().getEmail().equalsIgnoreCase(recruiterEmail)) {
            throw new AccessDeniedException("You are not authorized to delete this job posting");
        }

        // Soft delete
        job.setStatus(JobStatus.DELETED);
        job.setUpdatedAt(LocalDateTime.now());
        jobRepository.save(job);

        return new ApiResponse(true, "Job post deleted successfully");
    }

    private List<FormFieldConfig> getDefaultApplicationForm() {
        List<FormFieldConfig> defaults = new ArrayList<>();
        defaults.add(new FormFieldConfig("fullName", "Full Name", "text", true, "Enter full name"));
        defaults.add(new FormFieldConfig("email", "Email Address", "email", true, "name@example.com"));
        defaults.add(new FormFieldConfig("mobile", "Mobile Number", "tel", true, "10-digit mobile number"));
        defaults.add(new FormFieldConfig("resume", "Resume / CV", "file", true, "Upload PDF/DOCX"));
        return defaults;
    }

    private JobResponse mapToResponse(Job job) {
        return JobResponse.builder()
                .id(job.getId())
                .title(job.getTitle())
                .description(job.getDescription())
                .experience(job.getExperience())
                .skills(job.getSkills())
                .applicationFormSchema(job.getApplicationFormSchema())
                .status(job.getStatus())
                .recruiterId(job.getRecruiter().getId())
                .recruiterEmail(job.getRecruiter().getEmail())
                .createdAt(job.getCreatedAt())
                .build();
    }
}