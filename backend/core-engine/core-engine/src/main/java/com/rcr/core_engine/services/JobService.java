package com.rcr.core_engine.services;

import com.rcr.core_engine.dtos.ApiResponse;
import com.rcr.core_engine.dtos.CreateJobRequest;
import com.rcr.core_engine.dtos.JobResponse;

import java.util.List;

public interface JobService {
    JobResponse createJob(CreateJobRequest request, String recruiterEmail);
    List<JobResponse> getRecruiterJobs(String recruiterEmail);
    JobResponse getJobById(Long jobId);
    ApiResponse deleteJob(Long jobId, String recruiterEmail);
}