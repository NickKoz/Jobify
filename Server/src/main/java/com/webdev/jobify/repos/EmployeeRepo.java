package com.webdev.jobify.repos;
import org.springframework.data.jpa.repository.JpaRepository;
import com.webdev.jobify.model.Employee;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepo extends  JpaRepository<Employee, Long>{

    void deleteEmployeeById(Long id);

    Optional<Employee> findEmployeeById(Long id);

    Optional<Employee> findEmployeeByEmail(String email);

    @Query(value = "select ja.applicants_id from jobad_applicants ja where ja.job_ad_id = :id", nativeQuery = true)
    List<Employee> findApplicantsUsingJobAdId(@Param("id") Long id);

    @Query(value = "select s.skill from skills s where s.employee_id = :id", nativeQuery = true)
    List<String> findSkillsOfEmployee(@Param("id") Long id);

    boolean existsByEmail(String email);
}
