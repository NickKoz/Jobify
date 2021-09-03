package com.webdev.jobify.repos;

import com.webdev.jobify.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdministratorRepo extends JpaRepository<Employee, Long> {
}
