package com.webdev.jobify.services;

import com.webdev.jobify.model.JobAd;
import com.webdev.jobify.repos.JobAdRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobAdService {

    public final JobAdRepo jobAdRepo;

    @Autowired
    public JobAdService(JobAdRepo jobAdRepo) {
        this.jobAdRepo = jobAdRepo;
    }


    public JobAd findJobAdById(Long id) {
        return jobAdRepo.findJobAdById(id);
    }

    public List<JobAd> findAllJobAds() {
        return jobAdRepo.findAll();
    }

    public JobAd saveJobAd(JobAd jobAd) {
        return jobAdRepo.save(jobAd);
    }

    public List<JobAd> findAllByCreatorId(Long employee_id) {
        return jobAdRepo.findAllByCreatorId(employee_id);
    }

}
