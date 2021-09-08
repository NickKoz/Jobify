package com.webdev.jobify;

import com.webdev.jobify.model.Administrator;
import com.webdev.jobify.services.AdministratorService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;



@SpringBootApplication
public class Jobify {

	public static void main(String[] args) {
		SpringApplication.run(Jobify.class, args);
	}


	@Bean
	CommandLineRunner run(AdministratorService adminService) {

		Long id = 1L;
		String adminEmail = "admin@jobify.com";
		String adminPassword = "admin";

		return args -> {
			adminService.addAdministrator(new Administrator(id, "John", "Papadopoulos", adminEmail, adminPassword));
		};

	}
}
