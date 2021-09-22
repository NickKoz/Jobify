package com.webdev.jobify.controllers;


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

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/jobad")
public class JobAdController {

    private final JobAdService jobAdService;
    private final JobAdModelAssembler assembler;
    private final EmployeeService employeeService;

    public JobAdController(JobAdService jobAdService, JobAdModelAssembler assembler, EmployeeService employeeService) {
        this.jobAdService = jobAdService;
        this.assembler = assembler;
        this.employeeService = employeeService;
    }

    @GetMapping("/{id}")
    public EntityModel<JobAd> getJobAdById(@PathVariable("id") Long id) {
        JobAd jobAd = jobAdService.findJobAdById(id);
        return assembler.toModel(jobAd);
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



}
