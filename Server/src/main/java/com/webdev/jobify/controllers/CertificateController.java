package com.webdev.jobify.controllers;

import com.webdev.jobify.assemblers.CertificateModelAssembler;
import com.webdev.jobify.model.Certificate;
import com.webdev.jobify.model.Employee;
import com.webdev.jobify.services.CertificateService;
import com.webdev.jobify.services.EmployeeService;
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
@CrossOrigin(origins = "https://localhost:4200")
@RequestMapping("/certificate")
public class CertificateController {

    private final CertificateService certificateService;
    private final CertificateModelAssembler assembler;
    private final EmployeeService employeeService;

    public CertificateController(CertificateService certificateService, CertificateModelAssembler assembler, EmployeeService employeeService) {
        this.certificateService = certificateService;
        this.assembler = assembler;
        this.employeeService = employeeService;
    }

    @GetMapping("/{id}")
    public EntityModel<Certificate> getCertificateById(@PathVariable("id") Long id) {
        Certificate certificate = certificateService.findCertificateById(id);
        return assembler.toModel(certificate);
    }

    @GetMapping("/all")
    public CollectionModel<EntityModel<Certificate>> getAllCertificates() {
        List<EntityModel<Certificate>> certificates = certificateService.findAllCertificates().stream().map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(certificates, linkTo(methodOn(CertificateController.class).getAllCertificates()).withSelfRel());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addCertificateToEmployee(@RequestBody Certificate certificate, @RequestParam("employee_id") Long employee_id) {

        EntityModel<Certificate> entityModel;

        try {
            Employee employee = employeeService.findEmployeeById(employee_id);
            certificate.setEmployee(employee);
            entityModel= assembler.toModel(certificateService.saveCertificate(certificate));
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cannot add certificate!");
        }


        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

}
