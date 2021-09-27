package com.webdev.jobify.controllers;


import com.webdev.jobify.assemblers.EmployeeModelAssembler;
import com.webdev.jobify.assemblers.JobAdModelAssembler;
import com.webdev.jobify.model.Employee;
import com.webdev.jobify.model.JobAd;
import com.webdev.jobify.services.EmployeeService;
import com.webdev.jobify.services.JobAdService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@CrossOrigin(origins = "https://localhost:4200")
@RequestMapping("/jobad")
public class JobAdController {

    private final JobAdService jobAdService;
    private final JobAdModelAssembler assembler;
    private final EmployeeService employeeService;
    private final EmployeeModelAssembler employeeAssembler;

    public JobAdController(JobAdService jobAdService, JobAdModelAssembler assembler, EmployeeService employeeService, EmployeeModelAssembler employeeAssembler) {
        this.jobAdService = jobAdService;
        this.assembler = assembler;
        this.employeeService = employeeService;
        this.employeeAssembler = employeeAssembler;
    }

    @GetMapping("/{id}")
    public EntityModel<JobAd> getJobAdById(@PathVariable("id") Long id) {
        JobAd jobAd = jobAdService.findJobAdById(id);
        return assembler.toModel(jobAd);
    }

    @GetMapping("/{id}/applicants")
    public CollectionModel<EntityModel<Employee>> getJobAdApplicants(@PathVariable("id") Long id) {
        JobAd jobAd = jobAdService.findJobAdById(id);
        List<EntityModel<Employee>> applicants = jobAd.getApplicants().stream().map(employeeAssembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(applicants, linkTo(methodOn(EmployeeController.class).getAllEmployees()).withSelfRel());
    }

    @GetMapping("/all")
    public CollectionModel<EntityModel<JobAd>> getAllJobAds() {
        List<EntityModel<JobAd>> jobAds = jobAdService.findAllJobAds().stream().map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(jobAds, linkTo(methodOn(JobAdController.class).getAllJobAds()).withSelfRel());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addJobAd(@RequestBody JobAd jobAd, @RequestParam("creator_id") Long id) {

        EntityModel<JobAd> entityModel;

        try {
            Employee employee = employeeService.findEmployeeById(id);
            jobAd.setCreator(employee);
            entityModel = assembler.toModel(jobAdService.saveJobAd(jobAd));
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cannot add JobAd!");
        }

        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @PutMapping("/addapplicant")
    public ResponseEntity<?> addApplicantToJobAd(@RequestParam("jobad_id") Long id, @RequestParam("applicant_id") Long emp_id) {

        JobAd updatedJobAd;
        Employee applicant;
        try {
            updatedJobAd = jobAdService.findJobAdById(id);
            applicant = employeeService.findEmployeeById(emp_id);

            LinkedList<Employee> applicants = new LinkedList<>(updatedJobAd.getApplicants());

            applicants.push(applicant);

            updatedJobAd.setApplicants(applicants);

            jobAdService.saveJobAd(updatedJobAd);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cannot add applicant!");
        }


        return ResponseEntity.status(HttpStatus.OK).body("Applicant added!");
    }








}
