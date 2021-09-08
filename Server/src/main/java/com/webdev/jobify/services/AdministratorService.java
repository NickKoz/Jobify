package com.webdev.jobify.services;


import com.webdev.jobify.exception.UserNotFoundException;
import com.webdev.jobify.model.Administrator;
import com.webdev.jobify.repos.AdministratorRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

    public Administrator addAdministrator(Administrator admin) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPasswd = passwordEncoder.encode(admin.getPassword());

        admin.setPassword(encodedPasswd);

        return adminRepo.save(admin);
    }


}
