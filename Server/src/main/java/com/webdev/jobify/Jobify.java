package com.webdev.jobify;

import com.webdev.jobify.model.Administrator;
import com.webdev.jobify.repos.AdministratorRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;


@SpringBootApplication
public class Jobify {

	public static void main(String[] args) {
		SpringApplication.run(Jobify.class, args);
	}

	@Autowired
	AdministratorRepo adminRepo;

	@Bean
	InitializingBean initializeDatabase() {

		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encodedPasswd = passwordEncoder.encode("admin");

		Long id = 1L;
		String adminEmail = "admin@jobify.com";

		Optional<Administrator> admin = adminRepo.findAdministratorByEmail(adminEmail);

		if(admin.isEmpty()) {
			System.out.println("Admin will be created!");
			return () -> {
				adminRepo.save(new Administrator(id, "John", "Papadopoulos", adminEmail, encodedPasswd));
			};
		}


		return () -> {System.out.println("Admin already exists!");};

	}
}
