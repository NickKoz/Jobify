package com.webdev.linkedin.repo;
import org.springframework.data.jpa.repository.JpaRepository;
import com.webdev.linkedin.model.Employee;

import java.util.Optional;

public interface EmployeeRepo extends  JpaRepository<Employee, Long>{

    void deleteEmployeeById(Long id);

    Optional<Employee> findEmployeeById(Long id);
}
