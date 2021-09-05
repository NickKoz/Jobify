package com.webdev.jobify.controllers;


import com.webdev.jobify.assemblers.AdministratorModelAssembler;
import com.webdev.jobify.exception.UserNotFoundException;
import com.webdev.jobify.model.Administrator;
import com.webdev.jobify.services.AdministratorService;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/admin")
public class AdministratorController {

    private final AdministratorService adminService;
    private final AdministratorModelAssembler assembler;

    public AdministratorController(AdministratorService adminService, AdministratorModelAssembler assembler) {
        this.adminService = adminService;
        this.assembler = assembler;
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginAdmin(@RequestParam("email") String email, @RequestParam("password") String password) {

        EntityModel<Administrator> entityModel;

        try {
            entityModel = assembler.toModel(adminService.findAdministratorByEmail(email));
        }
        catch (UserNotFoundException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email was not found!");
        }

        // If admin's mail is found, we check the given password.
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        boolean isCorrectPassword = passwordEncoder.matches(password, entityModel.getContent().getPassword());

        if(isCorrectPassword){
            return ResponseEntity.status(HttpStatus.OK).body(entityModel);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Wrong password!");

    }

    @GetMapping("/find/{email}")
    public EntityModel<Administrator> getAdminByEmail(@PathVariable("email") String email){
        Administrator admin = adminService.findAdministratorByEmail(email);
        return assembler.toModel(admin);
    }


}
