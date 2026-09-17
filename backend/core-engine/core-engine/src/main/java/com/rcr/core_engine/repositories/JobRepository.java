package com.rcr.core_engine.repositories;

import com.rcr.core_engine.entity.Job;
import com.rcr.core_engine.entity.Job.JobStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    List<Job> findByRecruiterIdAndStatusNot(Long recruiterId, JobStatus status);
    Optional<Job> findByIdAndStatusNot(Long id, JobStatus status);
    List<Job> findByStatus(JobStatus status);
}