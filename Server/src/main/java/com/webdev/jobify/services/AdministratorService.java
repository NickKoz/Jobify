package com.webdev.jobify.services;


import com.webdev.jobify.exception.UserNotFoundException;
import com.webdev.jobify.model.Administrator;
import com.webdev.jobify.repos.AdministratorRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdministratorService {

    private final AdministratorRepo adminRepo;

    @Autowired
    public AdministratorService(AdministratorRepo adminRepo) {
        this.adminRepo = adminRepo;
    }

    public Administrator findAdministratorByEmail(String email) {
        return adminRepo.findAdministratorByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email " + email + " was not found"));
    }




}
