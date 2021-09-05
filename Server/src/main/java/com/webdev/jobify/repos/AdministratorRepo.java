package com.webdev.jobify.repos;

import com.webdev.jobify.model.Administrator;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdministratorRepo extends JpaRepository<Administrator, Long> {

    Optional<Administrator> findAdministratorByEmail(String email);
}
