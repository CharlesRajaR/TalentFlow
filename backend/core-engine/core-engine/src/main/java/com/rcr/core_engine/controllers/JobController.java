package com.rcr.core_engine.controllers;

import com.rcr.core_engine.dtos.ApiResponse;
import com.rcr.core_engine.dtos.CreateJobRequest;
import com.rcr.core_engine.dtos.JobResponse;
import com.rcr.core_engine.services.JobService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/jobs")
@RequiredArgsConstructor
@Tag(name = "Job Management", description = "Endpoints for creating, managing, and viewing job postings")
public class JobController {

    private final JobService jobService;

    @PostMapping
    @Operation(summary = "Create a job post with custom application fields (Recruiter only)")
    public ResponseEntity<JobResponse> createJob(
            @Valid @RequestBody CreateJobRequest request,
            @AuthenticationPrincipal String email
    ) {
        JobResponse response = jobService.createJob(request, email);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/my-jobs")
    @Operation(summary = "Get all jobs posted by the logged-in recruiter")
    public ResponseEntity<List<JobResponse>> getMyJobs(@AuthenticationPrincipal String email) {
        return ResponseEntity.ok(jobService.getRecruiterJobs(email));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get job details and form configuration by ID (Public/Candidate)")
    public ResponseEntity<JobResponse> getJobById(@PathVariable Long id) {
        return ResponseEntity.ok(jobService.getJobById(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a job post (Recruiter only)")
    public ResponseEntity<ApiResponse> deleteJob(
            @PathVariable Long id,
            @AuthenticationPrincipal String email
    ) {
        return ResponseEntity.ok(jobService.deleteJob(id, email));
    }
}