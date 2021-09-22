package com.webdev.jobify.repos;

import com.webdev.jobify.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JobRepo extends JpaRepository<Job, Long> {

    Job findJobById(Long id);

    @Query(value = "select * from job j where j.employee_id = :employee_id", nativeQuery = true)
    List<Job> findJobsByEmployeeId(@Param("employee_id")Long employee_id);
}
