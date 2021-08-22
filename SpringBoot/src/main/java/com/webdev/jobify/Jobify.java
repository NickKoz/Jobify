package com.webdev.jobify;

import com.webdev.jobify.controllers.EmployeeController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
//@ComponentScan(basePackageClasses = EmployeeController.class)
public class Jobify {

	public static void main(String[] args) {
		SpringApplication.run(Jobify.class, args);
	}

}
