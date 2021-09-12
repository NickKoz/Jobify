package com.webdev.jobify.services;

import com.webdev.jobify.model.Job;
import com.webdev.jobify.repos.JobRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobService {

    public final JobRepo jobRepo;

    @Autowired
    public JobService(JobRepo jobRepo) {
        this.jobRepo = jobRepo;
    }

    public Job findJobById(Long id) {
        return jobRepo.findJobById(id);
    }

    public List<Job> findAllJobs() {
        return jobRepo.findAll();
    }

    public Job saveJob(Job job) {
        return jobRepo.save(job);
    }

    public List<Job> findJobsByEmployeeId(Long employee_id) {
        return jobRepo.findJobsByEmployeeId(employee_id);
    }

}
