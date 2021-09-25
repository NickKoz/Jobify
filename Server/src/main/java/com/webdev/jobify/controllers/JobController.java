package com.webdev.jobify.controllers;


import com.webdev.jobify.assemblers.JobModelAssembler;
import com.webdev.jobify.model.Employee;
import com.webdev.jobify.model.Job;
import com.webdev.jobify.services.EmployeeService;
import com.webdev.jobify.services.JobService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;


@RestController
@CrossOrigin(origins = "https://localhost:4200")
@RequestMapping("/job")
public class JobController {

    private final JobService jobService;
    private final JobModelAssembler assembler;
    private final EmployeeService employeeService;

    public JobController(JobService jobService, JobModelAssembler assembler, EmployeeService employeeService) {
        this.jobService = jobService;
        this.assembler = assembler;
        this.employeeService = employeeService;
    }

    @GetMapping("/{id}")
    public EntityModel<Job> getJobById(@PathVariable("id") Long id) {
        Job job = jobService.findJobById(id);
        return assembler.toModel(job);
    }

    @GetMapping("/all")
    public CollectionModel<EntityModel<Job>> getAllJobs() {
        List<EntityModel<Job>> jobs = jobService.findAllJobs().stream().map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(jobs, linkTo(methodOn(JobController.class).getAllJobs()).withSelfRel());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addJobToEmployee(@RequestBody Job job, @RequestParam("employee_id") Long employee_id) {

        EntityModel<Job> entityModel;

        try {
            Employee employee = employeeService.findEmployeeById(employee_id);
            job.setEmployee(employee);
            entityModel= assembler.toModel(jobService.saveJob(job));
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cannot add job!");
        }


        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

}
