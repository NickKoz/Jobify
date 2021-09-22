package com.webdev.jobify.repos;

import com.webdev.jobify.model.JobAd;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobAdRepo extends JpaRepository<JobAd, Long> {

    JobAd findJobAdById(Long id);

    List<JobAd> findAllByCreatorId(Long employee_id);

}
