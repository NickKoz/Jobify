package com.webdev.jobify.repos;
import org.springframework.data.jpa.repository.JpaRepository;
import com.webdev.jobify.model.Employee;

import java.util.Optional;

public interface EmployeeRepo extends  JpaRepository<Employee, Long>{

    void deleteEmployeeById(Long id);

    Optional<Employee> findEmployeeById(Long id);

    Optional<Employee> findEmployeeByEmail(String email);
    boolean existsByEmail(String email);
}
